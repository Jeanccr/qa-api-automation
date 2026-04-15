package com.agibank.api.utils;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Utilitários para esperas e retry de validações
 */
public class WaitUtil {

    private static final Logger logger = LogManager.getLogger(WaitUtil.class);
    private static final int DEFAULT_TIMEOUT_SECONDS = 10;
    private static final int DEFAULT_POLL_INTERVAL_MILLIS = 500;

    /**
     * Aguarda com timeout especificado
     */
    public static void waitFor(long duration, TimeUnit timeUnit) {
        try {
            logger.info("Aguardando {} {}", duration, timeUnit);
            Thread.sleep(timeUnit.toMillis(duration));
        } catch (InterruptedException e) {
            logger.error("Interrupção durante wait", e);
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Aguarda 1 segundo
     */
    public static void wait1Second() {
        waitFor(1, TimeUnit.SECONDS);
    }

    /**
     * Aguarda 2 segundos
     */
    public static void wait2Seconds() {
        waitFor(2, TimeUnit.SECONDS);
    }

    /**
     * Aguarda 3 segundos
     */
    public static void wait3Seconds() {
        waitFor(3, TimeUnit.SECONDS);
    }

    /**
     * Retry com condição - executa função até condição ser verdadeira ou timeout
     */
    public static <T> T retryUntil(Supplier<T> supplier, java.util.function.Predicate<T> condition, 
                                    long timeoutSeconds, long pollIntervalMillis) {
        long startTime = System.currentTimeMillis();
        long timeoutMillis = TimeUnit.SECONDS.toMillis(timeoutSeconds);

        while (true) {
            try {
                T result = supplier.get();
                if (condition.test(result)) {
                    logger.info("Condição atendida");
                    return result;
                }
            } catch (Exception e) {
                logger.debug("Erro durante retry: {}", e.getMessage());
            }

            long elapsed = System.currentTimeMillis() - startTime;
            if (elapsed > timeoutMillis) {
                logger.error("Timeout excedido. Timeout: {}ms, Decorrido: {}ms", timeoutMillis, elapsed);
                throw new RuntimeException("Timeout aguardando condição");
            }

            try {
                Thread.sleep(pollIntervalMillis);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Interrompido durante retry", e);
            }
        }
    }

    /**
     * Retry com valores padrão
     */
    public static <T> T retryUntil(Supplier<T> supplier, java.util.function.Predicate<T> condition) {
        return retryUntil(supplier, condition, DEFAULT_TIMEOUT_SECONDS, DEFAULT_POLL_INTERVAL_MILLIS);
    }
}
