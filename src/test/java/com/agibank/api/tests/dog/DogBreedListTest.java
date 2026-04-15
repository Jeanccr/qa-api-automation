package com.agibank.api.tests.dog;

import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Nested;
import com.agibank.api.models.BreedsListResponse;
import com.agibank.api.tests.BaseApiTest;
import com.agibank.api.utils.JsonUtil;
import com.agibank.api.utils.AssertionUtil;

import java.util.List;

import static org.hamcrest.Matchers.*;

@DisplayName("Testes do Endpoint GET /breeds/list/all")
@Tag("dog-api")
@Tag("breeds-list")
public class DogBreedListTest extends BaseApiTest {

    private static final String BREEDS_ENDPOINT = "/breeds/list/all";

    @Test
    @DisplayName("Deve retornar status 200")
    @Tag("smoke")
    public void shouldReturnStatus200() {
        logInfo("Testando status code do endpoint " + BREEDS_ENDPOINT);

        Response response = apiClient.get(BREEDS_ENDPOINT);

        AssertionUtil.assertStatusCode(response.getStatusCode(), 200, "Deve retornar 200");
    }

    @Test
    @DisplayName("Deve retornar JSON com status 'success'")
    @Tag("smoke")
    public void shouldReturnSuccessStatus() {
        logInfo("Validando status 'success' na resposta");

        Response response = apiClient.get(BREEDS_ENDPOINT);

        response.then()
                .statusCode(200)
                .body("status", equalTo("success"));
    }

    @Test
    @DisplayName("Deve retornar object 'message' com lista de raças")
    @Tag("smoke")
    public void shouldReturnMessageWithBreeds() {
        logInfo("Validando presença de raças na resposta");

        Response response = apiClient.get(BREEDS_ENDPOINT);

        response.then()
                .statusCode(200)
                .body("message", notNullValue())
                .body("message.size()", greaterThan(0));
    }

    @Test
    @DisplayName("Deve desserializar resposta para BreedsListResponse")
    @Tag("deserialization")
    public void shouldDeserializeToBreedsList() {
        logInfo("Desserializando resposta para BreedsListResponse");

        Response response = apiClient.get(BREEDS_ENDPOINT);
        String jsonBody = response.getBody().asString();

        BreedsListResponse breedsList = JsonUtil.fromJson(jsonBody, BreedsListResponse.class);

        AssertionUtil.assertNotNull(breedsList, "BreedsListResponse não deve ser nulo");
        AssertionUtil.assertNotNull(breedsList.getAllBreeds(), "Breeds não devem ser nulos");
        AssertionUtil.assertTrue(breedsList.getBreedCount() > 0, "Deve conter pelo menos 1 raça");
    }

    @Test
    @DisplayName("Deve conter raças conhecidas")
    @Tag("validation")
    public void shouldContainKnownBreeds() {
        logInfo("Validando presença de raças conhecidas");

        Response response = apiClient.get(BREEDS_ENDPOINT);
        String jsonBody = response.getBody().asString();

        BreedsListResponse breedsList = JsonUtil.fromJson(jsonBody, BreedsListResponse.class);
        List<String> allBreeds = breedsList.getAllBreeds();

        logInfo("Total de raças: " + allBreeds.size());

        AssertionUtil.assertTrue(allBreeds.contains("labrador"), "Deve conter 'labrador'");
        AssertionUtil.assertTrue(allBreeds.contains("poodle"), "Deve conter 'poodle'");
        AssertionUtil.assertTrue(allBreeds.contains("bulldog"), "Deve conter 'bulldog'");
    }

    @Test
    @DisplayName("Deve retornar sub-raças para raças válidas")
    @Tag("validation")
    public void shouldReturnSubBreedsForValidBreeds() {
        logInfo("Validando sub-raças de raças conhecidas");

        Response response = apiClient.get(BREEDS_ENDPOINT);
        String jsonBody = response.getBody().asString();

        BreedsListResponse breedsList = JsonUtil.fromJson(jsonBody, BreedsListResponse.class);

        // Bulldog tem sub-raças
        List<String> bulldogSubBreeds = breedsList.getSubBreeds("bulldog");
        AssertionUtil.assertTrue(bulldogSubBreeds.size() > 0, "Bulldog deve ter sub-raças");

        logInfo("Bulldog sub-raças: " + bulldogSubBreeds);
    }

    @Test
    @DisplayName("Deve conter headers esperados")
    @Tag("headers")
    public void shouldHaveExpectedHeaders() {
        logInfo("Validando headers da resposta");

        Response response = apiClient.get(BREEDS_ENDPOINT);

        response.then()
                .header("Content-Type", containsString("application/json"))
                .header("Server", notNullValue());
    }

    @Nested
    @DisplayName("Validações de Estrutura")
    @Tag("structure")
    class StructureValidation {

        @Test
        @DisplayName("Estrutura JSON deve estar bem formada")
        public void shouldHaveWellFormedJson() {
            logInfo("Validando estrutura JSON");

            Response response = apiClient.get(BREEDS_ENDPOINT);

            response.then()
                    .statusCode(200)
                    .body("$", hasKey("status"))
                    .body("$", hasKey("message"));
        }

        @Test
        @DisplayName("Todas as raças devem ser strings")
        public void shouldAllBreedsBeStrings() {
            logInfo("Validando tipos de dados das raças");

            Response response = apiClient.get(BREEDS_ENDPOINT);

            response.then()
                    .statusCode(200)
                    .body("message.keySet().flatten()", everyItem(instanceOf(String.class)));
        }

        @Test
        @DisplayName("Todas as sub-raças devem ser arrays de strings")
        public void shouldAllSubBreedsBeStringArrays() {
            logInfo("Validando tipos de dados das sub-raças");

            Response response = apiClient.get(BREEDS_ENDPOINT);
            String jsonBody = response.getBody().asString();

            BreedsListResponse breedsList = JsonUtil.fromJson(jsonBody, BreedsListResponse.class);

            breedsList.getAllBreeds().forEach(breed -> {
                List<String> subBreeds = breedsList.getSubBreeds(breed);
                AssertionUtil.assertTrue(subBreeds instanceof List, 
                    "Sub-raças de " + breed + " devem ser lista");
            });
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
            apiClient.get(BREEDS_ENDPOINT);
            long responseTime = System.currentTimeMillis() - startTime;

            logInfo("Tempo de resposta: " + responseTime + "ms");

            AssertionUtil.assertTrue(responseTime < 2000, 
                "Resposta não deve demorar mais de 2 segundos");
        }
    }
}
