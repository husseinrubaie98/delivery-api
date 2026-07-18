package com.husseinrubaie.delivery.common.exception;

/**
 * Thrown when a requested domain resource does not exist (or is not visible to
 * the current tenant). Surfaces as HTTP 404.
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public static ResourceNotFoundException of(String resource, Object id) {
        return new ResourceNotFoundException("%s not found: %s".formatted(resource, id));
    }
}
