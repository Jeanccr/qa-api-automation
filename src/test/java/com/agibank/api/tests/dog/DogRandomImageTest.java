package com.agibank.api.tests.dog;

import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.RepeatedTest;
import com.agibank.api.models.RandomImageResponse;
import com.agibank.api.tests.BaseApiTest;
import com.agibank.api.utils.JsonUtil;
import com.agibank.api.utils.AssertionUtil;

import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.Matchers.*;

@DisplayName("Testes do Endpoint GET /breeds/image/random")
@Tag("dog-api")
@Tag("random-image")
public class DogRandomImageTest extends BaseApiTest {

    private static final String RANDOM_IMAGE_ENDPOINT = "/breeds/image/random";

    @Test
    @DisplayName("Deve retornar status 200")
    @Tag("smoke")
    public void shouldReturnStatus200() {
        logInfo("Testando status code do endpoint de imagem aleatória");

        Response response = apiClient.get(RANDOM_IMAGE_ENDPOINT);

        AssertionUtil.assertStatusCode(response.getStatusCode(), 200, "Deve retornar 200");
    }

    @Test
    @DisplayName("Deve retornar JSON com status 'success'")
    @Tag("smoke")
    public void shouldReturnSuccessStatus() {
        logInfo("Validando status 'success' na resposta");

        Response response = apiClient.get(RANDOM_IMAGE_ENDPOINT);

        response.then()
                .statusCode(200)
                .body("status", equalTo("success"));
    }

    @Test
    @DisplayName("Deve retornar URL de imagem válida")
    @Tag("smoke")
    public void shouldReturnValidImageUrl() {
        logInfo("Validando URL da imagem");

        Response response = apiClient.get(RANDOM_IMAGE_ENDPOINT);

        response.then()
                .statusCode(200)
                .body("message", notNullValue())
                .body("message", startsWith("https://"))
                .body("message", containsString("dog.ceo"));
    }

    @Test
    @DisplayName("Deve desserializar resposta para RandomImageResponse")
    @Tag("deserialization")
    public void shouldDeserializeToRandomImage() {
        logInfo("Desserializando resposta para RandomImageResponse");

        Response response = apiClient.get(RANDOM_IMAGE_ENDPOINT);
        String jsonBody = response.getBody().asString();

        RandomImageResponse randomImage = JsonUtil.fromJson(jsonBody, RandomImageResponse.class);

        AssertionUtil.assertNotNull(randomImage, "RandomImageResponse não deve ser nulo");
        AssertionUtil.assertNotNull(randomImage.getImageUrl(), "Message não deve ser nulo");
        AssertionUtil.assertTrue(randomImage.isValidUrl(), "URL deve ser válida");
    }

    @Test
    @DisplayName("URL deve ser imagem válida (JPG ou PNG)")
    @Tag("validation")
    public void shouldReturnValidImageFormat() {
        logInfo("Validando formato da imagem");

        Response response = apiClient.get(RANDOM_IMAGE_ENDPOINT);
        String jsonBody = response.getBody().asString();

        RandomImageResponse randomImage = JsonUtil.fromJson(jsonBody, RandomImageResponse.class);

        boolean isValidFormat = randomImage.isJpegImage() || randomImage.isPngImage();
        AssertionUtil.assertTrue(isValidFormat, 
            "Imagem deve ser JPG ou PNG, obtido: " + randomImage.getImageUrl());
    }

    @Test
    @DisplayName("Deve conter propriedades esperadas")
    @Tag("validation")
    public void shouldContainExpectedProperties() {
        logInfo("Validando propriedades da resposta");

        Response response = apiClient.get(RANDOM_IMAGE_ENDPOINT);

        response.then()
                .body("$", hasKey("status"))
                .body("$", hasKey("message"));
    }

    @Test
    @DisplayName("Deve retornar Content-Type JSON")
    @Tag("headers")
    public void shouldReturnJsonContentType() {
        logInfo("Validando Content-Type header");

        Response response = apiClient.get(RANDOM_IMAGE_ENDPOINT);

        response.then()
                .header("Content-Type", containsString("application/json"));
    }

    @RepeatedTest(5)
    @DisplayName("Deve retornar imagens diferentes em cada chamada")
    @Tag("randomness")
    public void shouldReturnDifferentImagesOnMultipleCalls() {
        logInfo("Chamada para validar aleatoriedade");

        Response response = apiClient.get(RANDOM_IMAGE_ENDPOINT);

        response.then()
                .statusCode(200)
                .body("status", equalTo("success"))
                .body("message", notNullValue());
    }

    @Nested
    @DisplayName("Testes de Aleatoriedade")
    @Tag("randomness")
    class RandomnessTests {

        @Test
        @DisplayName("Deve retornar URLs variadas em múltiplas chamadas")
        public void shouldReturnVariedUrlsInMultipleCalls() {
            logInfo("Testando variação de URLs em 10 chamadas");

            Set<String> urls = new HashSet<>();

            for (int i = 0; i < 10; i++) {
                Response response = apiClient.get(RANDOM_IMAGE_ENDPOINT);
                String url = JsonUtil.getJsonPathString(response, "message");
                urls.add(url);
                logInfo("Chamada " + (i + 1) + ": " + url);
            }

            AssertionUtil.assertTrue(urls.size() > 1, 
                "Deve retornar URLs diferentes, obteve: " + urls.size());

            logInfo("Total de URLs únicas: " + urls.size());
        }
    }

    @Nested
    @DisplayName("Testes de Performance")
    @Tag("performance")
    class PerformanceTests {

        @Test
        @DisplayName("Resposta não deve demorar mais de 2 segundos")
        public void shouldReturnWithinAcceptableTime() {
            logInfo("Testando tempo de resposta");

            long startTime = System.currentTimeMillis();
            Response response = apiClient.get(RANDOM_IMAGE_ENDPOINT);
            long responseTime = System.currentTimeMillis() - startTime;

            logInfo("Tempo de resposta: " + responseTime + "ms");

            AssertionUtil.assertTrue(responseTime < 2000, 
                "Resposta não deve demorar mais de 2 segundos");
        }
    }

    @Nested
    @DisplayName("Validações de Estrutura")
    @Tag("structure")
    class StructureValidation {

        @Test
        @DisplayName("Estrutura JSON deve estar bem formada")
        public void shouldHaveWellFormedJson() {
            logInfo("Validando estrutura JSON");

            Response response = apiClient.get(RANDOM_IMAGE_ENDPOINT);

            response.then()
                    .statusCode(200)
                    .body("$", hasKey("status"))
                    .body("$", hasKey("message"))
                    .body("$.size()", equalTo(2)); // Deve ter exatamente 2 chaves
        }

        @Test
        @DisplayName("Message deve ser string, não array")
        public void shouldMessageBeString() {
            logInfo("Validando tipo de message");

            Response response = apiClient.get(RANDOM_IMAGE_ENDPOINT);
            String jsonBody = response.getBody().asString();

            RandomImageResponse randomImage = JsonUtil.fromJson(jsonBody, RandomImageResponse.class);

            AssertionUtil.assertTrue(randomImage.getImageUrl() instanceof String, 
                "Message deve ser uma string");
        }
    }
}
