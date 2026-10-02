package com.store.authservice.repos;

import com.store.authservice.config.RedisConfig;
import com.store.authservice.dto.SessionInfo;
import com.store.authservice.repos.impl.RedisSessionRepoImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.redis.test.autoconfigure.DataRedisTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Duration;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@DataRedisTest
@Import({
        RedisSessionRepoImpl.class,
        RedisConfig.class
})
class RedisSessionRepoIntegrationTest {

    @Container
    static GenericContainer<?> redis =
            new GenericContainer<>("redis:8")
                    .withExposedPorts(6379);

    @DynamicPropertySource
    static void redisProperties(
            DynamicPropertyRegistry registry
    ) {
        registry.add(
                "spring.data.redis.host",
                redis::getHost
        );

        registry.add(
                "spring.data.redis.port",
                () -> redis.getMappedPort(6379)
        );
    }

    @Autowired
    private RedisSessionRepo redisSessionRepo;

    @Test
    void saveAndGetSessionTest() {
        SessionInfo session = new SessionInfo();
        session.setUserId(42L);

        redisSessionRepo.saveSession(
                "abc",
                session,
                Duration.ofMinutes(30)
        );

        Optional<SessionInfo> result =
                redisSessionRepo.getSession("abc");

        assertTrue(result.isPresent());
        assertEquals(42L, result.get().getUserId());
    }

    @Test
    void sessionTTLTest() throws InterruptedException {
        SessionInfo session = new SessionInfo();
        session.setUserId(42L);
        redisSessionRepo.saveSession(
                "short-lived",
                session,
                Duration.ofSeconds(1)
        );

        assertTrue(redisSessionRepo.checkToken("short-lived"));

        Thread.sleep(1500);

        assertFalse(redisSessionRepo.checkToken("short-lived"));
    }
}
