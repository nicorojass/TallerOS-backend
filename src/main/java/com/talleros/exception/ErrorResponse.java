package com.talleros.exception;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorResponse {

    private int status;
    private String error;
    private Map<String, String> errores; // para errores de validación por campo
    private LocalDateTime timestamp;
}
