package com.agibank.api.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * Modelo de resposta para GET /breeds/list/all
 * Retorna todas as raças de cães com sub-raças
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BreedsListResponse {
    
    @JsonProperty("message")
    private Map<String, List<String>> message;
    
    @JsonProperty("status")
    private String status;

    /**
     * Obtém lista de todas as raças principais
     */
    public List<String> getAllBreeds() {
        return message != null ? message.keySet().stream().toList() : List.of();
    }

    /**
     * Obtém sub-raças de uma raça específica
     */
    public List<String> getSubBreeds(String breed) {
        return message != null ? message.getOrDefault(breed, List.of()) : List.of();
    }

    /**
     * Valida se raça existe
     */
    public boolean hasBreed(String breed) {
        return message != null && message.containsKey(breed);
    }

    /**
     * Obtém quantidade de raças
     */
    public int getBreedCount() {
        return message != null ? message.size() : 0;
    }
}
