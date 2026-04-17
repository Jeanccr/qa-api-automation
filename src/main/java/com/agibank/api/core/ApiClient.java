package com.agibank.api.core;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static io.restassured.RestAssured.given;

/**
 * Classe base para requisições de API
 * Encapsula os métodos HTTP comuns (GET, POST, PUT, DELETE, PATCH)
 */
public class ApiClient {

    private static final Logger logger = LogManager.getLogger(ApiClient.class);
    protected RequestSpecification requestSpec;

    public ApiClient(RequestSpecification requestSpec) {
        this.requestSpec = requestSpec;
    }

    public ApiClient() {
        this.requestSpec = ApiSpecBuilder.defaultSpec();
    }

    /**
     * Executa requisição GET
     */
    public Response get(String endpoint) {
        logger.info("Executando GET request: {}", endpoint);
        return given()
                .spec(requestSpec)
                .when()
                .get(endpoint)
                .then()
                .log().all()
                .extract().response();
    }

    /**
     * Executa requisição GET com path parameters
     */
    public Response get(String endpoint, String... pathParams) {
        logger.info("Executando GET request: {} com params: {}", endpoint, pathParams);
        return given()
                .spec(requestSpec)
                .when()
                .get(endpoint, (Object[]) pathParams)
                .then()
                .log().all()
                .extract().response();
    }

    /**
     * Executa requisição POST com body
     */
    public Response post(String endpoint, Object body) {
        logger.info("Executando POST request: {} com body: {}", endpoint, body);
        return given()
                .spec(requestSpec)
                .body(body)
                .when()
                .post(endpoint)
                .then()
                .log().all()
                .extract().response();
    }

    /**
     * Executa requisição POST sem body
     */
    public Response post(String endpoint) {
        logger.info("Executando POST request: {}", endpoint);
        return given()
                .spec(requestSpec)
                .when()
                .post(endpoint)
                .then()
                .log().all()
                .extract().response();
    }

    /**
     * Executa requisição PUT com body
     */
    public Response put(String endpoint, Object body) {
        logger.info("Executando PUT request: {} com body: {}", endpoint, body);
        return given()
                .spec(requestSpec)
                .body(body)
                .when()
                .put(endpoint)
                .then()
                .log().all()
                .extract().response();
    }

    /**
     * Executa requisição PUT sem body
     */
    public Response put(String endpoint) {
        logger.info("Executando PUT request: {}", endpoint);
        return given()
                .spec(requestSpec)
                .when()
                .put(endpoint)
                .then()
                .log().all()
                .extract().response();
    }

    /**
     * Executa requisição PATCH com body
     */
    public Response patch(String endpoint, Object body) {
        logger.info("Executando PATCH request: {} com body: {}", endpoint, body);
        return given()
                .spec(requestSpec)
                .body(body)
                .when()
                .patch(endpoint)
                .then()
                .log().all()
                .extract().response();
    }

    /**
     * Executa requisição DELETE
     */
    public Response delete(String endpoint) {
        logger.info("Executando DELETE request: {}", endpoint);
        return given()
                .spec(requestSpec)
                .when()
                .delete(endpoint)
                .then()
                .log().all()
                .extract().response();
    }

    /**
     * Executa requisição DELETE com body
     */
    public Response delete(String endpoint, Object body) {
        logger.info("Executando DELETE request: {} com body: {}", endpoint, body);
        return given()
                .spec(requestSpec)
                .body(body)
                .when()
                .delete(endpoint)
                .then()
                .log().all()
                .extract().response();
    }

    /**
     * Executa requisição HEAD
     */
    public Response head(String endpoint) {
        logger.info("Executando HEAD request: {}", endpoint);
        return given()
                .spec(requestSpec)
                .when()
                .head(endpoint)
                .then()
                .log().all()
                .extract().response();
    }

    /**
     * Atualiza a especificação de requisição
     */
    public void setRequestSpec(RequestSpecification newSpec) {
        this.requestSpec = newSpec;
    }

    /**
     * Retorna a especificação atual
     */
    public RequestSpecification getRequestSpec() {
        return requestSpec;
    }
}
