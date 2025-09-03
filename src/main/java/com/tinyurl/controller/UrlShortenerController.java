package com.tinyurl.controller;

import com.tinyurl.DTO.ShortenUrlRequest;
import com.tinyurl.DTO.ShortenUrlResponse;
import com.tinyurl.DTO.UrlListResponse;
import com.tinyurl.entity.UrlEntity;
import com.tinyurl.repository.UrlRepository;
import com.tinyurl.service.Base62EncodingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.tinyurl.service.EntityNotFoundException;
import com.tinyurl.service.UrlShortenerService;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class UrlShortenerController {

    private final UrlShortenerService urlService;
    private final UrlRepository urlRepository;
    private final Base62EncodingService encodingService;

    @Autowired
    public UrlShortenerController(UrlShortenerService urlService, UrlRepository urlRepository, Base62EncodingService encodingService) {
        this.urlService = urlService;
        this.urlRepository = urlRepository;
        this.encodingService = encodingService;
    }

    @GetMapping("/urls")
    public ResponseEntity<List<UrlListResponse>> getAllUrls() {
        try {
            List<UrlListResponse> urls = urlService.getAllUrls();
            return ResponseEntity.ok(urls);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

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

    @DeleteMapping("/urls/{shortCode}")
    public ResponseEntity<Void> deleteUrl(@PathVariable String shortCode) {
        try {
            UrlEntity urlEntity = null;

            // First try to find by alias
            Optional<UrlEntity> urlByAlias = urlRepository.findByAlias(shortCode);
            if (urlByAlias.isPresent()) {
                urlEntity = urlByAlias.get();
            } else {
                // If not found by alias, try to decode as numeric ID
                try {
                    Long numericId = encodingService.decode(shortCode);
                    Optional<UrlEntity> urlByNumeric = urlRepository.findByNumericId(numericId);
                    if (urlByNumeric.isPresent()) {
                        urlEntity = urlByNumeric.get();
                    }
                } catch (Exception e) {
                    // Invalid encoding, urlEntity remains null
                }
            }

            if (urlEntity == null) {
                return ResponseEntity.notFound().build();
            }

            urlRepository.delete(urlEntity);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
