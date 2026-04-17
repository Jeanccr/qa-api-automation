package com.agibank.api.core;

import com.agibank.api.config.TestConfig;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

/**
 * Classe base para construir especificações de request e response padrão
 * Reutiliza RequestSpecification do RestAssured
 */
public class ApiSpecBuilder {
    
    private final RequestSpecBuilder requestSpecBuilder;
    private final ResponseSpecBuilder responseSpecBuilder;

    public ApiSpecBuilder() {
        this.requestSpecBuilder = new RequestSpecBuilder();
        this.responseSpecBuilder = new ResponseSpecBuilder();
        initializeDefaults();
    }

    /**
     * Inicializa configurações padrão para todas as requisições
     */
    private void initializeDefaults() {
        requestSpecBuilder
                .setBaseUri(TestConfig.getBaseUrl())
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .setRelaxedHTTPSValidation();

        if (TestConfig.isLoggingEnabled()) {
            requestSpecBuilder
                    .addFilter(new RequestLoggingFilter())
                    .addFilter(new ResponseLoggingFilter());
        }

        responseSpecBuilder
                .expectContentType(ContentType.JSON);
    }

    /**
     * Adiciona header customizado
     */
    public ApiSpecBuilder withHeader(String key, String value) {
        requestSpecBuilder.addHeader(key, value);
        return this;
    }

    /**
     * Adiciona múltiplos headers
     */
    public ApiSpecBuilder withHeaders(java.util.Map<String, String> headers) {
        headers.forEach((key, value) -> requestSpecBuilder.addHeader(key, value));
        return this;
    }

    /**
     * Adiciona autenticação Bearer token
     */
    public ApiSpecBuilder withBearerToken(String token) {
        requestSpecBuilder.addHeader("Authorization", "Bearer " + token);
        return this;
    }

    /**
     * Adiciona autenticação Basic
     */
    public ApiSpecBuilder withBasicAuth(String username, String password) {
        String auth = java.util.Base64.getEncoder()
                .encodeToString((username + ":" + password).getBytes(java.nio.charset.StandardCharsets.UTF_8));
        requestSpecBuilder.addHeader("Authorization", "Basic " + auth);
        return this;
    }

    /**
     * Adiciona query parameter
     */
    public ApiSpecBuilder withQueryParam(String key, String value) {
        requestSpecBuilder.addQueryParam(key, value);
        return this;
    }

    /**
     * Adiciona múltiplos query parameters
     */
    public ApiSpecBuilder withQueryParams(java.util.Map<String, String> params) {
        params.forEach((key, value) -> requestSpecBuilder.addQueryParam(key, value));
        return this;
    }

    /**
     * Adiciona path parameter
     */
    public ApiSpecBuilder withPathParam(String key, String value) {
        requestSpecBuilder.addPathParam(key, value);
        return this;
    }

    /**
     * Define tipo de conteúdo
     */
    public ApiSpecBuilder withContentType(ContentType contentType) {
        requestSpecBuilder.setContentType(contentType);
        return this;
    }

    /**
     * Define Accept header
     */
    public ApiSpecBuilder withAccept(ContentType contentType) {
        requestSpecBuilder.setAccept(contentType);
        return this;
    }

    /**
     * Desativa logging de requisição/resposta
     */
    public ApiSpecBuilder disableLogging() {
        return this;
    }

    /**
     * Constrói e retorna RequestSpecification pronta para uso
     */
    public RequestSpecification build() {
        return requestSpecBuilder.build();
    }

    /**
     * Retorna ResponseSpecification para validações padrão
     */
    public ResponseSpecification buildResponseSpec() {
        return responseSpecBuilder.build();
    }

    /**
     * Cria uma nova instância de ApiSpecBuilder
     */
    public static ApiSpecBuilder builder() {
        return new ApiSpecBuilder();
    }

    /**
     * Retorna especificação padrão sem customizações
     */
    public static RequestSpecification defaultSpec() {
        return new ApiSpecBuilder().build();
    }
}
