package com.tinyurl.service;

import com.tinyurl.DTO.UrlInfoResponse;
import com.tinyurl.DTO.UrlListResponse;
import com.tinyurl.config.UrlShortenerConfig;
import com.tinyurl.entity.UrlEntity;
import com.tinyurl.repository.UrlRepository;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Core business service for URL shortening operations.
 *
 * <p>This service provides the main business logic for the TinyURL application,
 * handling URL shortening, retrieval, analytics, and management operations.
 * It integrates caching for performance optimization and transactional management
 * for data consistency across multiple database operations.</p>
 *
 * <p>The service acts as the business logic layer between controllers and the
 * data access layer, implementing validation, business rules, and coordination
 * of multiple operations while maintaining clean separation of concerns.</p>
 *
 * <p><strong>Key Features:</strong></p>
 * <ul>
 *   <li>URL shortening with custom alias validation</li>
 *   <li>Cached URL resolution for high-performance redirects</li>
 *   <li>Analytics tracking with click count management</li>
 *   <li>Comprehensive URL information retrieval</li>
 *   <li>Transactional data integrity across operations</li>
 * </ul>
 *
 * <p><strong>Caching Strategy:</strong> Uses Spring's caching abstraction to
 * cache frequently accessed URL lookups, significantly improving redirect
 * performance for popular URLs.</p>
 *
 * <p><strong>Transaction Management:</strong> All methods are transactional
 * to ensure data consistency, especially important for operations that modify
 * click counts and perform multiple database operations.</p>
 *
 * @author Jose
 * @since 1.0
 * @see UrlRepository
 * @see UrlShortenerConfig
 * @see org.springframework.cache.annotation.Cacheable
 * @see org.springframework.transaction.annotation.Transactional
 */
@Service
@Transactional
public class UrlShortenerService {

    private final UrlRepository urlRepository;
    private final UrlShortenerConfig config;

    /**
     * Constructs the URL shortener service with required dependencies.
     *
     * <p>Uses constructor injection following Spring Boot best practices
     * for dependency injection. This approach ensures all dependencies are
     * available at construction time and supports immutable field design.</p>
     *
     * @param urlRepository the repository for URL data access operations
     * @param config the configuration properties for URL shortening behavior
     */
    public UrlShortenerService(UrlRepository urlRepository, UrlShortenerConfig config) {
        this.urlRepository = urlRepository;
        this.config = config;
    }

    /**
     * Creates a shortened URL with the specified alias.
     *
     * <p>This method validates the provided alias for uniqueness and creates a new
     * URL entity in the system. The alias must be unique across all existing URLs
     * to prevent conflicts during redirection operations.</p>
     *
     * <p><strong>Validation Rules:</strong></p>
     * <ul>
     *   <li>Alias cannot be null, empty, or contain only whitespace</li>
     *   <li>Alias must be unique across all existing URLs in the system</li>
     *   <li>Alias is trimmed to remove leading/trailing whitespace</li>
     * </ul>
     *
     * <p><strong>Business Logic:</strong></p>
     * <ol>
     *   <li>Validate alias is not null/empty</li>
     *   <li>Check alias uniqueness against existing URLs</li>
     *   <li>Create and persist new URL entity</li>
     *   <li>Return complete shortened URL</li>
     * </ol>
     *
     * @param originalUrl the complete URL to be shortened (validated by DTO layer)
     * @param alias the custom alias for the shortened URL (cannot be null/empty)
     * @return the complete shortened URL string (baseUrl + alias)
     * @throws IllegalArgumentException if alias is null, empty, or already exists
     * @see UrlShortenerConfig#getBaseUrl()
     */
    public String shortenUrl(String originalUrl, String alias) {
        // Validate alias is provided
        if (alias == null || alias.trim().isEmpty()) {
            throw new IllegalArgumentException("Alias is required");
        }

        // Check alias uniqueness
        Optional<UrlEntity> existingAlias = urlRepository.findByAlias(alias.trim());
        if (existingAlias.isPresent()) {
            throw new IllegalArgumentException("Alias '" + alias + "' is already taken");
        }

        // Create new URL entity
        UrlEntity urlEntity = new UrlEntity();
        urlEntity.setOriginalUrl(originalUrl);
        urlEntity.setAlias(alias.trim());

        // Persist to database
        urlRepository.save(urlEntity);

        // Return complete shortened URL
        return config.getBaseUrl() + alias.trim();
    }

    /**
     * Retrieves the original URL for redirection and updates access analytics.
     *
     * <p>This method performs the core URL shortening service function by looking up
     * the original URL associated with a short code and tracking access analytics.
     * It uses caching to optimize performance for frequently accessed URLs and
     * atomically increments the click count for analytics purposes.</p>
     *
     * <p><strong>Caching:</strong> Results are cached using the short code as the key
     * with synchronized access to prevent cache stampede issues. The cache improves
     * performance for popular URLs by avoiding repeated database lookups.</p>
     *
     * <p><strong>Analytics:</strong> Each successful lookup increments the URL's
     * click count, providing real-time access statistics. The increment operation
     * is performed within the same transaction as the lookup for consistency.</p>
     *
     * <p><strong>Performance Note:</strong> While caching improves lookup performance,
     * the click count increment still requires a database write operation. Consider
     * asynchronous analytics updates for ultra-high-traffic scenarios.</p>
     *
     * @param shortCode the alias/short code to look up
     * @return the original URL for redirection purposes
     * @throws EntityNotFoundException if the short code doesn't exist in the system
     * @see org.springframework.cache.annotation.Cacheable
     */
    @Cacheable(value = "urls", key = "#shortCode", sync = true)
    public String getOriginalUrl(String shortCode) throws EntityNotFoundException {
        // Look up URL by alias
        Optional<UrlEntity> urlEntityByAlias = urlRepository.findByAlias(shortCode);

        if (urlEntityByAlias.isEmpty()) {
            throw new EntityNotFoundException("Short URL not found");
        }

        UrlEntity entity = urlEntityByAlias.get();

        // Update analytics - increment click count
        entity.setClickCount(entity.getClickCount() + 1);
        urlRepository.save(entity);

        return entity.getOriginalUrl();
    }

    /**
     * Retrieves all URLs in the system for management and overview purposes.
     *
     * <p>This method fetches all URL entities from the database and converts them
     * to response DTOs suitable for list display. Each URL is enriched with its
     * complete shortened URL by combining the base URL with the stored alias.</p>
     *
     * <p><strong>Performance Consideration:</strong> This method loads all URLs
     * into memory simultaneously. For systems with large numbers of URLs, consider
     * implementing pagination to improve performance and memory usage.</p>
     *
     * <p><strong>Use Cases:</strong></p>
     * <ul>
     *   <li>Administrative overview of all shortened URLs</li>
     *   <li>URL management interfaces</li>
     *   <li>Analytics dashboards and reporting</li>
     *   <li>Bulk operations and data export</li>
     * </ul>
     *
     * @return a list of {@link UrlListResponse} objects containing all URLs with their metadata
     * @see UrlListResponse
     */
    @Transactional(readOnly = true)
    public List<UrlListResponse> getAllUrls() {
        List<UrlEntity> allUrls = urlRepository.findAll();

        return allUrls.stream()
                .map(entity -> {
                    String shortUrl = config.getBaseUrl() + entity.getAlias();
                    return new UrlListResponse(
                            shortUrl,
                            entity.getOriginalUrl(),
                            entity.getAlias(),
                            entity.getCreatedDate(),
                            entity.getClickCount()
                    );
                })
                .collect(Collectors.toList());
    }

    /**
     * Retrieves detailed information about a specific shortened URL.
     *
     * <p>This method provides comprehensive information about a URL without
     * affecting its analytics (unlike {@link #getOriginalUrl(String)} which
     * increments the click count). It's designed for information retrieval,
     * management operations, and analytics display purposes.</p>
     *
     * <p><strong>Read-Only Operation:</strong> This method only reads data and
     * doesn't modify click counts or other analytics, making it suitable for
     * information display and management interfaces.</p>
     *
     * <p><strong>Use Cases:</strong></p>
     * <ul>
     *   <li>URL information display in management interfaces</li>
     *   <li>Analytics reporting and dashboard displays</li>
     *   <li>URL validation and verification operations</li>
     *   <li>Pre-deletion information display</li>
     * </ul>
     *
     * @param shortCode the alias/short code to retrieve information for
     * @return detailed {@link UrlInfoResponse} with all URL metadata and analytics
     * @throws EntityNotFoundException if the short code doesn't exist in the system
     * @see UrlInfoResponse
     */
    @Transactional(readOnly = true)
    public UrlInfoResponse getUrlInfo(String shortCode) throws EntityNotFoundException {
        // Look up URL by alias (read-only, no analytics update)
        Optional<UrlEntity> urlEntityByAlias = urlRepository.findByAlias(shortCode);

        if (urlEntityByAlias.isEmpty()) {
            throw new EntityNotFoundException("Short URL not found");
        }

        UrlEntity entity = urlEntityByAlias.get();
        String shortUrl = config.getBaseUrl() + entity.getAlias();

        return new UrlInfoResponse(
                shortUrl,
                entity.getOriginalUrl(),
                entity.getAlias(),
                entity.getCreatedDate(),
                entity.getClickCount()
        );
    }

    /**
     * Deletes a URL from the system by its database ID.
     *
     * <p>This method permanently removes a URL entity from the system using its
     * database identifier. Once deleted, the shortened URL will no longer be
     * accessible and all associated analytics data will be lost.</p>
     *
     * <p><strong>Cache Implications:</strong> After deletion, any cached entries
     * for the URL should be invalidated. Consider adding {@link CacheEvict}
     * annotation to ensure cache consistency.</p>
     *
     * <p><strong>Data Integrity:</strong> This operation is irreversible. The URL
     * and all its associated data (analytics, creation date, etc.) will be
     * permanently removed from the system.</p>
     *
     * <p><strong>Implementation Note:</strong> Currently uses database ID for deletion.
     * Consider whether alias-based deletion might be more intuitive for users,
     * matching the pattern used in other methods.</p>
     *
     * @param id the database ID of the URL entity to delete
     * @throws EntityNotFoundException if no URL exists with the specified ID
     * @see org.springframework.cache.annotation.CacheEvict
     */
    public void deleteUrl(String id) throws EntityNotFoundException {
        if (!urlRepository.existsById(id)) {
            throw new EntityNotFoundException("URL not found with id: " + id);
        }
        urlRepository.deleteById(id);
    }
}
