package com.tinyurl.repository;

import com.tinyurl.entity.UrlEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for URL entity database operations.
 *
 * <p>This repository provides data access layer functionality for {@link UrlEntity} objects,
 * extending Spring Data MongoDB's {@link MongoRepository} to offer comprehensive CRUD operations
 * and custom query methods. The repository handles efficient data retrieval for URL shortening
 * operations, analytics, and maintenance tasks.</p>
 *
 * <p>The interface leverages Spring Data's repository abstraction, providing both derived
 * query methods through method naming conventions and custom queries using the {@link Query}
 * annotation for complex MongoDB operations.</p>
 *
 * <p><strong>Key Features:</strong></p>
 * <ul>
 *   <li>Standard CRUD operations via {@link MongoRepository}</li>
 *   <li>Custom finder methods for URL alias lookups</li>
 *   <li>Expiration-based queries for URL maintenance</li>
 *   <li>Optimized queries leveraging MongoDB's native capabilities</li>
 * </ul>
 *
 * <p><strong>Performance Considerations:</strong> All query methods in this repository
 * are designed to leverage the indexing strategy defined in {@link UrlEntity}, particularly
 * the unique index on the alias field for optimal lookup performance.</p>
 *
 * @author Jose
 * @since 1.0
 * @see com.tinyurl.entity.UrlEntity
 * @see org.springframework.data.mongodb.repository.MongoRepository
 * @see org.springframework.data.mongodb.repository.Query
 */
@Repository
public interface UrlRepository extends MongoRepository<UrlEntity, String> {

    /**
     * Finds a URL entity by its unique alias.
     *
     * <p>This method performs a lookup based on the alias field, which serves as the
     * unique identifier in shortened URLs. The query leverages the unique index on
     * the alias field for optimal performance, making this one of the most frequently
     * used and performance-critical operations in the URL shortening service.</p>
     *
     * <p><strong>Query Derivation:</strong> Spring Data MongoDB automatically derives
     * the query from the method name, generating the equivalent of: {@code {"alias": alias}}</p>
     *
     * <p><strong>Performance:</strong> This operation is highly optimized due to the unique
     * index on the alias field, providing O(log n) lookup time complexity.</p>
     *
     * <p><strong>Use Cases:</strong></p>
     * <ul>
     *   <li>URL redirection operations (most common use case)</li>
     *   <li>URL information retrieval for analytics</li>
     *   <li>Alias validation during URL creation</li>
     *   <li>URL management operations (update, delete)</li>
     * </ul>
     *
     * @param alias the unique alias to search for, typically extracted from the shortened URL path
     * @return an {@link Optional} containing the {@link UrlEntity} if found, empty otherwise
     */
    Optional<UrlEntity> findByAlias(String alias);

    /**
     * Finds all URLs that have expired before the specified date.
     *
     * <p>This method uses a custom MongoDB query to find URLs whose expiration date
     * is earlier than the provided current date. It's designed for maintenance operations
     * such as cleanup tasks, expired URL removal, and system maintenance workflows.</p>
     *
     * <p><strong>Query Details:</strong> The method uses the {@code $lt} (less than) operator
     * to find documents where {@code expiresDate < currentDate}. The query is executed
     * efficiently using MongoDB's native query capabilities.</p>
     *
     * <p><strong>Important Note:</strong> The current {@link UrlEntity} does not include
     * an {@code expiresDate} field. This method appears to be designed for future functionality
     * or requires the {@code expiresDate} field to be added to the entity.</p>
     *
     * <p><strong>Maintenance Use Cases:</strong></p>
     * <ul>
     *   <li>Scheduled cleanup of expired URLs</li>
     *   <li>System maintenance and storage optimization</li>
     *   <li>Compliance with data retention policies</li>
     *   <li>Performance optimization by removing stale data</li>
     * </ul>
     *
     * <p><strong>Implementation Consideration:</strong> For this method to work correctly,
     * the {@link UrlEntity} should be updated to include an {@code expiresDate} field
     * with appropriate indexing for efficient query execution.</p>
     *
     * @param currentDate the reference date to compare against expiration dates
     * @return a list of {@link UrlEntity} objects that have expired before the current date
     * @see Query
     */
    @Query("{ 'expiresDate' : { $lt: ?0 } }")
    List<UrlEntity> findExpiredUrls(Date currentDate);
}
