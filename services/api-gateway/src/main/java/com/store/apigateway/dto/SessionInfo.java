package com.store.apigateway.dto;

public record SessionInfo(
        Long userId,
        String email,
        String role,
        String userStatus
) {
}
