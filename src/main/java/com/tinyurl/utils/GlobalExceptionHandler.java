package com.tinyurl.utils;

import com.tinyurl.service.EntityNotFoundException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.stream.Collectors;

/**
 * Global exception handler for centralized error handling across the TinyURL application.
 *
 * <p>This class provides centralized exception handling using Spring Boot's {@link ControllerAdvice}
 * mechanism, ensuring consistent error responses across all REST API endpoints. It intercepts
 * exceptions thrown by any controller method and converts them into standardized {@link ErrorResponse}
 * objects with appropriate HTTP status codes.</p>
 *
 * <p>The global exception handler follows best practices for REST API error handling by:</p>
 * <ul>
 *   <li>Providing consistent error response structure across all endpoints</li>
 *   <li>Mapping specific exceptions to appropriate HTTP status codes</li>
 *   <li>Converting validation errors into user-friendly messages</li>
 *   <li>Centralizing error handling logic to avoid code duplication</li>
 *   <li>Ensuring proper error logging for debugging and monitoring</li>
 * </ul>
 *
 * <p><strong>Exception Handling Strategy:</strong></p>
 * <ul>
 *   <li>{@link EntityNotFoundException} → HTTP 404 Not Found</li>
 *   <li>{@link IllegalArgumentException} → HTTP 400 Bad Request</li>
 *   <li>{@link MethodArgumentNotValidException} → HTTP 400 Bad Request (Validation Errors)</li>
 * </ul>
 *
 * <p><strong>Benefits:</strong></p>
 * <ul>
 *   <li>Eliminates the need for try-catch blocks in controllers</li>
 *   <li>Provides consistent error responses for client applications</li>
 *   <li>Improves maintainability by centralizing error handling logic</li>
 *   <li>Enables easy addition of new exception types and handlers</li>
 * </ul>
 *
 * @author Jose
 * @since 1.0
 * @see org.springframework.web.bind.annotation.ControllerAdvice
 * @see org.springframework.web.bind.annotation.ExceptionHandler
 * @see ErrorResponse
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles {@link EntityNotFoundException} thrown when requested entities are not found.
     *
     * <p>This handler is triggered when service layer methods throw {@link EntityNotFoundException},
     * typically during URL lookup operations where the requested short code or alias doesn't
     * exist in the system. It returns an HTTP 404 Not Found response with a standardized
     * error structure.</p>
     *
     * <p><strong>Common Scenarios:</strong></p>
     * <ul>
     *   <li>User attempts to access a non-existent shortened URL</li>
     *   <li>API client requests information about a deleted URL</li>
     *   <li>Invalid short codes are provided in API requests</li>
     * </ul>
     *
     * <p><strong>Response Example:</strong></p>
     * <pre>
     * HTTP 404 Not Found
     * {
     *   "code": "NOT_FOUND",
     *   "message": "Short URL 'abc123' not found"
     * }
     * </pre>
     *
     * @param e the {@link EntityNotFoundException} that was thrown
     * @return {@link ResponseEntity} containing {@link ErrorResponse} with HTTP 404 status
     * @see EntityNotFoundException
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFound(EntityNotFoundException e) {
        ErrorResponse error = new ErrorResponse("NOT_FOUND", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    /**
     * Handles {@link IllegalArgumentException} thrown for invalid business logic arguments.
     *
     * <p>This handler manages business rule violations and invalid input arguments that
     * don't pass service layer validation. It typically occurs when users provide
     * arguments that violate business constraints, such as duplicate aliases or
     * invalid parameter combinations.</p>
     *
     * <p><strong>Common Scenarios:</strong></p>
     * <ul>
     *   <li>Attempting to create a URL with an already existing alias</li>
     *   <li>Providing invalid parameters to service methods</li>
     *   <li>Business rule violations in URL shortening operations</li>
     * </ul>
     *
     * <p><strong>Response Example:</strong></p>
     * <pre>
     * HTTP 400 Bad Request
     * {
     *   "code": "BAD_REQUEST",
     *   "message": "Alias 'my-link' is already taken"
     * }
     * </pre>
     *
     * @param e the {@link IllegalArgumentException} that was thrown
     * @return {@link ResponseEntity} containing {@link ErrorResponse} with HTTP 400 status
     * @see IllegalArgumentException
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException e) {
        ErrorResponse error = new ErrorResponse("BAD_REQUEST", e.getMessage());
        return ResponseEntity.badRequest().body(error);
    }

    /**
     * Handles validation errors from Jakarta Bean Validation (JSR-380).
     *
     * <p>This handler processes validation errors that occur when request DTOs fail
     * validation constraints. It aggregates all field validation errors into a
     * single, user-friendly error message that describes all validation failures.</p>
     *
     * <p>The handler extracts validation messages from {@link FieldError} objects
     * and combines them into a comma-separated string, providing comprehensive
     * feedback about what fields failed validation and why.</p>
     *
     * <p><strong>Common Validation Scenarios:</strong></p>
     * <ul>
     *   <li>Empty or null required fields (e.g., originalUrl, alias)</li>
     *   <li>Invalid URL format violations</li>
     *   <li>Pattern constraint failures (e.g., invalid alias characters)</li>
     *   <li>Length constraint violations</li>
     * </ul>
     *
     * <p><strong>Response Example:</strong></p>
     * <pre>
     * HTTP 400 Bad Request
     * {
     *   "code": "VALIDATION_ERROR",
     *   "message": "URL cannot be blank, Alias can only contain letters, numbers, hyphens, and underscores"
     * }
     * </pre>
     *
     * <p><strong>Error Message Format:</strong> Individual field validation errors are
     * joined with commas to provide a comprehensive list of all validation failures
     * in a single response.</p>
     *
     * @param e the {@link MethodArgumentNotValidException} containing validation errors
     * @return {@link ResponseEntity} containing {@link ErrorResponse} with HTTP 400 status
     * @see MethodArgumentNotValidException
     * @see org.springframework.validation.FieldError
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));

        ErrorResponse error = new ErrorResponse("VALIDATION_ERROR", message);
        return ResponseEntity.badRequest().body(error);
    }

    /**
     * Fallback handler for unexpected exceptions not covered by specific handlers.
     *
     * <p>This handler serves as a safety net for any uncaught exceptions that might
     * occur during request processing. It prevents the application from returning
     * default Spring error pages or stack traces to clients, instead providing
     * a consistent error response format.</p>
     *
     * <p><strong>Security Note:</strong> This handler should avoid exposing sensitive
     * system information in error messages. For production environments, consider
     * logging the full exception details while returning generic error messages to clients.</p>
     *
     * <p><strong>Response Example:</strong></p>
     * <pre>
     * HTTP 500 Internal Server Error
     * {
     *   "code": "INTERNAL_SERVER_ERROR",
     *   "message": "An unexpected error occurred. Please try again later."
     * }
     * </pre>
     *
     * <p><strong>Production Considerations:</strong></p>
     * <ul>
     *   <li>Log full exception stack traces for debugging</li>
     *   <li>Return generic error messages to avoid information disclosure</li>
     *   <li>Consider implementing alerting for unexpected exceptions</li>
     *   <li>Monitor exception frequency for system health insights</li>
     * </ul>
     *
     * @param e the unexpected {@link Exception} that was thrown
     * @return {@link ResponseEntity} containing {@link ErrorResponse} with HTTP 500 status
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception e) {
        // In production, log the full exception but return a generic message
        // Logger.error("Unexpected exception occurred", e);

        ErrorResponse error = new ErrorResponse(
                "INTERNAL_SERVER_ERROR",
                "An unexpected error occurred. Please try again later."
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
