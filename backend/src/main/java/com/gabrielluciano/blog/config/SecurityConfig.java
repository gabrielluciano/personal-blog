package com.gabrielluciano.blog.config;

import com.gabrielluciano.blog.models.Role;
import com.gabrielluciano.blog.security.entrypoints.JWTAuthenticationEntryPoint;
import com.gabrielluciano.blog.security.filters.JWTAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JWTAuthenticationFilter jwtAuthenticationFilter;
    private final HandlerMappingIntrospector introspector;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(conf -> conf.configurationSource(req -> {
                    CorsConfiguration configuration = new CorsConfiguration();
                    configuration.applyPermitDefaultValues();
                    configuration.addAllowedMethod(HttpMethod.PUT);
                    configuration.addAllowedMethod(HttpMethod.DELETE);
                    return configuration;
                }))
                .sessionManagement(conf -> conf.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(conf -> conf.authenticationEntryPoint(new JWTAuthenticationEntryPoint()))
                .addFilterAt(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(antMatcher(HttpMethod.GET, "/posts/**")).permitAll()
                        .requestMatchers(antMatcher(HttpMethod.GET, "/tags/**")).permitAll()
                        .requestMatchers(antMatcher(HttpMethod.POST, "/signup")).permitAll()
                        .requestMatchers(antMatcher(HttpMethod.POST, "/login")).permitAll()
                        .requestMatchers(antMatcher("/users/**")).hasRole(Role.ADMIN.name())
                        .anyRequest().hasAnyRole(Role.EDITOR.name(), Role.ADMIN.name()))
                .build();
    }
}
