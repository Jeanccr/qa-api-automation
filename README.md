# QA API Automation - Agibank

Projeto profissional de automação de testes de API usando **Java** e **RestAssured**.

## 🏗️ Estrutura do Projeto

```
qa-api-automation/
├── src/
│   ├── main/java/com/agibank/api/
│   │   ├── config/              # Configurações globais
│   │   ├── core/                # Classes base (ApiClient, ApiSpecBuilder)
│   │   ├── models/              # DTOs e modelos de dados
│   │   ├── specs/               # Especificações pré-construídas
│   │   └── utils/               # Utilitários (JSON, Assertions, etc)
│   └── test/
│       ├── java/com/agibank/api/
│       │   └── tests/           # Classes de teste
│       └── resources/
│           ├── log4j2.xml       # Configuração de logging
│           └── fixtures/        # Dados de teste
├── pom.xml                       # Dependências Maven
└── README.md
```

## 📦 Dependências Principais

- **RestAssured** 5.3.2 - HTTP Client para testes de API
- **JUnit 5** 5.9.2 - Framework de testes
- **Jackson** 2.15.2 - Serialização/Desserialização JSON
- **Lombok** 1.18.28 - Redução de boilerplate
- **Log4j 2** 2.20.0 - Logging
- **AssertJ** 3.24.1 - Assertions fluentes
- **Hamcrest** 2.2 - Matchers

## 🚀 Como Usar

### Pré-requisitos

- Java 11+
- Maven 3.6+

### 1. Clonar o Repositório

```bash
git clone <repository-url>
cd qa-api-automation
```

### 2. Compilar o Projeto

```bash
mvn clean compile
```

### 3. Executar Testes

#### Todos os testes
```bash
mvn clean test
```

#### Testes específicos (por tag)
```bash
mvn clean test -Dgroups=smoke
mvn clean test -Dgroups=post
```

#### Testes de uma classe
```bash
mvn clean test -Dtest=ExampleApiTest
```

#### Testes com padrão no nome
```bash
mvn clean test -Dtest=*ApiTest
```

### 4. Relatórios de Testes

Os testes geram relatórios automáticamente em `target/surefire-reports/`

#### Usar script automatizado (recomendado)
```bash
# Windows
.\run-tests.bat

# Linux/Mac
./run-tests.sh
```

Este script:
1. Limpa o projeto
2. Compila o código
3. Executa todos os testes
4. **Abre o relatório automaticamente**

#### Execução manual
```bash
mvn clean test
# Abrir manualmente: target/surefire-reports/
```

## 📝 Criando Testes

### Exemplo Básico

```java
@DisplayName("Testes da API de Usuários")
@Tag("users")
public class UserApiTest extends BaseApiTest {

    @Test
    @DisplayName("Deve retornar lista de usuários")
    @Tag("smoke")
    public void shouldGetUserList() {
        Response response = apiClient.get("/api/v1/users");
        
        AssertionUtil.assertStatusCode(response.getStatusCode(), 200, "Deve retornar 200");
        
        response.then()
            .body("users", notNullValue())
            .body("users.size()", greaterThan(0));
    }
}
```

### Fazendo Requisições

#### GET
```java
Response response = apiClient.get("/endpoint");
```

#### POST com Body
```java
UserRequest request = UserRequest.builder()
    .name("João")
    .email("joao@example.com")
    .build();

Response response = apiClient.post("/api/users", request);
```

#### PUT
```java
Response response = apiClient.put("/api/users/1", updateData);
```

#### DELETE
```java
Response response = apiClient.delete("/api/users/1");
```

### Utilizando ApiSpecBuilder para Customizações

```java
RequestSpecification spec = ApiSpecBuilder.builder()
    .withBearerToken("seu-token-aqui")
    .withHeader("X-Custom-Header", "valor")
    .withQueryParam("page", "1")
    .build();

ApiClient client = new ApiClient(spec);
Response response = client.get("/endpoint");
```

### Validações

#### Com AssertionUtil
```java
AssertionUtil.assertStatusCode(response.getStatusCode(), 200, "Status deve ser 200");
AssertionUtil.assertEquals(actual, expected, "Valores devem ser iguais");
AssertionUtil.assertTrue(condition, "Condição deve ser verdadeira");
```

#### Com Hamcrest Matchers
```java
response.then()
    .statusCode(200)
    .body("id", notNullValue())
    .body("name", equalTo("João"))
    .body("email", containsString("@"));
```

#### Com JSONPath
```java
String email = JsonUtil.getJsonPathString(response, "data.email");
AssertionUtil.assertNotNull(email, "Email não deve ser nulo");
```

## 🔐 Autenticação

### Bearer Token
```java
RequestSpecification spec = ApiSpecBuilder.builder()
    .withBearerToken("token-aqui")
    .build();
```

### Basic Auth
```java
RequestSpecification spec = ApiSpecBuilder.builder()
    .withBasicAuth("username", "password")
    .build();
```

### Headers Customizados
```java
RequestSpecification spec = ApiSpecBuilder.builder()
    .withHeader("Authorization", "Bearer token")
    .withHeader("X-API-Key", "sua-chave")
    .build();
```

## ⚙️ Configurações

Edite `TestConfig.java` para configurar:
- Base URL da API
- Timeouts
- Versão da API
- Nível de logging

Ou use variáveis de sistema:
```bash
mvn clean test -Dapi.base.url=http://localhost:8080 -Dapi.version=v2
```

## 📊 Relatórios

### Log4j2
Logs são salvos em `./logs/qa-api-automation.log`

### Maven Surefire
Relatórios XML detalhados em `target/surefire-reports/` com:
- Resultados de todas as execuções
- Tempos de execução
- Stack traces de falhas
- Integração com IDEs e CI/CD

## 🛠️ Boas Práticas

✅ Use `@DisplayName` para descrições claras
✅ Organize testes com `@Tag`
✅ Herde de `BaseApiTest`
✅ Use builders para requisições complexas
✅ Valide status code, headers e body
✅ Mantenha dados de teste em fixtures
✅ Reutilize especificações com `ApiSpecs`
✅ Use logging para rastreabilidade

## 📚 Recursos

- [RestAssured Documentation](https://rest-assured.io/)
- [JUnit 5 Guide](https://junit.org/junit5/docs/current/user-guide/)

## 👥 Contribuição

1. Crie uma branch para sua feature: `git checkout -b feature/nova-feature`
2. Commit suas mudanças: `git commit -m 'Add nova feature'`
3. Push para a branch: `git push origin feature/nova-feature`
4. Abra um Pull Request

## 📄 Licença

Este projeto está sob licença MIT.

---

**Desenvolvido por**: QA Team - Agibank
**Data**: 2026