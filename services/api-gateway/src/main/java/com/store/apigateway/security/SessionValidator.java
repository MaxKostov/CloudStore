package com.store.apigateway.security;

import com.store.apigateway.dto.SessionInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class SessionValidator {

    private final ReactiveRedisTemplate<String, SessionInfo> reactiveRedisTemplate;

    @Autowired
    public SessionValidator(ReactiveRedisTemplate<String, SessionInfo> reactiveRedisTemplate) {
        this.reactiveRedisTemplate = reactiveRedisTemplate;
    }

    public Mono<SessionInfo> validate(String token) {
        return reactiveRedisTemplate.opsForValue()
                .get("session:" + token)
                .switchIfEmpty(
                        Mono.error(
                                new BadCredentialsException("Invalid or expired session")
                        )
                );
    }
}
