package com.store.authservice.repos;

import com.store.authservice.dto.SessionInfo;

import java.time.Duration;
import java.util.Optional;

public interface RedisSessionRepo {
    Optional<SessionInfo> getSession(String token);
    boolean checkToken(String token);
    void saveSession(String token, SessionInfo sessionInfo, Duration ttl);
    void deleteSession(String token);
}
