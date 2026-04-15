package com.agibank.api.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Modelo de resposta para GET /breeds/image/random
 * Retorna uma imagem aleatória de cão
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RandomImageResponse {
    
    @JsonProperty("message")
    private String message;
    
    @JsonProperty("status")
    private String status;

    /**
     * Obtém URL da imagem (alias para getMessage)
     */
    public String getMessage() {
        return message;
    }

    /**
     * Obtém URL da imagem
     */
    public String getImageUrl() {
        return message;
    }

    /**
     * Valida se URL é válida
     */
    public boolean isValidUrl() {
        return message != null && message.startsWith("http");
    }

    /**
     * Valida se URL é JPEG
     */
    public boolean isJpegImage() {
        return message != null && message.toLowerCase().endsWith(".jpg");
    }

    /**
     * Valida se URL é PNG
     */
    public boolean isPngImage() {
        return message != null && message.toLowerCase().endsWith(".png");
    }
}
