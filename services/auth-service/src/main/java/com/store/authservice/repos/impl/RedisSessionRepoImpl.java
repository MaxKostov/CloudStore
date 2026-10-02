package com.store.authservice.repos.impl;

import com.store.authservice.dto.SessionInfo;
import com.store.authservice.repos.RedisSessionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.Optional;

@Repository
public class RedisSessionRepoImpl implements RedisSessionRepo {
    private final RedisTemplate<String, SessionInfo> redisTemplate;

    @Autowired
    public RedisSessionRepoImpl(RedisTemplate<String, SessionInfo> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Optional<SessionInfo> getSession(String token) {
        return Optional.ofNullable(redisTemplate.opsForValue().get("session:"+token));
    }

    @Override
    public boolean checkToken(String token) {
        return redisTemplate.hasKey("session:"+token);
    }

    @Override
    public void saveSession(String token, SessionInfo sessionInfo, Duration ttl) {
        redisTemplate.opsForValue().set("session:"+token, sessionInfo, ttl);
    }

    @Override
    public void deleteSession(String token) {
        redisTemplate.delete("session:"+token);
    }
}
