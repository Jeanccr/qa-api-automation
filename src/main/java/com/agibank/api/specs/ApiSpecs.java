package com.agibank.api.specs;

import io.restassured.specification.RequestSpecification;
import com.agibank.api.core.ApiSpecBuilder;

/**
 * Especificações pré-construídas para diferentes cenários
 * Exemplo: autenticação, headers específicos, etc.
 */
public class ApiSpecs {

    /**
     * Especificação com bearer token
     */
    public static RequestSpecification withBearerToken(String token) {
        return ApiSpecBuilder.builder()
                .withBearerToken(token)
                .build();
    }

    /**
     * Especificação com autenticação básica
     */
    public static RequestSpecification withBasicAuth(String username, String password) {
        return ApiSpecBuilder.builder()
                .withBasicAuth(username, password)
                .build();
    }

    /**
     * Especificação com headers customizados
     */
    public static RequestSpecification withCustomHeaders(java.util.Map<String, String> headers) {
        return ApiSpecBuilder.builder()
                .withHeaders(headers)
                .build();
    }

    /**
     * Especificação padrão
     */
    public static RequestSpecification defaultSpec() {
        return ApiSpecBuilder.builder().build();
    }
}
