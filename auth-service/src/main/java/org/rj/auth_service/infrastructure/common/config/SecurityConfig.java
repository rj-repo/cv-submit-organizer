package org.rj.auth_service.infrastructure.common.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {


    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf(AbstractHttpConfigurer::disable);
        httpSecurity.authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/login/**").permitAll()
                        .requestMatchers("/auth/registration/**").permitAll()
                        .requestMatchers("/auth/verification", "/auth/verification/**").permitAll()
                        .requestMatchers("/auth/validation/**").permitAll()
                        .anyRequest().authenticated())
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .httpBasic(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return httpSecurity.build();
    }
}
