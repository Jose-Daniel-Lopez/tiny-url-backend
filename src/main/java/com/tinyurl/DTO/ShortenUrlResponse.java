package com.tinyurl.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for URL shortening responses.
 *
 * <p>This DTO encapsulates the response data returned after successfully creating
 * a shortened URL. It contains the complete shortened URL that clients can use
 * for redirection purposes. The response provides a clean, structured format
 * for API consumers to extract the shortened URL information.</p>
 *
 * <p>The shortened URL included in this response is a fully qualified URL that
 * can be directly used by clients, browsers, or other applications to redirect
 * users to the original long URL.</p>
 *
 * <p><strong>Usage Example:</strong></p>
 * <pre>
 * ShortenUrlResponse response = new ShortenUrlResponse("<a href="https://tinyurl.example.com/abc123">...</a>");
 * String shortUrl = response.getShortUrl(); // "https://tinyurl.example.com/abc123"
 * </pre>
 *
 * <p><strong>JSON Representation:</strong></p>
 * <pre>
 * {
 *   "shortUrl": "https://tinyurl.example.com/abc123"
 * }
 * </pre>
 *
 * @author Jose
 * @since 1.0
 * @see ShortenUrlRequest
 * @see Data
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShortenUrlResponse {

    /**
     * The complete shortened URL.
     *
     * <p>This field contains the fully qualified shortened URL that clients can use
     * to access the original URL through redirection. The shortened URL is constructed
     * by combining the configured base URL with the generated or custom short code/alias.</p>
     *
     * <p><strong>URL Structure:</strong> The shortened URL follows the pattern:
     * {@code {baseUrl}/{shortCode}} where:</p>
     * <ul>
     *   <li><strong>baseUrl:</strong> The configured base URL from application properties</li>
     *   <li><strong>shortCode:</strong> Either a generated unique identifier or custom alias</li>
     * </ul>
     *
     * <p><strong>Example URLs:</strong></p>
     * <ul>
     *   <li><a href="https://tinyurl.example.com/a1B2c3">...</a> (generated short code)</li>
     *   <li>https://tinyurl.example.com/my-custom-alias (custom alias)</li>
     * </ul>
     *
     * <p><strong>Usage:</strong> This URL can be shared, embedded in applications,
     * or used anywhere a standard URL is expected. When accessed, it will automatically
     * redirect (HTTP 302) users to the original long URL.</p>
     *
     * <p><strong>Protocol:</strong> The URL will always use the protocol specified
     * in the base URL configuration, typically HTTPS for production environments
     * to ensure secure redirection.</p>
     */
    private String shortUrl;
}
