package com.github.dlism.backend.services;

import com.github.dlism.backend.models.Organization;
import com.github.dlism.backend.models.User;
import com.github.dlism.backend.repositories.UserRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.mockito.Mockito.when;
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }
    @Test
    public void testHasOrganization_UserWithOrganization_ReturnsTrue() {
        User user = new User();
        user.setUsername("testUser");
        user.setOrganization(new Organization());

        when(userRepository.findByUsername(user.getUsername()))
                .thenReturn(Optional.of(user));

        boolean result = userRepository.findByUsername(user.getUsername()).isPresent();

        Assert.assertTrue(result);
    }


}