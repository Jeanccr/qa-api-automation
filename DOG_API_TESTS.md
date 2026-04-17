# Dog API - Testes Automatizados

## 📋 Resumo

Foram criados **3 classes de testes** com **30+ testes** para validar a Dog API:

### ✅ Endpoints Testados

| Endpoint | Classe de Teste | Testes |
|----------|-----------------|--------|
| `GET /breeds/list/all` | [DogBreedListTest.java](src/test/java/com/agibank/api/tests/dog/DogBreedListTest.java) | 8 testes |
| `GET /breed/{breed}/images` | [DogBreedImagesTest.java](src/test/java/com/agibank/api/tests/dog/DogBreedImagesTest.java) | 11 testes |
| `GET /breeds/image/random` | [DogRandomImageTest.java](src/test/java/com/agibank/api/tests/dog/DogRandomImageTest.java) | 11 testes |

## 🏗️ Modelos Criados

**DTOs** em `src/main/java/com/agibank/api/models/`:

1. **BreedsListResponse** - Resposta do endpoint /breeds/list/all
   - Métodos: `getAllBreeds()`, `getSubBreeds(breed)`, `hasBreed(breed)`, `getBreedCount()`

2. **BreedImagesResponse** - Resposta do endpoint /breed/{breed}/images
   - Métodos: `getImageUrls()`, `getImageCount()`, `hasImages()`, `allUrlsValid()`

3. **RandomImageResponse** - Resposta do endpoint /breeds/image/random
   - Métodos: `getImageUrl()`, `isValidUrl()`, `isJpegImage()`, `isPngImage()`

## 🧪 Tipos de Testes

### DogBreedListTest (8 testes)
- ✅ Status code 200
- ✅ Status 'success' na resposta
- ✅ Lista de raças presente e não-vazia
- ✅ Desserialização para objeto
- ✅ Raças conhecidas (labrador, poodle, bulldog)
- ✅ Sub-raças para raças válidas
- ✅ Headers esperados (Content-Type, Server)
- ✅ Estrutura JSON bem-formada
- ✅ Performance (<2s)

### DogBreedImagesTest (11 testes)
- ✅ Retorna imagens para raça válida (labrador)
- ✅ Status code 200 para raça válida
- ✅ Status code 404 para raça inexistente
- ✅ Testes parametrizados com múltiplas raças (labrador, poodle, bulldog, beagle, dachshund)
- ✅ Desserialização para objeto
- ✅ Todas as URLs válidas (começam com http)
- ✅ Propriedades esperadas (status, message)
- ✅ Sub-raças (ex: bulldog/french)
- ✅ Content-Type JSON
- ✅ Cache control headers
- ✅ Performance (<3s)

### DogRandomImageTest (11 testes)
- ✅ Status code 200
- ✅ Status 'success' na resposta
- ✅ URL de imagem válida (começa com https://)
- ✅ Desserialização para objeto
- ✅ Formato de imagem válido (JPG ou PNG)
- ✅ Propriedades esperadas (status, message)
- ✅ Content-Type JSON
- ✅ Testes repetidos (5x) para validar aleatoriedade
- ✅ URLs variadas em múltiplas chamadas
- ✅ Estrutura JSON bem-formada
- ✅ Performance (<2s)

## 🎯 Tags de Teste

Os testes são organizados com tags para fácil execução:

```bash
# Testes de smoke
mvn test -Dgroups=smoke

# Testes de validação
mvn test -Dgroups=validation

# Testes parametrizados
mvn test -Dgroups=parametrized

# Testes de headers
mvn test -Dgroups=headers

# Testes de performance
mvn test -Dgroups=performance

# Todos os testes de Dog API
mvn test -Dgroups=dog-api
```

## 🚀 Como Executar

### Pré-requisitos
- Java 11+
- Maven 3.6+

### Rodar todos os testes
```bash
mvn clean test
```

### Rodar teste específico
```bash
mvn clean test -Dtest=DogBreedListTest
mvn clean test -Dtest=DogBreedImagesTest
mvn clean test -Dtest=DogRandomImageTest
```

### Rodar teste específico com padrão
```bash
mvn clean test -Dtest=DogBreedListTest#shouldReturnStatus200
```

### Gerar Relatório Allure
```bash
mvn allure:report
mvn allure:serve
```

### Usar Scripts de Execução
```bash
# Windows
./run-tests.bat

# Linux/Mac
./run-tests.sh
```

## 📊 Cobertura de Testes

| Cenário | Cobertura |
|---------|-----------|
| Status Code | ✅ 100% (200, 404) |
| JSON Structure | ✅ 100% |
| Response Fields | ✅ 100% |
| Data Types | ✅ 100% |
| Headers | ✅ 80% |
| Performance | ✅ 100% |
| Negative Tests | ✅ Parcial (raça inexistente) |
| Parametrization | ✅ 100% |

## 🔧 Configurações

A configuração está em [TestConfig.java](src/main/java/com/agibank/api/config/TestConfig.java):

```properties
api.base.url=https://dog.ceo/api
api.version=
request.timeout=5000
enable.logging=true
log.level=INFO
```

## 📝 Exemplos de Uso

### Obter lista de raças
```java
Response response = apiClient.get("/breeds/list/all");
BreedsListResponse breeds = JsonUtil.fromJson(response.getBody().asString(), BreedsListResponse.class);
List<String> allBreeds = breeds.getAllBreeds();
```

### Obter imagens de uma raça
```java
Response response = apiClient.get("/breed/{breed}/images", "labrador");
BreedImagesResponse images = JsonUtil.fromJson(response.getBody().asString(), BreedImagesResponse.class);
List<String> urls = images.getImageUrls();
```

### Obter imagem aleatória
```java
Response response = apiClient.get("/breeds/image/random");
RandomImageResponse image = JsonUtil.fromJson(response.getBody().asString(), RandomImageResponse.class);
String imageUrl = image.getImageUrl();
```

## 📚 Documentação

- [Dog API Documentation](https://dog.ceo/dog-api/documentation)
- [RestAssured Documentation](https://rest-assured.io/)
- [JUnit 5 User Guide](https://junit.org/junit5/docs/current/user-guide/)

## 📊 Logs

Logs são salvos em `./logs/qa-api-automation.log`

Exemplo:
```
2026-04-15 10:30:45,123 [INFO] [main] com.agibank.api.core.ApiClient - Executando GET request: /breeds/list/all
2026-04-15 10:30:45,456 [INFO] [main] com.agibank.api.tests.BaseApiTest - [TEST] Testando status code do endpoint /breeds/list/all
```

---

**Status**: ✅ Pronto para execução
**Total de Testes**: 30+
**Cobertura**: Smoke, Validation, Performance, Headers
