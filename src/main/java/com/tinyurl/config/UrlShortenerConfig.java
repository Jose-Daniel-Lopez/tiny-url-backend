package com.tinyurl.config;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Configuration properties for URL shortener functionality.
 *
 * <p>This class binds externalized configuration properties with the prefix "url-shortener"
 * to provide type-safe access to URL shortening parameters. The configuration supports
 * customization of key generation algorithms, URL formatting, and service behavior.</p>
 *
 * <p>Properties can be configured in application.properties or application.yml:</p>
 * <pre>
 * url-shortener:
 *   allowed-characters: "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
 *   key-length: 7
 *   base-url: "<a href="https://tinyurl.example.com">...</a>"
 * </pre>
 *
 * <p>The {@link Data} annotation automatically generates getters, setters, toString(),
 * equals(), and hashCode() methods, reducing boilerplate code while maintaining clean
 * configuration binding.</p>
 *
 * @author Jose
 * @since 1.0
 * @see ConfigurationProperties
 * @see Data
 */
@Component
@ConfigurationProperties(prefix = "url-shortener")
@Data
@Validated
public class UrlShortenerConfig {

    /**
     * Characters allowed for generating short URL keys.
     *
     * <p>This string defines the character set used by the key generation algorithm
     * to create unique short URL identifiers. A larger character set provides more
     * possible combinations but should exclude characters that may cause URL encoding
     * issues or user confusion (e.g., 0 vs O, 1 vs l).</p>
     *
     * <p><strong>Default recommended value:</strong>
     * "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"</p>
     *
     * <p><strong>Security Note:</strong> The character set size directly affects the
     * keyspace and collision resistance. A 62-character set (A-Z, a-z, 0-9) provides
     * good entropy for most use cases.</p>
     */
    @NotBlank(message = "Allowed characters cannot be blank")
    private String allowedCharacters;

    /**
     * Length of generated short URL keys.
     *
     * <p>Determines the number of characters in generated short URL keys. Higher values
     * increase the keyspace exponentially, reducing collision probability but resulting
     * in longer URLs. The optimal length depends on expected URL volume and collision
     * tolerance.</p>
     *
     * <p><strong>Keyspace calculation:</strong> With 62 allowed characters and length 7,
     * the keyspace is 62^7 ≈ 3.5 trillion possible keys.</p>
     *
     * <p><strong>Recommended range:</strong> 6-8 characters for most applications.</p>
     */
    @NotNull(message = "Key length cannot be null")
    @Min(value = 1, message = "Key length must be at least 1")
    private int keyLength;

    /**
     * Base URL for constructing complete short URLs.
     *
     * <p>The base URL that will be prepended to generated keys to form complete
     * short URLs. This should be the publicly accessible domain where the URL
     * shortening service is hosted.</p>
     *
     * <p><strong>Format:</strong> Should include the protocol (<a href="https://">...</a>) and domain,
     * optionally with a context path. Should NOT end with a trailing slash as it
     * will be added during URL construction.</p>
     *
     * <p><strong>Example:</strong> "https://tinyurl.example.com" results in URLs like
     * "https://tinyurl.example.com/abc123"</p>
     *
     * <p><strong>Security Note:</strong> Always use HTTPS in production environments
     * to ensure secure redirection and prevent URL manipulation.</p>
     */
    @NotBlank(message = "Base URL cannot be blank")
    private String baseUrl;
}
