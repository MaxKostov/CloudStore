package com.store.authservice.services.impl;

import com.store.authservice.dto.SessionInfo;
import com.store.authservice.dto.UserDTO;
import com.store.authservice.exceptions.SessionDoesntExistException;
import com.store.authservice.repos.RedisSessionRepo;
import com.store.authservice.services.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Duration;
import java.util.Base64;

@Service
public class SessionServiceImpl implements SessionService {
    private static final Duration SESSION_TTL = Duration.ofMinutes(30);
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();
    private final RedisSessionRepo redisSessionRepo;

    @Autowired
    public SessionServiceImpl(RedisSessionRepo redisSessionRepo) {
        this.redisSessionRepo = redisSessionRepo;
    }

    @Override
    public String createSession(UserDTO userDTO) {
        String token = generateToken();
        SessionInfo sessionInfo = toSessionInfo(userDTO);
        redisSessionRepo.saveSession(token, sessionInfo, SESSION_TTL);
        return token;
    }

    @Override
    public SessionInfo getSession(String token) {
        return redisSessionRepo.getSession(token).orElseThrow(SessionDoesntExistException::new);
    }

    @Override
    public boolean checkToken(String token) {
        return redisSessionRepo.checkToken(token);
    }

    @Override
    public void logout(String token) {
        redisSessionRepo.deleteSession(token);
    }

    private String generateToken() {
        byte[] bytes = new byte[32];
        SECURE_RANDOM.nextBytes(bytes);

        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    private SessionInfo toSessionInfo(UserDTO userDTO) {
        SessionInfo sessionInfo = new SessionInfo();
        sessionInfo.setEmail(userDTO.getEmail());
        sessionInfo.setUserId(userDTO.getId());
        sessionInfo.setRole(userDTO.getRole());
        sessionInfo.setUserStatus(userDTO.getStatus());
        return sessionInfo;
    }
}
