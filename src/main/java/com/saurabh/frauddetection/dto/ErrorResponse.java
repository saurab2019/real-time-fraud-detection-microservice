package com.saurabh.frauddetection.dto;

import com.saurabh.frauddetection.exception.ErrorCode;

import java.time.LocalDateTime;
import java.util.List;

public record ErrorResponse(
        ErrorCode errorCode,
        String messages,
        LocalDateTime timestamp
) {}
