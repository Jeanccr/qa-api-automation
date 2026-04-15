package com.agibank.api.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Utilitários para carregar fixtures (dados de teste) de arquivos
 */
public class FixtureUtil {

    private static final Logger logger = LogManager.getLogger(FixtureUtil.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final String FIXTURES_PATH = "src/test/resources/fixtures/";

    /**
     * Carrega fixture JSON como string
     */
    public static String loadFixtureAsString(String filename) {
        try {
            String path = FIXTURES_PATH + filename;
            String content = new String(Files.readAllBytes(Paths.get(path)));
            logger.info("Fixture carregada: {}", filename);
            return content;
        } catch (IOException e) {
            logger.error("Erro ao carregar fixture: {}", filename, e);
            throw new RuntimeException("Fixture não encontrada: " + filename, e);
        }
    }

    /**
     * Carrega fixture JSON e converte para objeto específico
     */
    public static <T> T loadFixture(String filename, Class<T> clazz) {
        try {
            String json = loadFixtureAsString(filename);
            return objectMapper.readValue(json, clazz);
        } catch (IOException e) {
            logger.error("Erro ao desserializar fixture: {}", filename, e);
            throw new RuntimeException("Erro ao processar fixture: " + filename, e);
        }
    }

    /**
     * Carrega fixture e retorna como Map
     */
    public static java.util.Map<String, Object> loadFixtureAsMap(String filename) {
        try {
            String json = loadFixtureAsString(filename);
            return objectMapper.readValue(json, java.util.Map.class);
        } catch (IOException e) {
            logger.error("Erro ao carregar fixture como map: {}", filename, e);
            throw new RuntimeException("Erro ao processar fixture: " + filename, e);
        }
    }

    /**
     * Carrega fixture e retorna como List
     */
    public static <T> java.util.List<T> loadFixtureAsList(String filename, Class<T> clazz) {
        try {
            String json = loadFixtureAsString(filename);
            return objectMapper.readValue(json, 
                objectMapper.getTypeFactory().constructCollectionType(java.util.List.class, clazz));
        } catch (IOException e) {
            logger.error("Erro ao carregar fixture como lista: {}", filename, e);
            throw new RuntimeException("Erro ao processar fixture: " + filename, e);
        }
    }
}
