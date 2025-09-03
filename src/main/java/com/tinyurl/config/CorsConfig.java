package com.tinyurl.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Cross-Origin Resource Sharing (CORS) configuration for the TinyURL application.
 *
 * <p>This configuration enables CORS support for all endpoints, allowing web clients
 * from different origins to interact with the TinyURL API. The current setup provides
 * a permissive configuration suitable for development environments.</p>
 *
 * <p><strong>Security Warning:</strong> The current configuration allows all origins (*)
 * which may pose security risks in production environments. Consider restricting
 * {@code allowedOrigins} to specific domains in production deployments.</p>
 *
 * <p>CORS is enforced by browsers and controls which web pages can access resources
 * from this server. This configuration handles preflight requests and sets appropriate
 * response headers for cross-origin requests.</p>
 *
 * @author Your Name
 * @since 1.0
 * @see org.springframework.web.servlet.config.annotation.WebMvcConfigurer
 * @see org.springframework.web.servlet.config.annotation.CorsRegistry
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    /**
     * Configures CORS mappings for all endpoints in the application.
     *
     * <p>This method sets up global CORS policy that applies to all request mappings.
     * The configuration includes:</p>
     *
     * <ul>
     *   <li><strong>Mappings:</strong> All endpoints ("/**")</li>
     *   <li><strong>Origins:</strong> All origins ("*") - Consider restricting in production</li>
     *   <li><strong>Methods:</strong> Common HTTP methods including OPTIONS for preflight</li>
     *   <li><strong>Headers:</strong> All request headers allowed</li>
     * </ul>
     *
     * <p><strong>Production Recommendations:</strong></p>
     * <ul>
     *   <li>Replace "*" with specific allowed origins (e.g., "<a href="https://yourdomain.com">...</a>")</li>
     *   <li>Restrict allowed headers to only those required by your application</li>
     *   <li>Consider setting {@code allowCredentials(true)} if authentication is needed</li>
     *   <li>Add {@code maxAge()} for preflight cache optimization</li>
     * </ul>
     *
     * @param registry the {@link CorsRegistry} to configure CORS mappings
     * @see <a href="https://developer.mozilla.org/en-US/docs/Web/HTTP/CORS">MDN CORS Documentation</a>
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*");
    }
}
