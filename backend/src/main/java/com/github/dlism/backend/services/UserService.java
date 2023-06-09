package com.github.dlism.backend.services;

import com.github.dlism.backend.dto.UserDto;
import com.github.dlism.backend.exceptions.DuplicateRecordException;
import com.github.dlism.backend.mappers.UserMapper;
import com.github.dlism.backend.models.Organization;
import com.github.dlism.backend.models.Role;
import com.github.dlism.backend.models.User;
import com.github.dlism.backend.pojo.UserPojo;
import com.github.dlism.backend.repositories.UserRepository;
import com.github.dlism.model.RabbitmqDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ProduceService produceService;

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

        User user = UserMapper.INSTANCE.dtoToEntity(userDto);
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setRoles(Collections.singleton(Role.ROLE_USER));

        try {
            UUID code = UUID.randomUUID();
            userRepository.save(user);
            userRepository.saveActivationCode(code.toString(), user.getId());
            produceService.produceAnswer(new RabbitmqDto(user.getUsername(), code));
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateRecordException("Пользовател с таким именим уже существует");
        }

    }

    public boolean hasOrganization(User user) {
        return userRepository.findByUsername(user.getUsername())
                .map(User::getOrganization)
                .isPresent();
    }

    public long count() {
        return userRepository.count();
    }

    public List<UserPojo> getAllUsers() {
        return userRepository.all();
    }

    @Transactional
    public UserDto update(User user, UserDto userDto) throws DuplicateRecordException, IllegalArgumentException {

        if (!userDto.getPassword().equals(userDto.getPasswordConfirmation())) {
            throw new IllegalArgumentException("Пароль и подтверждение пароля не совпадают!");
        }
        user.setUsername(userDto.getUsername());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        try {
            user = userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateRecordException("Пользовател с таким именим уже существует");
        }

        return UserMapper.INSTANCE.entityToDto(user);
    }

    @Transactional
    public Optional<User> active(String activationCode) {
        Optional<User> userOptional = userRepository.getUserByActivationCode(activationCode);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setActive(true);
            userRepository.save(user);
            userRepository.deleteActivationCodeByCode(activationCode);
        }

        return userOptional;
    }

    @Transactional
    public void subscribeToOrganization(Organization organization, User user){
        try {
            userRepository.joinToOrganization(user.getId(), organization.getId());
        }catch (DataIntegrityViolationException e){
            throw new DuplicateRecordException("Вы уже подписаны в эту организацию");
        }
    }


}
