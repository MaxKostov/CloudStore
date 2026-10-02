package com.store.authservice.services;

import com.store.authservice.dto.SessionInfo;
import com.store.authservice.dto.UserDTO;

public interface SessionService {
    String createSession(UserDTO userDTO);
    SessionInfo getSession(String token);
    boolean checkToken(String token);
    void logout(String token);
}
