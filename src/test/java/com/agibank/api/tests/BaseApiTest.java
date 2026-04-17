package com.agibank.api.tests;

import org.junit.jupiter.api.BeforeEach;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.agibank.api.core.ApiClient;
import com.agibank.api.specs.ApiSpecs;

/**
 * Classe base para todos os testes de API
 * Fornece setup comum e utilitários reutilizáveis
 */
public class BaseApiTest {

    protected static final Logger logger = LogManager.getLogger(BaseApiTest.class);
    protected ApiClient apiClient;

    @BeforeEach
    public void setUp() {
        logger.info("Inicializando teste de API");
        apiClient = new ApiClient(ApiSpecs.defaultSpec());
    }

    /**
     * Log de informação do teste
     */
    protected void logInfo(String message) {
        logger.info("[TEST] {}", message);
    }

    /**
     * Log de warning do teste
     */
    protected void logWarn(String message) {
        logger.warn("[TEST] {}", message);
    }

    /**
     * Log de erro do teste
     */
    protected void logError(String message) {
        logger.error("[TEST] {}", message);
    }
}
