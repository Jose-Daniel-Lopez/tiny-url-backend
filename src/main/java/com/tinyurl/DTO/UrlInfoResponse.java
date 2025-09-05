package com.tinyurl.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Data Transfer Object for detailed URL information responses.
 *
 * <p>This DTO encapsulates comprehensive information about a shortened URL, including
 * its metadata, access statistics, and creation details. It serves as the response
 * structure for endpoints that provide detailed URL analytics and information,
 * offering clients complete visibility into URL usage and properties.</p>
 *
 * <p>The response aggregates data from multiple sources including URL mappings,
 * access logs, and configuration settings to provide a complete picture of a
 * shortened URL's lifecycle and performance.</p>
 *
 * <p><strong>Usage Example:</strong></p>
 * <pre>
 * UrlInfoResponse info = new UrlInfoResponse(
 *     "<a href="https://tinyurl.example.com/abc123">...</a>",
 *     "https://www.example.com/very/long/url",
 *     "my-custom-alias",
 *     new Date(),
 *     42L
 * );
 * </pre>
 *
 * <p><strong>JSON Representation:</strong></p>
 * <pre>
 * {
 *   "shortUrl": "https://tinyurl.example.com/abc123",
 *   "originalUrl": "https://www.example.com/very/long/url",
 *   "alias": "my-custom-alias",
 *   "createdDate": "2024-01-15T10:30:00.000Z",
 *   "clickCount": 42
 * }
 * </pre>
 *
 * @author Jose
 * @since 1.0
 * @see ShortenUrlRequest
 * @see ShortenUrlResponse
 * @see Data
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UrlInfoResponse {

    /**
     * The complete shortened URL.
     *
     * <p>This field contains the fully qualified shortened URL that clients can use
     * for redirection purposes. It represents the publicly accessible URL that users
     * interact with to access the original destination.</p>
     *
     * <p><strong>Format:</strong> {baseUrl}/{shortCode} where the baseUrl is configured
     * in application properties and shortCode is either generated or custom alias.</p>
     *
     * <p><strong>Example:</strong> "<a href="https://tinyurl.example.com/abc123">...</a>"</p>
     */
    private String shortUrl;

    /**
     * The original long URL that was shortened.
     *
     * <p>This field contains the destination URL where users will be redirected
     * when they access the shortened URL. It represents the original, unmodified
     * URL that was provided during the shortening process.</p>
     *
     * <p>The original URL is validated during creation to ensure it follows proper
     * URL format and is accessible. This field provides transparency about the
     * destination without requiring clients to follow redirects.</p>
     *
     * <p><strong>Example:</strong> "<a href="https://www.example.com/articles/2024/spring-boot-guide">...</a>"</p>
     */
    private String originalUrl;

    /**
     * The custom alias or generated short code identifier.
     *
     * <p>This field represents the unique identifier portion of the shortened URL.
     * It can be either a custom alias provided by the user during URL creation
     * or an automatically generated short code following the configured algorithm.</p>
     *
     * <p><strong>Custom Alias:</strong> User-defined, human-readable identifier that
     * follows the pattern {@code ^[a-zA-Z0-9_-]+$} for URL safety.</p>
     *
     * <p><strong>Generated Code:</strong> Automatically created using the configured
     * character set and key length to ensure uniqueness and collision resistance.</p>
     *
     * <p><strong>Examples:</strong></p>
     * <ul>
     *   <li>Custom alias: "my-blog-post"</li>
     *   <li>Generated code: "a1B2c3D"</li>
     * </ul>
     */
    private String alias;

    /**
     * The timestamp when the shortened URL was created.
     *
     * <p>This field records the exact date and time when the URL shortening
     * request was processed and the shortened URL was first created in the system.
     * It uses {@link java.util.Date} for compatibility with existing serialization
     * and database mapping configurations.</p>
     *
     * <p><strong>Timezone Considerations:</strong> The date is typically stored in UTC
     * to ensure consistency across different geographical locations and client timezones.
     * Client applications should handle timezone conversion for display purposes.</p>
     *
     * <p><strong>Usage:</strong> This information is valuable for analytics, audit trails,
     * and understanding URL lifecycle patterns.</p>
     *
     * <p><strong>Note:</strong> Consider migrating to {@code java.time.Instant} or
     * {@code java.time.OffsetDateTime} for better timezone handling in future versions.</p>
     */
    private Date createdDate;

    /**
     * The total number of times the shortened URL has been accessed.
     *
     * <p>This field provides access analytics by tracking how many times users have
     * clicked on the shortened URL and been redirected to the original destination.
     * Each successful redirection increments this counter.</p>
     *
     * <p><strong>Counting Logic:</strong></p>
     * <ul>
     *   <li>Incremented on each successful HTTP 302 redirect response</li>
     *   <li>Excludes failed access attempts (404 errors, etc.)</li>
     *   <li>May include bot traffic depending on filtering configuration</li>
     * </ul>
     *
     * <p><strong>Data Type:</strong> Uses {@code Long} to accommodate high-traffic URLs
     * that may exceed {@code Integer.MAX_VALUE} (2.1 billion) clicks over time.</p>
     *
     * <p><strong>Analytics Value:</strong> This metric helps users understand URL
     * popularity, campaign effectiveness, and content engagement patterns.</p>
     *
     * <p><strong>Initial Value:</strong> Set to 0 when the URL is first created.</p>
     */
    private Long clickCount;
}
