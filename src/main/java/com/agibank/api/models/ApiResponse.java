package com.agibank.api.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Modelo de exemplo para uma resposta genérica de API
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse {
    private String status;
    private String message;
    private Object data;
    private Long timestamp;
}
