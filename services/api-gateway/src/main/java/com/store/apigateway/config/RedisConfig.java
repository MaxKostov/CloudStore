package com.store.apigateway.config;

import com.store.apigateway.dto.SessionInfo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.JacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Bean
    public ReactiveRedisTemplate<String, SessionInfo> redisOperations(ReactiveRedisConnectionFactory factory) {
        JdkSerializationRedisSerializer jdkSerializationRedisSerializer = new JdkSerializationRedisSerializer();
        StringRedisSerializer stringRedisSerializer = StringRedisSerializer.UTF_8;
        JacksonJsonRedisSerializer<SessionInfo> sessionToStringSerializer = new JacksonJsonRedisSerializer<>(SessionInfo.class);

        return new ReactiveRedisTemplate<>(factory,
                RedisSerializationContext.<String, SessionInfo>newSerializationContext(jdkSerializationRedisSerializer)
                        .key(stringRedisSerializer).value(sessionToStringSerializer).build());
    }
}
