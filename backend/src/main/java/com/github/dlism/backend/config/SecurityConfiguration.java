package com.github.dlism.backend.config;

import com.github.dlism.backend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    @Autowired
    private UserService userDetailsService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((authz) -> authz
                        .requestMatchers("/",
                                "/registration",
                                "/registration-organization",
                                "/login",
                                "/static/**",
                                "/organization-list/**",
                                "/active/**").permitAll()
                        .requestMatchers("/control/**").hasRole("ADMIN")
                        .requestMatchers("/organization/**").hasRole("ORGANIZER")
                        .anyRequest().authenticated()
                )
                .formLogin()
                .permitAll()
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .permitAll()
                .and()
                .rememberMe()
                .and()
                .exceptionHandling()
                .accessDeniedPage("/access-exception");

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManagerBean(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder.getPasswordEncoder());
        return authenticationManagerBuilder.build();
    }

}
