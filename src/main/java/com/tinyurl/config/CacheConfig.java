package com.tinyurl.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Cache configuration for the TinyURL application.
 *
 * <p>This configuration enables Spring Boot's caching abstraction and provides
 * a simple in-memory cache manager for URL caching operations. The cache is designed
 * to improve performance by reducing database lookups for frequently accessed URLs.</p>
 *
 * <p><strong>Note:</strong> The current implementation uses {@link ConcurrentMapCacheManager}
 * which is suitable for development and testing but may not be appropriate for production
 * environments due to memory limitations and lack of persistence across application restarts.</p>
 *
 * @author Jose
 * @since 1.0
 * @see org.springframework.cache.annotation.EnableCaching
 * @see org.springframework.cache.CacheManager
 */
@Configuration
@EnableCaching
public class CacheConfig {

    /**
     * Cache name constant for URL-related caching operations.
     *
     * <p>This cache is used to store URL mappings and improve lookup performance
     * by avoiding repeated database queries for the same URL data.</p>
     */
    public static final String URLS_CACHE = "urls";

    /**
     * Creates and configures the cache manager for the application.
     *
     * <p>This method configures a {@link ConcurrentMapCacheManager} with a predefined
     * cache named "urls" for storing URL-related data. The cache manager uses
     * {@link java.util.concurrent.ConcurrentHashMap} internally for thread-safe operations.</p>
     *
     * <p><strong>Production Considerations:</strong></p>
     * <ul>
     *   <li>For production use, consider using distributed cache solutions like Redis or Hazelcast</li>
     *   <li>Implement cache eviction policies to prevent memory overflow</li>
     *   <li>Add cache metrics and monitoring for performance optimization</li>
     * </ul>
     *
     * @return the configured {@link CacheManager} instance
     * @see ConcurrentMapCacheManager
     * @see #URLS_CACHE
     */
    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager(URLS_CACHE);
    }
}
