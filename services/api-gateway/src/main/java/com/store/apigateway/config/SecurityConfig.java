package com.store.apigateway.config;

import com.store.apigateway.security.SessionValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
public class SecurityConfig {
    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(
        ServerHttpSecurity http,
        SessionValidator sessionValidator) {



        return http
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers("/actuator/health/**", "/api/auth/register", "api/auth/login").permitAll()
                        .anyExchange().authenticated())
                .build();
    }
}
