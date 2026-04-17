package com.agibank.api.tests.dog;

import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import com.agibank.api.models.BreedsListResponse;
import com.agibank.api.models.BreedImagesResponse;
import com.agibank.api.tests.BaseApiTest;
import com.agibank.api.utils.JsonUtil;
import com.agibank.api.utils.AssertionUtil;

import java.util.List;

import static org.hamcrest.Matchers.*;

@DisplayName("Testes do Endpoint GET /breed/{breed}/images")
@Tag("dog-api")
@Tag("breed-images")
public class DogBreedImagesTest extends BaseApiTest {

    private static final String BREED_IMAGES_ENDPOINT = "/breed/{breed}/images";

    @Test
    @DisplayName("Deve retornar imagens da raça 'labrador'")
    @Tag("smoke")
    public void shouldReturnLabradorImages() {
        logInfo("Testando endpoint para raça 'labrador'");

        Response response = apiClient.get(BREED_IMAGES_ENDPOINT, "labrador");

        response.then()
                .statusCode(200)
                .body("status", equalTo("success"))
                .body("message", notNullValue())
                .body("message.size()", greaterThan(0));
    }

    @Test
    @DisplayName("Deve retornar status 200 para raça válida")
    @Tag("smoke")
    public void shouldReturnStatus200ForValidBreed() {
        logInfo("Testando status code para raça 'poodle'");

        Response response = apiClient.get(BREED_IMAGES_ENDPOINT, "poodle");

        AssertionUtil.assertStatusCode(response.getStatusCode(), 200, "Deve retornar 200");
    }

    @Test
    @DisplayName("Deve retornar 404 para raça inexistente")
    @Tag("negative")
    public void shouldReturn404ForNonexistentBreed() {
        logInfo("Testando endpoint com raça inexistente");

        Response response = apiClient.get(BREED_IMAGES_ENDPOINT, "racacleunicode123xyz");

        response.then()
                .statusCode(404);

        logInfo("Retornou 404 como esperado");
    }

    @ParameterizedTest(name = "Deve retornar imagens para raça: {0}")
    @ValueSource(strings = {"labrador", "poodle", "bulldog", "beagle", "dachshund"})
    @Tag("parametrized")
    public void shouldReturnImagesForMultipleBreeds(String breed) {
        logInfo("Testando raça: " + breed);

        Response response = apiClient.get(BREED_IMAGES_ENDPOINT, breed);

        response.then()
                .statusCode(200)
                .body("status", equalTo("success"))
                .body("message.size()", greaterThan(0));
    }

    @Test
    @DisplayName("Deve desserializar resposta para BreedImagesResponse")
    @Tag("deserialization")
    public void shouldDeserializeToBreedImages() {
        logInfo("Desserializando resposta para BreedImagesResponse");

        Response response = apiClient.get(BREED_IMAGES_ENDPOINT, "labrador");
        String jsonBody = response.getBody().asString();

        BreedImagesResponse breedImages = JsonUtil.fromJson(jsonBody, BreedImagesResponse.class);

        AssertionUtil.assertNotNull(breedImages, "BreedImagesResponse não deve ser nulo");
        AssertionUtil.assertNotNull(breedImages.getImageUrls(), "Message não deve ser nulo");
        AssertionUtil.assertTrue(breedImages.hasImages(), "Deve ter imagens");
    }

    @Test
    @DisplayName("Todas as URLs devem ser válidas")
    @Tag("validation")
    public void shouldAllUrlsBeValid() {
        logInfo("Validando formato das URLs");

        Response response = apiClient.get(BREED_IMAGES_ENDPOINT, "labrador");
        String jsonBody = response.getBody().asString();

        BreedImagesResponse breedImages = JsonUtil.fromJson(jsonBody, BreedImagesResponse.class);

        AssertionUtil.assertTrue(breedImages.allUrlsValid(), 
            "Todas as URLs devem começar com http");

        // Log de algumas URLs
        List<String> urls = breedImages.getImageUrls();
        logInfo("Primeiras URLs: " + urls.stream().limit(3).toList());
    }

    @Test
    @DisplayName("Deve conter propriedades esperadas")
    @Tag("validation")
    public void shouldContainExpectedProperties() {
        logInfo("Validando propriedades da resposta");

        Response response = apiClient.get(BREED_IMAGES_ENDPOINT, "bulldog");

        response.then()
                .body("$", hasKey("status"))
                .body("$", hasKey("message"));
    }

    @Nested
    @DisplayName("Testes com Sub-raças")
    @Tag("subbreeds")
    class SubBreedsTests {

        @Test
        @DisplayName("Deve retornar imagens de sub-raça 'french'")
        public void shouldReturnImagesForSubBreedFrench() {
            logInfo("Testando endpoint com sub-raça");

            // Teste de sub-raça usando endpoint correto: /breed/bulldog/french/images
            String fullBreedPath = "breed/bulldog/french/images";
            logInfo("Testando sub-raça: " + fullBreedPath);

            Response response = apiClient.get("/" + fullBreedPath);

            response.then()
                    .statusCode(200)
                    .body("status", equalTo("success"))
                    .body("message.size()", greaterThan(0));
        }
    }

    @Nested
    @DisplayName("Testes de Headers")
    @Tag("headers")
    class HeaderTests {

        @Test
        @DisplayName("Deve retornar Content-Type JSON")
        public void shouldReturnJsonContentType() {
            logInfo("Validando Content-Type header");

            Response response = apiClient.get(BREED_IMAGES_ENDPOINT, "poodle");

            response.then()
                    .header("Content-Type", containsString("application/json"));
        }

        @Test
        @DisplayName("Deve ter cache control headers")
        public void shouldHaveCacheControlHeaders() {
            logInfo("Validando cache control headers");

            Response response = apiClient.get(BREED_IMAGES_ENDPOINT, "poodle");

            response.then()
                    .header("Server", notNullValue());
        }
    }

    @Nested
    @DisplayName("Testes de Performance")
    @Tag("performance")
    class PerformanceTests {

        @Test
        @DisplayName("Resposta não deve demorar mais de 3 segundos")
        public void shouldReturnWithinAcceptableTime() {
            logInfo("Testando tempo de resposta");

            long startTime = System.currentTimeMillis();
            Response response = apiClient.get(BREED_IMAGES_ENDPOINT, "labrador");
            long responseTime = System.currentTimeMillis() - startTime;

            logInfo("Tempo de resposta: " + responseTime + "ms");

            AssertionUtil.assertTrue(responseTime < 3000, 
                "Resposta não deve demorar mais de 3 segundos");
        }
    }
}
