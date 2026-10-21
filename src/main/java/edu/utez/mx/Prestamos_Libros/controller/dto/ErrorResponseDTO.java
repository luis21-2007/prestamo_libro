package edu.utez.mx.Prestamos_Libros.controller.dto;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Data
public class ErrorResponseDTO {
    private int status;
    private String error;
    private String message;
    private Map<String, String> details;
    private LocalDateTime timestamp;

    public ErrorResponseDTO(int status, String error, String message) {
        this.status = status;
        this.error = error;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

    public ErrorResponseDTO(int status, String error, String message, Map<String, String> details) {
        this(status, error, message);
        this.details = details;
    }
}
