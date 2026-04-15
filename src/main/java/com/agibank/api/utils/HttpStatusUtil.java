package com.agibank.api.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Utilitários para validações de HTTP Status Codes
 */
public class HttpStatusUtil {

    private static final Logger logger = LogManager.getLogger(HttpStatusUtil.class);

    // 2xx Success
    public static final int OK = 200;
    public static final int CREATED = 201;
    public static final int ACCEPTED = 202;
    public static final int NO_CONTENT = 204;

    // 3xx Redirection
    public static final int MOVED_PERMANENTLY = 301;
    public static final int FOUND = 302;
    public static final int NOT_MODIFIED = 304;
    public static final int TEMPORARY_REDIRECT = 307;
    public static final int PERMANENT_REDIRECT = 308;

    // 4xx Client Error
    public static final int BAD_REQUEST = 400;
    public static final int UNAUTHORIZED = 401;
    public static final int FORBIDDEN = 403;
    public static final int NOT_FOUND = 404;
    public static final int METHOD_NOT_ALLOWED = 405;
    public static final int CONFLICT = 409;
    public static final int UNPROCESSABLE_ENTITY = 422;
    public static final int TOO_MANY_REQUESTS = 429;

    // 5xx Server Error
    public static final int INTERNAL_SERVER_ERROR = 500;
    public static final int NOT_IMPLEMENTED = 501;
    public static final int BAD_GATEWAY = 502;
    public static final int SERVICE_UNAVAILABLE = 503;
    public static final int GATEWAY_TIMEOUT = 504;

    /**
     * Obtém descrição do status code
     */
    public static String getStatusDescription(int statusCode) {
        String description = switch (statusCode) {
            // 2xx
            case 200 -> "OK";
            case 201 -> "Created";
            case 202 -> "Accepted";
            case 204 -> "No Content";
            // 3xx
            case 301 -> "Moved Permanently";
            case 302 -> "Found";
            case 304 -> "Not Modified";
            case 307 -> "Temporary Redirect";
            case 308 -> "Permanent Redirect";
            // 4xx
            case 400 -> "Bad Request";
            case 401 -> "Unauthorized";
            case 403 -> "Forbidden";
            case 404 -> "Not Found";
            case 405 -> "Method Not Allowed";
            case 409 -> "Conflict";
            case 422 -> "Unprocessable Entity";
            case 429 -> "Too Many Requests";
            // 5xx
            case 500 -> "Internal Server Error";
            case 501 -> "Not Implemented";
            case 502 -> "Bad Gateway";
            case 503 -> "Service Unavailable";
            case 504 -> "Gateway Timeout";
            default -> "Unknown Status Code";
        };
        return description;
    }

    /**
     * Valida se status code é de sucesso (2xx)
     */
    public static boolean isSuccess(int statusCode) {
        return statusCode >= 200 && statusCode < 300;
    }

    /**
     * Valida se status code é de redirecionamento (3xx)
     */
    public static boolean isRedirect(int statusCode) {
        return statusCode >= 300 && statusCode < 400;
    }

    /**
     * Valida se status code é de erro do cliente (4xx)
     */
    public static boolean isClientError(int statusCode) {
        return statusCode >= 400 && statusCode < 500;
    }

    /**
     * Valida se status code é de erro do servidor (5xx)
     */
    public static boolean isServerError(int statusCode) {
        return statusCode >= 500 && statusCode < 600;
    }

    /**
     * Log detalhado do status code
     */
    public static void logStatus(int statusCode) {
        String type = isSuccess(statusCode) ? "SUCCESS" :
                      isRedirect(statusCode) ? "REDIRECT" :
                      isClientError(statusCode) ? "CLIENT_ERROR" :
                      isServerError(statusCode) ? "SERVER_ERROR" : "UNKNOWN";
        logger.info("[{}] Status Code: {} - {}", type, statusCode, getStatusDescription(statusCode));
    }
}
