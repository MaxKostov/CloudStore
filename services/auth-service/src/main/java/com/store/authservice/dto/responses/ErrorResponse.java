package com.store.authservice.dto.responses;

import lombok.Data;

import java.time.Instant;

@Data
public class ErrorResponse {
    private int status;
    private String error;
    private String message;
    private String path;
    private Instant timestamp;
}
