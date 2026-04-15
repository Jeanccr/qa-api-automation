package com.agibank.api.config;

import lombok.Getter;

/**
 * Classe de configuração centralizada para toda a suíte de testes
 */
@Getter
public class TestConfig {
    
    private static final String BASE_URL = getPropertyOrDefault("api.base.url", "https://dog.ceo/api");
    private static final String API_VERSION = getPropertyOrDefault("api.version", "");
    private static final Integer REQUEST_TIMEOUT = Integer.parseInt(getPropertyOrDefault("request.timeout", "5000"));
    private static final Boolean ENABLE_LOGGING = Boolean.parseBoolean(getPropertyOrDefault("enable.logging", "true"));
    private static final String LOG_LEVEL = getPropertyOrDefault("log.level", "INFO");

    public static String getBaseUrl() {
        return BASE_URL;
    }

    public static String getApiVersion() {
        return API_VERSION;
    }

    public static Integer getRequestTimeout() {
        return REQUEST_TIMEOUT;
    }

    public static Boolean isLoggingEnabled() {
        return ENABLE_LOGGING;
    }

    public static String getLogLevel() {
        return LOG_LEVEL;
    }

    /**
     * Obtém propriedade do sistema ou arquivo de configuração, com valor padrão
     */
    private static String getPropertyOrDefault(String key, String defaultValue) {
        String value = System.getProperty(key);
        if (value != null && !value.isEmpty()) {
            return value;
        }
        return defaultValue;
    }
}
