package com.tinyurl.controller;

import com.tinyurl.DTO.ShortenUrlRequest;
import com.tinyurl.DTO.ShortenUrlResponse;
import com.tinyurl.DTO.UrlInfoResponse;
import com.tinyurl.DTO.UrlListResponse;
import com.tinyurl.entity.UrlEntity;
import com.tinyurl.repository.UrlRepository;
import com.tinyurl.service.EntityNotFoundException;
import com.tinyurl.service.UrlShortenerService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;
import java.util.Optional;

/**
 * REST Controller for URL shortening operations.
 *
 * <p>This controller provides RESTful endpoints for the TinyURL service, enabling clients
 * to shorten URLs, retrieve URL information, redirect to original URLs, and manage
 * existing shortened URLs. All endpoints return JSON responses except for the redirect
 * endpoint which performs HTTP redirection.</p>
 *
 * <p><strong>API Endpoints:</strong></p>
 * <ul>
 *   <li>POST /api/shorten - Create a new shortened URL</li>
 *   <li>GET /api/urls - Retrieve all URLs</li>
 *   <li>GET /api/urls/{shortCode} - Get detailed URL information</li>
 *   <li>GET /{shortCode} - Redirect to original URL</li>
 *   <li>DELETE /api/urls/{shortCode} - Delete a shortened URL</li>
 * </ul>
 *
 * <p><strong>Security Note:</strong> The current CORS configuration allows all origins (*).
 * Consider restricting this in production environments for better security.</p>
 *
 * @author Jose
 * @since 1.0
 * @see UrlShortenerService
 * @see UrlRepository
 */
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class UrlShortenerController {

    private final UrlShortenerService urlService;
    private final UrlRepository urlRepository;

    /**
     * Constructs a new UrlShortenerController with required dependencies.
     *
     * @param urlService the service for URL shortening operations
     * @param urlRepository the repository for direct URL data access
     */
    @Autowired
    public UrlShortenerController(UrlShortenerService urlService, UrlRepository urlRepository) {
        this.urlService = urlService;
        this.urlRepository = urlRepository;
    }

    /**
     * Retrieves all shortened URLs in the system.
     *
     * <p>This endpoint returns a list of all URLs currently stored in the system,
     * providing a comprehensive view of all shortened URLs. The response includes
     * basic information about each URL without detailed statistics.</p>
     *
     * @return {@link ResponseEntity} containing a list of {@link UrlListResponse} objects
     *         with HTTP 200 OK status, or HTTP 500 if an internal error occurs
     */
    @GetMapping("/urls")
    public ResponseEntity<List<UrlListResponse>> getAllUrls() {
        try {
            List<UrlListResponse> urls = urlService.getAllUrls();
            return ResponseEntity.ok(urls);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Retrieves detailed information for a specific shortened URL.
     *
     * <p>This endpoint provides comprehensive information about a shortened URL
     * including creation date, access statistics, and other metadata. The short code
     * parameter corresponds to the unique identifier of the shortened URL.</p>
     *
     * @param shortCode the unique identifier of the shortened URL
     * @return {@link ResponseEntity} containing {@link UrlInfoResponse} with detailed URL information
     *         and HTTP 200 OK status, HTTP 404 if URL not found, or HTTP 500 for internal errors
     */
    @GetMapping("/urls/{shortCode}")
    public ResponseEntity<UrlInfoResponse> getUrlInfo(@PathVariable String shortCode) {
        try {
            UrlInfoResponse urlInfo = urlService.getUrlInfo(shortCode);
            return ResponseEntity.ok(urlInfo);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Redirects the client to the original URL associated with the short code.
     *
     * <p>This is the core redirection endpoint that performs the URL shortening service's
     * primary function. When accessed, it looks up the original URL and returns an HTTP 302
     * Found response with the Location header set to the original URL, causing browsers
     * to automatically redirect.</p>
     *
     * <p><strong>Note:</strong> This endpoint is mapped without the "/api" prefix to provide
     * clean short URLs (e.g., "<a href="https://domain.com/abc123">...</a>" instead of "https://domain.com/api/abc123").</p>
     *
     * @param shortCode the unique identifier of the shortened URL
     * @return {@link ResponseEntity} with HTTP 302 Found status and Location header set to original URL,
     *         or HTTP 404 if the short code is not found
     */
    @GetMapping("/{shortCode}")
    public ResponseEntity<Void> redirectToOriginalUrl(@PathVariable String shortCode) {
        try {
            String originalUrl = urlService.getOriginalUrl(shortCode);

            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(URI.create(originalUrl));

            return new ResponseEntity<>(headers, HttpStatus.FOUND);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Creates a new shortened URL from the provided original URL.
     *
     * <p>This endpoint accepts a URL shortening request and generates a new shortened URL.
     * The request can optionally include a custom alias; if not provided, a random
     * short code will be generated. The response contains the complete shortened URL.</p>
     *
     * <p><strong>Request Validation:</strong> The request body is validated using JSR-380
     * annotations to ensure URL format correctness and required fields are present.</p>
     *
     * @param request the {@link ShortenUrlRequest} containing the original URL and optional alias
     * @return {@link ResponseEntity} containing {@link ShortenUrlResponse} with the shortened URL
     *         and HTTP 200 OK status, HTTP 400 for invalid requests, or HTTP 500 for internal errors
     */
    @PostMapping("/shorten")
    public ResponseEntity<ShortenUrlResponse> shortenUrl(@Valid @RequestBody ShortenUrlRequest request) {
        try {
            String shortUrl = urlService.shortenUrl(request.getOriginalUrl(), request.getAlias());
            return ResponseEntity.ok(new ShortenUrlResponse(shortUrl));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Deletes a shortened URL from the system.
     *
     * <p>This endpoint removes a shortened URL identified by its short code or alias.
     * Once deleted, the shortened URL will no longer be accessible and attempts to
     * access it will result in HTTP 404 Not Found responses.</p>
     *
     * <p><strong>Implementation Note:</strong> Currently searches by alias only. Consider
     * expanding the search logic to include both short code and alias for better flexibility.</p>
     *
     * <p><strong>Caution:</strong> This operation is irreversible. Deleted URLs cannot be recovered.</p>
     *
     * @param shortCode the unique identifier (alias) of the shortened URL to delete
     * @return {@link ResponseEntity} with HTTP 204 No Content status on successful deletion,
     *         HTTP 404 if URL not found, or HTTP 500 for internal errors
     */
    @DeleteMapping("/urls/{shortCode}")
    public ResponseEntity<Void> deleteUrl(@PathVariable String shortCode) {
        try {
            // Only search by alias now
            Optional<UrlEntity> urlByAlias = urlRepository.findByAlias(shortCode);
            if (urlByAlias.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            urlRepository.delete(urlByAlias.get());
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
