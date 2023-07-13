package com.github.dlism.backend.services;

import com.github.dlism.backend.dto.RabbitmqDto;
import com.github.dlism.backend.dto.user.UserDto;
import com.github.dlism.backend.dto.user.UserUpdatePasswordDto;
import com.github.dlism.backend.dto.user.UserUpdateProfileDto;
import com.github.dlism.backend.exceptions.DuplicateRecordException;
import com.github.dlism.backend.exceptions.UpdateException;
import com.github.dlism.backend.exceptions.UserNotFoundException;
import com.github.dlism.backend.mappers.UserMapper;
import com.github.dlism.backend.models.Organization;
import com.github.dlism.backend.models.Role;
import com.github.dlism.backend.models.User;
import com.github.dlism.backend.models.UserActivationCode;
import com.github.dlism.backend.repositories.UserActivationCodeRepository;
import com.github.dlism.backend.repositories.UserRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final ProduceService produceService;

    private final UserActivationCodeRepository activationCodeRepository;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, ProduceService produceService, UserActivationCodeRepository activationCodeRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.produceService = produceService;
        this.activationCodeRepository = activationCodeRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Transactional
    public void create(UserDto userDto) throws DuplicateRecordException, IllegalArgumentException {

        if (!userDto.getPassword().equals(userDto.getPasswordConfirmation())) {
            throw new IllegalArgumentException("Пароль и подтверждение пароля не совпадают!");
        }

        saveUser(userDto, Role.ROLE_USER);
    }

    @Transactional
    public void createOrganizer(UserDto userDto) throws DuplicateRecordException, IllegalArgumentException {

        if (!userDto.getPassword().equals(userDto.getPasswordConfirmation())) {
            throw new IllegalArgumentException("Пароль и подтверждение пароля не совпадают!");
        }

        saveUser(userDto, Role.ROLE_ORGANIZER);
    }


    private void saveUser(UserDto userDto, Role role) {
        try {
            User user = UserMapper.INSTANCE.dtoToEntity(userDto);
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));
            user.setRoles(new HashSet<>(List.of(new Role[]{role, Role.ROLE_NOT_ACTIVE})));
            user.setActive(true);

            UUID code = UUID.randomUUID();
            userRepository.save(user);
            activationCodeRepository.save(UserActivationCode.builder().user(user).code(code.toString()).build());
            produceService.produceAnswer(new RabbitmqDto(user.getUsername(), code));

        } catch (DataIntegrityViolationException e) {
            throw new DuplicateRecordException("Пользовател с таким именим уже существует");
        }
    }

    public long count() {
        return userRepository.count();
    }

    public Page<User> getAllUsers(Pageable page) {
        return userRepository.findAll(page);
    }

    @Transactional
    public UserUpdateProfileDto update(User user, UserUpdateProfileDto userDto) throws DuplicateRecordException, IllegalArgumentException {
        try {
            User userForUpdate=
                    userRepository.findById(user.getId())
                            .orElseThrow(()->new UserNotFoundException("Пользователь не найден"));

            userForUpdate.setUsername(userDto.getUsername());
            userForUpdate.setName(userDto.getName());
            userForUpdate.setFirstname(userDto.getFirstname());
            userForUpdate.setLastname(userDto.getLastname());
            userForUpdate.setPhone(userDto.getPhone());
            userForUpdate.setGender(userDto.getGender());
            userForUpdate.setBirthday(userDto.getBirthday());
            userForUpdate.setCountry(userDto.getCountry());
            userForUpdate.setRegion(userDto.getRegion());
            userForUpdate.setLocality(userDto.getLocality());
            userForUpdate.setTypeOfActivity(userDto.getTypeOfActivity());

            return UserMapper.INSTANCE.entityToUpdateProfileDto(userRepository.save(userForUpdate));

        } catch (DataIntegrityViolationException e) {
            throw new DuplicateRecordException("Пользовател с таким именим уже существует");
        }
    }

    @Transactional
    public Optional<User> active(String activationCode) {
        Optional<UserActivationCode> userOptional = activationCodeRepository.findByCode(activationCode);

        if (userOptional.isPresent()) {
            User user = userOptional.orElseThrow(() -> new UserNotFoundException("Пользователь не найден")).getUser();
            user.setActive(true);
            userRepository.save(user);
            activationCodeRepository.deleteByCode(activationCode);
            return Optional.of(user);
        }
        return Optional.empty();
    }

    @Transactional
    public void subscribeToOrganization(Organization organization, User user) {
        try {
            user.setOrganization(organization);
            userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateRecordException("Вы уже подписаны на эту организацию");
        }
    }

    public UserUpdateProfileDto getById(Long id) {
        return UserMapper.INSTANCE.entityToUpdateProfileDto(userRepository.getReferenceById(id));
    }

    @Transactional
    public void changePassword(User user, UserUpdatePasswordDto userUpdatePasswordDto) {
        if (!userUpdatePasswordDto.getNewPassword().equals(userUpdatePasswordDto.getNewPasswordConfirmation())) {
            throw new IllegalArgumentException("Пароль и подтверждение пароля не совпадают!");
        }

        try {
            Optional<User> userOptional = userRepository.findById(user.getId());
            if (userOptional.isPresent()) {
                User updateUser = userOptional.get();
                updateUser.setPassword(passwordEncoder.encode(userUpdatePasswordDto.getNewPassword()));
                userRepository.save(updateUser);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new UpdateException("Ошибка при изминения пароля!");
        }
    }
}
