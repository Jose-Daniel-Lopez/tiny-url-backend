package com.tinyurl.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

/**
 * MongoDB configuration for the TinyURL application.
 *
 * <p>This configuration class enables MongoDB auditing capabilities, which automatically
 * populates audit fields such as creation date, modification date, and user information
 * in MongoDB documents that implement auditing interfaces.</p>
 *
 * <p>When {@link EnableMongoAuditing} is enabled, Spring Data MongoDB will automatically:</p>
 * <ul>
 *   <li>Set {@code @CreatedDate} fields when documents are first persisted</li>
 *   <li>Update {@code @LastModifiedDate} fields when documents are modified</li>
 *   <li>Populate {@code @CreatedBy} and {@code @ModifiedBy} fields if auditor aware is configured</li>
 * </ul>
 *
 * <p>Entity classes should extend {@code AbstractAuditingEntity} or implement the appropriate
 * auditing interfaces to take advantage of these features.</p>
 *
 * <p><strong>Note:</strong> Additional MongoDB configuration such as connection settings,
 * custom converters, or transaction management can be added to this class as needed.</p>
 *
 * @author Jose
 * @since 1.0
 * @see org.springframework.data.mongodb.config.EnableMongoAuditing
 * @see org.springframework.data.annotation.CreatedDate
 * @see org.springframework.data.annotation.LastModifiedDate
 * @see org.springframework.data.annotation.CreatedBy
 * @see org.springframework.data.annotation.LastModifiedBy
 */
@Configuration
@EnableMongoAuditing
public class MongoConfig {

    // Additional MongoDB configuration beans can be added here as needed
}
