package com.agibank.api.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * Utilitários gerais para testes
 */
public class TestDataUtil {

    private static final Logger logger = LogManager.getLogger(TestDataUtil.class);
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Gera UUID aleatório
     */
    public static String generateUUID() {
        String uuid = UUID.randomUUID().toString();
        logger.info("UUID gerado: {}", uuid);
        return uuid;
    }

    /**
     * Gera email aleatório
     */
    public static String generateRandomEmail() {
        String email = "test_" + System.currentTimeMillis() + "@example.com";
        logger.info("Email gerado: {}", email);
        return email;
    }

    /**
     * Gera nome aleatório
     */
    public static String generateRandomName() {
        String name = "User_" + System.currentTimeMillis();
        logger.info("Nome gerado: {}", name);
        return name;
    }

    /**
     * Gera CPF aleatório (formato: XXX.XXX.XXX-XX)
     */
    public static String generateRandomCPF() {
        String cpf = String.format("%03d.%03d.%03d-%02d",
                (int) (Math.random() * 1000),
                (int) (Math.random() * 1000),
                (int) (Math.random() * 1000),
                (int) (Math.random() * 100));
        logger.info("CPF gerado: {}", cpf);
        return cpf;
    }

    /**
     * Retorna timestamp atual
     */
    public static String getCurrentTimestamp() {
        String timestamp = LocalDateTime.now().format(formatter);
        logger.info("Timestamp atual: {}", timestamp);
        return timestamp;
    }

    /**
     * Retorna timestamp atual em milissegundos
     */
    public static long getCurrentTimestampMillis() {
        long timestamp = System.currentTimeMillis();
        logger.info("Timestamp em ms: {}", timestamp);
        return timestamp;
    }

    /**
     * Gera número aleatório entre min e max
     */
    public static int generateRandomNumber(int min, int max) {
        int number = min + (int) (Math.random() * (max - min + 1));
        logger.info("Número aleatório gerado: {}", number);
        return number;
    }

    /**
     * Gera string aleatória com comprimento específico
     */
    public static String generateRandomString(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < length; i++) {
            result.append(characters.charAt((int) (Math.random() * characters.length())));
        }
        logger.info("String aleatória gerada: {}", result);
        return result.toString();
    }
}
