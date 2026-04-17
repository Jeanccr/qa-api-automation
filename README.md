# QA API Automation 

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

## 📚 Recursos

- [RestAssured Documentation](https://rest-assured.io/)
- [JUnit 5 Guide](https://junit.org/junit5/docs/current/user-guide/)
