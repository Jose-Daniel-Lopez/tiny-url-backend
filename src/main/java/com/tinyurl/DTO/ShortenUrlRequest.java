package com.tinyurl.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

/**
 * Data Transfer Object for URL shortening requests.
 *
 * <p>This DTO encapsulates the data required to create a shortened URL, including
 * the original URL to be shortened and an optional custom alias. The class uses
 * Jakarta Bean Validation annotations to ensure data integrity and prevent invalid
 * requests from reaching the service layer.</p>
 *
 * <p>The validation ensures that:</p>
 * <ul>
 *   <li>The original URL is not blank and follows valid URL format</li>
 *   <li>The alias is not blank and contains only safe characters</li>
 *   <li>Both fields meet security and usability requirements</li>
 * </ul>
 *
 * <p><strong>Usage Example:</strong></p>
 * <pre>
 * ShortenUrlRequest request = new ShortenUrlRequest(
 *     "<a href="https://www.example.com/very/long/url">...</a>",
 *     "my-custom-alias"
 * );
 * </pre>
 *
 * @author Jose
 * @since 1.0
 * @see NotBlank
 * @see URL
 * @see Pattern
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShortenUrlRequest {

    /**
     * The original URL to be shortened.
     *
     * <p>This field must contain a valid, well-formed URL that users will be
     * redirected to when accessing the shortened URL. The URL validation uses
     * Hibernate Validator's {@link URL} constraint which leverages {@code java.net.URL}
     * for validation, ensuring the URL is syntactically correct and contains a valid protocol.</p>
     *
     * <p><strong>Supported protocols:</strong> http, https, ftp, file, jar (default JVM handlers)</p>
     *
     * <p><strong>Validation rules:</strong></p>
     * <ul>
     *   <li>Cannot be null, empty, or contain only whitespace</li>
     *   <li>Must follow valid URL syntax (protocol://host[:port][/path][?query][#fragment])</li>
     *   <li>Protocol handler must be available in the JVM</li>
     * </ul>
     *
     * <p><strong>Examples of valid URLs:</strong></p>
     * <ul>
     *   <li><a href="https://www.example.com">...</a></li>
     *   <li>http://localhost:8080/api/test</li>
     *   <li>ftp://files.example.com/downloads</li>
     * </ul>
     */
    @NotBlank(message = "URL cannot be blank")
    @URL(message = "Invalid URL format")
    private String originalUrl;

    /**
     * Custom alias for the shortened URL.
     *
     * <p>This field allows users to specify a custom, human-readable identifier
     * for their shortened URL instead of using a randomly generated code. The alias
     * becomes part of the final shortened URL path.</p>
     *
     * <p><strong>Character restrictions:</strong> Only alphanumeric characters, hyphens,
     * and underscores are allowed to ensure URL safety, prevent encoding issues,
     * and maintain compatibility across different systems and browsers.</p>
     *
     * <p><strong>Validation rules:</strong></p>
     * <ul>
     *   <li>Cannot be null, empty, or contain only whitespace</li>
     *   <li>Must match pattern: {@code ^[a-zA-Z0-9_-]+$}</li>
     *   <li>No special characters, spaces, or Unicode characters allowed</li>
     * </ul>
     *
     * <p><strong>Examples of valid aliases:</strong></p>
     * <ul>
     *   <li>my-custom-link</li>
     *   <li>blog_post_2024</li>
     *   <li>ProductDemo123</li>
     *   <li>user-profile</li>
     * </ul>
     *
     * <p><strong>Security considerations:</strong> The restricted character set prevents
     * potential security issues such as path traversal attacks, script injection through
     * URLs, and encoding-related vulnerabilities.</p>
     */
    @NotBlank(message = "Alias cannot be blank")
    @Pattern(
            regexp = "^[a-zA-Z0-9_-]+$",
            message = "Alias can only contain letters, numbers, hyphens, and underscores"
    )
    private String alias;
}
