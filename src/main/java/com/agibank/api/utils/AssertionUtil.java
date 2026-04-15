package com.agibank.api.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Utilitários para assertions customizadas e validações
 */
public class AssertionUtil {

    private static final Logger logger = LogManager.getLogger(AssertionUtil.class);

    /**
     * Valida se o status code é o esperado
     */
    public static void assertStatusCode(int actualStatus, int expectedStatus, String message) {
        if (actualStatus != expectedStatus) {
            String errorMsg = String.format("%s - Esperado: %d, Obtido: %d", message, expectedStatus, actualStatus);
            logger.error(errorMsg);
            throw new AssertionError(errorMsg);
        }
        logger.info("Status code {} validado com sucesso", actualStatus);
    }

    /**
     * Valida se um valor é nulo
     */
    public static void assertNull(Object value, String message) {
        if (value != null) {
            String errorMsg = message + " - Valor não é nulo: " + value;
            logger.error(errorMsg);
            throw new AssertionError(errorMsg);
        }
    }

    /**
     * Valida se um valor não é nulo
     */
    public static void assertNotNull(Object value, String message) {
        if (value == null) {
            String errorMsg = message + " - Valor é nulo";
            logger.error(errorMsg);
            throw new AssertionError(errorMsg);
        }
    }

    /**
     * Valida se dois valores são iguais
     */
    public static void assertEquals(Object actual, Object expected, String message) {
        if (!actual.equals(expected)) {
            String errorMsg = String.format("%s - Esperado: %s, Obtido: %s", message, expected, actual);
            logger.error(errorMsg);
            throw new AssertionError(errorMsg);
        }
        logger.info("Assert '{}' passou com sucesso", message);
    }

    /**
     * Valida se uma string contém um valor
     */
    public static void assertContains(String actual, String expected, String message) {
        if (!actual.contains(expected)) {
            String errorMsg = String.format("%s - '%s' não contém '%s'", message, actual, expected);
            logger.error(errorMsg);
            throw new AssertionError(errorMsg);
        }
    }

    /**
     * Valida se uma condição é verdadeira
     */
    public static void assertTrue(boolean condition, String message) {
        if (!condition) {
            logger.error(message);
            throw new AssertionError(message);
        }
        logger.info("Assert '{}' passou com sucesso", message);
    }

    /**
     * Valida se uma condição é falsa
     */
    public static void assertFalse(boolean condition, String message) {
        if (condition) {
            logger.error(message);
            throw new AssertionError(message);
        }
        logger.info("Assert '{}' passou com sucesso", message);
    }
}
