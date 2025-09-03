package com.tinyurl.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Data Transfer Object for URL list responses in collection endpoints.
 *
 * <p>This DTO represents individual URL entries within collection responses,
 * providing essential information about shortened URLs in a format optimized
 * for list views and bulk operations. It contains the same comprehensive data
 * as {@link UrlInfoResponse} but is specifically designed for use in array
 * responses where multiple URLs are returned simultaneously.</p>
 *
 * <p>This response format is typically used by endpoints that return multiple
 * URL records, such as "get all URLs" operations, search results, or paginated
 * URL listings. Each instance represents one shortened URL with its complete
 * metadata and analytics information.</p>
 *
 * <p><strong>Usage Context:</strong> Designed for REST endpoints that return
 * collections of URLs, enabling clients to display URL lists with full
 * information without requiring additional API calls for details.</p>
 *
 * <p><strong>JSON Array Representation:</strong></p>
 * <pre>
 * [
 *   {
 *     "shortUrl": "<a href="https://tinyurl.example.com/abc123">...</a>",
 *     "originalUrl": "https://www.example.com/page1",
 *     "alias": "custom-alias-1",
 *     "createdDate": "2024-01-15T10:30:00.000Z",
 *     "clickCount": 42
 *   },
 *   {
 *     "shortUrl": "https://tinyurl.example.com/def456",
 *     "originalUrl": "https://www.example.com/page2",
 *     "alias": "generated-code",
 *     "createdDate": "2024-01-16T14:20:00.000Z",
 *     "clickCount": 15
 *   }
 * ]
 * </pre>
 *
 * @author Jose
 * @since 1.0
 * @see UrlInfoResponse
 * @see ShortenUrlRequest
 * @see Data
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UrlListResponse {

    /**
     * The complete shortened URL.
     *
     * <p>Contains the fully qualified shortened URL that can be used for
     * redirection. In list contexts, this provides immediate access to
     * the clickable shortened URL without requiring additional API calls.</p>
     *
     * <p><strong>Format:</strong> {baseUrl}/{shortCode}</p>
     * <p><strong>Example:</strong> "<a href="https://tinyurl.example.com/abc123">...</a>"</p>
     */
    private String shortUrl;

    /**
     * The original long URL that was shortened.
     *
     * <p>The destination URL where users will be redirected. In list views,
     * this allows users to quickly identify the destination without following
     * the redirect, enabling better URL management and organization.</p>
     *
     * <p><strong>Display Purpose:</strong> Often truncated in UI list views
     * for better readability while maintaining full URL in API responses.</p>
     *
     * <p><strong>Example:</strong> "<a href="https://www.example.com/articles/spring-boot-guide">...</a>"</p>
     */
    private String originalUrl;

    /**
     * The custom alias or generated short code identifier.
     *
     * <p>The unique identifier used in the shortened URL. In list contexts,
     * this field is essential for URL identification, editing, and management
     * operations where users need to reference specific URLs.</p>
     *
     * <p><strong>Types:</strong></p>
     * <ul>
     *   <li><strong>Custom alias:</strong> User-defined, human-readable (e.g., "my-blog-post")</li>
     *   <li><strong>Generated code:</strong> System-generated unique identifier (e.g., "a1B2c3D")</li>
     * </ul>
     *
     * <p><strong>List Usage:</strong> Often used as the primary identifier in
     * management interfaces for editing, deleting, or analyzing specific URLs.</p>
     */
    private String alias;

    /**
     * The timestamp when the shortened URL was created.
     *
     * <p>Creation timestamp for the URL entry. In list views, this enables
     * chronological sorting, filtering by date ranges, and understanding
     * URL creation patterns over time.</p>
     *
     * <p><strong>List Context Benefits:</strong></p>
     * <ul>
     *   <li>Enables "newest first" or "oldest first" sorting</li>
     *   <li>Supports date-based filtering in management interfaces</li>
     *   <li>Provides audit trail visibility across URL collections</li>
     * </ul>
     *
     * <p><strong>Display:</strong> Often formatted as relative time ("2 days ago")
     * or abbreviated date format ("Jan 15, 2024") in list UI components.</p>
     */
    private Date createdDate;

    /**
     * The total number of times the shortened URL has been accessed.
     *
     * <p>Access count providing immediate analytics visibility in list views.
     * This metric enables quick identification of popular URLs, campaign
     * performance comparison, and content engagement assessment without
     * requiring drill-down into individual URL analytics.</p>
     *
     * <p><strong>List Context Value:</strong></p>
     * <ul>
     *   <li>Enables sorting by popularity ("most clicked" views)</li>
     *   <li>Provides at-a-glance performance metrics</li>
     *   <li>Supports bulk analytics and trend identification</li>
     *   <li>Helps identify URLs that may need attention or promotion</li>
     * </ul>
     *
     * <p><strong>Display Formatting:</strong> Often abbreviated in lists
     * (e.g., "1.2K", "45M") for space efficiency while maintaining precision
     * in the underlying data.</p>
     *
     * <p><strong>Performance Indicator:</strong> Zero values may indicate new URLs
     * or URLs requiring marketing attention, while high values suggest successful
     * content or campaigns.</p>
     */
    private Long clickCount;
}
