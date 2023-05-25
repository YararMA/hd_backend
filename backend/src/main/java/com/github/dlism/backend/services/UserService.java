package com.github.dlism.backend.services;

import com.github.dlism.backend.dto.UserDto;
import com.github.dlism.backend.models.Role;
import com.github.dlism.backend.models.User;
import com.github.dlism.backend.pojo.UserPojo;
import com.github.dlism.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Transactional
    public boolean createUser(UserDto userDto) {

        //TODO использовать маппер
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setPassword(userDto.getPassword());

        if (userRepository.existsByUsername(user.getUsername())) {
            return false;
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Collections.singleton(Role.ROLE_USER));

        try {
            userRepository.save(user);
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while saving user", e);
        }

        return true;
    }

    public boolean hasOrganization(User user) {
        Optional<User> userFromDB = userRepository.findByUsername(user.getUsername());

        return userFromDB
                .flatMap(u -> Optional.ofNullable(u.getOrganization()))
                .map(organization -> true)
                .orElse(false);
    }

    public long count() {
        return userRepository.count();
    }

    public List<UserPojo> getAllUsers() {
        return userRepository.all();
    }

    public boolean update(User user, UserDto userDto) {

        Optional<User> userFromDb = userRepository.findByUsername(userDto.getUsername());

        if(userFromDb.get().getId() != user.getId()){
            return false;
        }

        //TODO использовать маппер
        user.setUsername(userDto.getUsername());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        try {
            userRepository.save(user);
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while saving user", e);
        }

        return true;
    }
}
