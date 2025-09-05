package com.tinyurl.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for standardized API error responses.
 *
 * <p>This DTO provides a consistent structure for error responses across all
 * REST API endpoints in the TinyURL application. It encapsulates error information
 * in a standardized format that clients can reliably parse and handle, improving
 * the overall API user experience and error handling capabilities.</p>
 *
 * <p>The error response follows industry best practices for REST API error handling
 * by providing both machine-readable error codes and human-readable messages.
 * This dual approach enables both programmatic error handling and user-friendly
 * error display in client applications.</p>
 *
 * <p><strong>Design Principles:</strong></p>
 * <ul>
 *   <li>Consistent structure across all API error responses</li>
 *   <li>Machine-readable error codes for programmatic handling</li>
 *   <li>Human-readable messages for user display</li>
 *   <li>Lightweight design optimized for network transfer</li>
 * </ul>
 *
 * <p><strong>Usage Example:</strong></p>
 * <pre>
 * ErrorResponse error = new ErrorResponse("ALIAS_ALREADY_EXISTS",
 *     "The alias 'my-link' is already taken. Please choose a different alias.");
 * </pre>
 *
 * <p><strong>JSON Representation:</strong></p>
 * <pre>
 * {
 *   "code": "ALIAS_ALREADY_EXISTS",
 *   "message": "The alias 'my-link' is already taken. Please choose a different alias."
 * }
 * </pre>
 *
 * <p><strong>Global Exception Handling:</strong> This DTO is typically used in
 * conjunction with Spring Boot's {@code @ControllerAdvice} for centralized
 * exception handling, ensuring consistent error response format across the application.</p>
 *
 * @author Jose
 * @since 1.0
 * @see lombok.Data
 * @see lombok.AllArgsConstructor
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {

    /**
     * Machine-readable error code for programmatic error handling.
     *
     * <p>This field contains a standardized error code that client applications
     * can use for programmatic error handling and conditional logic. Error codes
     * should be consistent, descriptive, and follow a clear naming convention
     * to enable reliable client-side error processing.</p>
     *
     * <p><strong>Naming Convention:</strong> Error codes should use UPPER_SNAKE_CASE
     * format and be descriptive of the specific error condition. They should be
     * stable across API versions to maintain client compatibility.</p>
     *
     * <p><strong>Common Error Codes:</strong></p>
     * <ul>
     *   <li>{@code ENTITY_NOT_FOUND} - Requested resource doesn't exist</li>
     *   <li>{@code ALIAS_ALREADY_EXISTS} - URL alias is already taken</li>
     *   <li>{@code INVALID_URL_FORMAT} - Provided URL format is invalid</li>
     *   <li>{@code ALIAS_REQUIRED} - Alias field is required but missing</li>
     *   <li>{@code VALIDATION_FAILED} - Input validation errors</li>
     * </ul>
     *
     * <p><strong>Client Usage:</strong> Clients can check this field to determine
     * the specific error type and implement appropriate handling logic, such as
     * displaying specific error messages or triggering retry mechanisms.</p>
     *
     * <p><strong>Example:</strong> "ALIAS_ALREADY_EXISTS"</p>
     */
    private String code;

    /**
     * Human-readable error message for user display.
     *
     * <p>This field contains a descriptive, user-friendly error message that
     * explains what went wrong and, when possible, provides guidance on how
     * to resolve the issue. Messages should be clear, concise, and appropriate
     * for display to end users.</p>
     *
     * <p><strong>Message Guidelines:</strong></p>
     * <ul>
     *   <li>Use clear, non-technical language accessible to all users</li>
     *   <li>Provide specific information about what caused the error</li>
     *   <li>Include actionable guidance when possible</li>
     *   <li>Avoid exposing sensitive system information</li>
     *   <li>Keep messages concise while being informative</li>
     * </ul>
     *
     * <p><strong>Localization Considerations:</strong> For internationalized
     * applications, this message should be localized based on the user's
     * preferred language. Consider using message keys and resource bundles
     * for proper internationalization support.</p>
     *
     * <p><strong>Security Note:</strong> Error messages should not expose
     * sensitive system details, internal paths, or security-related information
     * that could be exploited by malicious users.</p>
     *
     * <p><strong>Examples:</strong></p>
     * <ul>
     *   <li>"The alias 'my-link' is already taken. Please choose a different alias."</li>
     *   <li>"The provided URL format is invalid. Please enter a valid URL starting with http:// or https://"</li>
     *   <li>"The requested short URL could not be found."</li>
     * </ul>
     */
    private String message;
}
