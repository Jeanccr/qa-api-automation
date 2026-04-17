package com.agibank.api.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Utilitários para manipulação de JSON
 */
public class JsonUtil {

    private static final Logger logger = LogManager.getLogger(JsonUtil.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Converte objeto para JSON string
     */
    public static String toJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (Exception e) {
            logger.error("Erro ao converter objeto para JSON", e);
            throw new RuntimeException("Erro ao serializar JSON", e);
        }
    }

    /**
     * Converte JSON string para objeto de determinada classe
     */
    public static <T> T fromJson(String json, Class<T> clazz) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (Exception e) {
            logger.error("Erro ao converter JSON para objeto", e);
            throw new RuntimeException("Erro ao desserializar JSON", e);
        }
    }

    /**
     * Extrai valor de um campo JSON usando JSONPath
     */
    public static Object getJsonPath(Response response, String path) {
        try {
            return response.jsonPath().get(path);
        } catch (Exception e) {
            logger.error("Erro ao extrair JSONPath: {}", path, e);
            return null;
        }
    }

    /**
     * Extrai valor string usando JSONPath
     */
    public static String getJsonPathString(Response response, String path) {
        Object value = getJsonPath(response, path);
        return value != null ? value.toString() : null;
    }

    /**
     * Verifica se JSON contém um campo
     */
    public static boolean hasJsonPath(Response response, String path) {
        try {
            return getJsonPath(response, path) != null;
        } catch (Exception e) {
            return false;
        }
    }
}
