# Guia Rápido de Uso

## 1. Setup Inicial

```bash
# Compilar projeto
mvn clean compile

# Executar testes
mvn clean test
```

## 2. Estrutura de um Teste Básico

```java
@DisplayName("Testes da API")
@Tag("api")
public class MyApiTest extends BaseApiTest {

    @Test
    @DisplayName("Teste descritivo")
    @Tag("smoke")
    public void testName() {
        // Arrange
        String endpoint = "/api/v1/resource";
        
        // Act
        Response response = apiClient.get(endpoint);
        
        // Assert
        AssertionUtil.assertStatusCode(response.getStatusCode(), 200, "Deve retornar 200");
    }
}
```

## 3. Exemplos Práticos

### GET Request
```java
Response response = apiClient.get("/users");
```

### POST com Body
```java
User user = User.builder()
    .name("João")
    .email("joao@example.com")
    .build();

Response response = apiClient.post("/users", user);
```

### Validações
```java
response.then()
    .statusCode(200)
    .body("id", notNullValue())
    .body("name", equalTo("João"));
```

### Autenticação
```java
RequestSpecification spec = ApiSpecBuilder.builder()
    .withBearerToken("seu-token")
    .build();

ApiClient client = new ApiClient(spec);
```

### Fixtures
```java
User user = FixtureUtil.loadFixture("user_request.json", User.class);
```

### Dados Aleatórios
```java
String email = TestDataUtil.generateRandomEmail();
String uuid = TestDataUtil.generateUUID();
```

### Esperas
```java
WaitUtil.wait2Seconds();
```

## 4. Executar Testes Específicos

```bash
# Por tag
mvn clean test -Dgroups=smoke

# Classe específica
mvn clean test -Dtest=MyApiTest

# Método específico
mvn clean test -Dtest=MyApiTest#testName
```

## 5. Relatórios

```bash
# Gerar relatório Allure
mvn allure:report

# Abrir relatório no navegador
mvn allure:serve
```

---

Pronto para começar seus testes! 🚀
