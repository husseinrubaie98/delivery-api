package com.husseinrubaie.delivery.common.exception;

/**
 * Thrown when an operation violates a domain/business rule (e.g. an illegal
 * order state transition). Surfaces as HTTP 409/400 depending on context.
 */
public class BusinessRuleException extends RuntimeException {

    public BusinessRuleException(String message) {
        super(message);
    }
}
