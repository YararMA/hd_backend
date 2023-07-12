package com.github.dlism.backend.services;

import com.github.dlism.backend.dto.user.UserDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SecurityService {
    private final AuthenticationManager authenticationManager;

    private final SecurityContextRepository securityContextRepository;

    public void login(UserDto user, HttpServletRequest request, HttpServletResponse response) {

        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());

        Authentication authentication = authenticationManager.authenticate(token);
        if (authentication.isAuthenticated()) {
            SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder
                    .getContextHolderStrategy();
            SecurityContext context = securityContextHolderStrategy.createEmptyContext();
            context.setAuthentication(authentication);
            securityContextHolderStrategy.setContext(context);

            securityContextRepository.saveContext(context, request, response);
        }
    }
}
