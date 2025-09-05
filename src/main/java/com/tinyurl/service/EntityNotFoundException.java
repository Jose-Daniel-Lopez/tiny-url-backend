package com.tinyurl.service;

/**
 * Custom exception thrown when a requested URL entity cannot be found.
 *
 * <p>This exception is specifically designed for URL shortening service scenarios
 * where clients request information about a shortened URL that doesn't exist in the
 * system. It extends {@link RuntimeException} to avoid forcing calling code to
 * handle checked exceptions, following Spring Boot best practices for web applications.</p>
 *
 * <p>The exception is typically thrown by service layer methods when:</p>
 * <ul>
 *   <li>A user attempts to access a shortened URL that doesn't exist</li>
 *   <li>URL information is requested for a non-existent alias</li>
 *   <li>Operations are attempted on deleted or expired URLs</li>
 *   <li>Database queries return empty results for URL lookups</li>
 * </ul>
 *
 * <p><strong>Usage in REST Controllers:</strong> This exception should be handled
 * by a global exception handler (e.g., {@code @ControllerAdvice}) to return
 * appropriate HTTP 404 Not Found responses to clients.</p>
 *
 * <p><strong>Example Usage:</strong></p>
 * <pre>
 * if (urlEntity == null) {
 *     throw new EntityNotFoundException("Short URL 'abc123' not found");
 * }
 * </pre>
 *
 * @author Jose
 * @since 1.0
 * @see RuntimeException
 */
public class EntityNotFoundException extends RuntimeException {

    /**
     * Constructs a new EntityNotFoundException with the specified detail message.
     *
     * <p>Creates an exception with a descriptive error message that should provide
     * clear information about what entity was not found. The message should be
     * user-friendly enough to be included in API responses while providing sufficient
     * detail for debugging and logging purposes.</p>
     *
     * <p><strong>Message Guidelines:</strong></p>
     * <ul>
     *   <li>Be specific about what entity was not found (e.g., "Short URL", "URL alias")</li>
     *   <li>Include the identifier that was searched for when appropriate</li>
     *   <li>Use clear, user-friendly language suitable for API responses</li>
     *   <li>Avoid exposing sensitive system information in the message</li>
     * </ul>
     *
     * <p><strong>Example Messages:</strong></p>
     * <ul>
     *   <li>"Short URL 'abc123' not found"</li>
     *   <li>"URL with alias 'my-custom-link' does not exist"</li>
     *   <li>"Requested URL entity could not be located"</li>
     * </ul>
     *
     * @param message the detail message explaining what entity was not found.
     *               This message will be returned by {@link #getMessage()} and
     *               should be suitable for logging and API responses.
     */
    public EntityNotFoundException(String message) {
        super(message);
    }

    /**
     * Constructs a new EntityNotFoundException with the specified detail message and cause.
     *
     * <p>This constructor is useful when the EntityNotFoundException is being thrown
     * as a result of another exception, such as a database access error or underlying
     * system failure. The cause is preserved for debugging and logging purposes while
     * presenting a clean, domain-specific exception to the calling code.</p>
     *
     * <p><strong>Use Cases:</strong></p>
     * <ul>
     *   <li>Wrapping database exceptions during URL lookup operations</li>
     *   <li>Converting lower-level exceptions to domain-specific exceptions</li>
     *   <li>Maintaining exception chain for debugging purposes</li>
     * </ul>
     *
     * @param message the detail message explaining what entity was not found
     * @param cause the underlying exception that caused this EntityNotFoundException
     *              to be thrown. This is typically a lower-level exception from
     *              the data access layer or external service calls.
     */
    public EntityNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
