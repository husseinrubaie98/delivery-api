package com.husseinrubaie.delivery.common.web;

import com.husseinrubaie.delivery.common.exception.BusinessRuleException;
import com.husseinrubaie.delivery.common.exception.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

/**
 * Central exception handling for the server-rendered UI. Translates domain
 * exceptions into user-facing error pages with an appropriate HTTP status.
 *
 * <p>REST/JSON endpoints (added in later phases) will get a dedicated
 * {@code @RestControllerAdvice}; this advice is intentionally view-oriented.</p>
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    private static final String ERROR_VIEW = "error/error";

    @ExceptionHandler(ResourceNotFoundException.class)
    public ModelAndView handleNotFound(ResourceNotFoundException ex, HttpServletRequest request) {
        log.debug("Resource not found at {}: {}", request.getRequestURI(), ex.getMessage());
        return errorView(HttpStatus.NOT_FOUND, ex.getMessage(), request);
    }

    @ExceptionHandler(BusinessRuleException.class)
    public ModelAndView handleBusinessRule(BusinessRuleException ex, HttpServletRequest request) {
        log.debug("Business rule violation at {}: {}", request.getRequestURI(), ex.getMessage());
        return errorView(HttpStatus.CONFLICT, ex.getMessage(), request);
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView handleUnexpected(Exception ex, HttpServletRequest request) {
        log.error("Unexpected error at {}", request.getRequestURI(), ex);
        return errorView(HttpStatus.INTERNAL_SERVER_ERROR,
                "Something went wrong on our side. Please try again.", request);
    }

    private ModelAndView errorView(HttpStatus status, String message, HttpServletRequest request) {
        ModelAndView mav = new ModelAndView(ERROR_VIEW);
        mav.setStatus(status);
        mav.addObject("status", status.value());
        mav.addObject("reason", status.getReasonPhrase());
        mav.addObject("message", message);
        mav.addObject("path", request.getRequestURI());
        return mav;
    }
}
