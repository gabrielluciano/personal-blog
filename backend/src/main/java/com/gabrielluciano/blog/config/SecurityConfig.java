package com.gabrielluciano.blog.config;

import com.gabrielluciano.blog.models.Role;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf().disable() // TODO: Replace this line with csrf configuration
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.GET, "/posts/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/tags/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/signup", "/login").permitAll()
                        // .anyRequest().hasRole(Role.ADMIN.name())) // TODO: Uncomment this line when authentication is implement
                        .anyRequest().permitAll()) // TODO: Remove this line when authentication is implemented
                .build();
    }
}
