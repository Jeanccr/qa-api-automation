package com.agibank.api.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Modelo de resposta para GET /breed/{breed}/images
 * Retorna lista de imagens de uma raça específica
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BreedImagesResponse {
    
    @JsonProperty("message")
    private List<String> message;
    
    @JsonProperty("status")
    private String status;

    /**
     * Obtém lista de URLs das imagens / mensagem
     */
    public List<String> getMessage() {
        return message != null ? message : List.of();
    }

    /**
     * Obtém lista de URLs das imagens
     */
    public List<String> getImageUrls() {
        return getMessage();
    }

    /**
     * Obtém quantidade de imagens
     */
    public int getImageCount() {
        return message != null ? message.size() : 0;
    }

    /**
     * Valida se há imagens
     */
    public boolean hasImages() {
        return message != null && !message.isEmpty();
    }

    /**
     * Valida se todas as URLs são válidas (começam com http)
     */
    public boolean allUrlsValid() {
        return message != null && message.stream().allMatch(url -> url.startsWith("http"));
    }
}
