package com.store.authservice.services;

import com.store.authservice.dto.SessionInfo;
import com.store.authservice.dto.UserDTO;
import com.store.authservice.entities.enums.UserRole;
import com.store.authservice.entities.enums.UserStatus;
import com.store.authservice.exceptions.SessionDoesntExistException;
import com.store.authservice.repos.RedisSessionRepo;
import com.store.authservice.services.impl.SessionServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SessionServiceImplTest {

    @Mock
    private RedisSessionRepo redisSessionRepo;

    @InjectMocks
    private SessionServiceImpl sessionService;

    @Test
    void createSession_shouldSaveSessionWithThirtyMinuteTtl() {
        UserDTO user = new UserDTO();
        user.setId(42L);
        user.setEmail("maxim@test.com");
        user.setRole(UserRole.USER);
        user.setStatus(UserStatus.ACTIVE);

        String token = sessionService.createSession(user);

        assertNotNull(token);
        assertFalse(token.isBlank());

        ArgumentCaptor<String> tokenCaptor =
                ArgumentCaptor.forClass(String.class);

        ArgumentCaptor<SessionInfo> sessionCaptor =
                ArgumentCaptor.forClass(SessionInfo.class);

        ArgumentCaptor<Duration> ttlCaptor =
                ArgumentCaptor.forClass(Duration.class);

        verify(redisSessionRepo).saveSession(
                tokenCaptor.capture(),
                sessionCaptor.capture(),
                ttlCaptor.capture()
        );

        assertEquals(token, tokenCaptor.getValue());
        assertEquals(Duration.ofMinutes(30), ttlCaptor.getValue());

        SessionInfo session = sessionCaptor.getValue();

        assertEquals(42L, session.getUserId());
        assertEquals("maxim@test.com", session.getEmail());
        assertEquals(UserRole.USER, session.getRole());
        assertEquals(UserStatus.ACTIVE, session.getUserStatus());
    }

    @Test
    void getSession_whenSessionExists_shouldReturnSession() {
        SessionInfo sessionInfo = new SessionInfo();

        when(redisSessionRepo.getSession("token"))
                .thenReturn(Optional.of(sessionInfo));

        SessionInfo result = sessionService.getSession("token");

        assertSame(sessionInfo, result);
    }

    @Test
    void getSession_whenSessionDoesNotExist_shouldThrowException() {
        when(redisSessionRepo.getSession("token"))
                .thenReturn(Optional.empty());

        assertThrows(
                SessionDoesntExistException.class,
                () -> sessionService.getSession("token")
        );
    }

    @Test
    void checkToken_shouldReturnRepositoryResult() {
        when(redisSessionRepo.checkToken("token"))
                .thenReturn(true);

        assertTrue(sessionService.checkToken("token"));
    }

    @Test
    void logout_shouldDeleteSession() {
        sessionService.logout("token");

        verify(redisSessionRepo).deleteSession("token");
    }
}
