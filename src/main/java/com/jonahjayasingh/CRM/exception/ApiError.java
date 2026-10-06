package com.jonahjayasingh.CRM.exception;

import java.time.LocalDateTime;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiError {
    private LocalDateTime timestamp;
    private int status;
    private String message;
    private Map<String, String> errors;
}
