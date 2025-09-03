package com.tinyurl.controller;

import com.tinyurl.DTO.ShortenUrlRequest;
import com.tinyurl.DTO.ShortenUrlResponse;
import com.tinyurl.DTO.UrlInfoResponse;
import com.tinyurl.DTO.UrlListResponse;
import com.tinyurl.entity.UrlEntity;
import com.tinyurl.repository.UrlRepository;
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

    @Autowired
    public UrlShortenerController(UrlShortenerService urlService, UrlRepository urlRepository) {
        this.urlService = urlService;
        this.urlRepository = urlRepository;
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
            // Only search by alias now
            Optional<UrlEntity> urlByAlias = urlRepository.findByAlias(shortCode);
            if (!urlByAlias.isPresent()) {
                return ResponseEntity.notFound().build();
            }

            urlRepository.delete(urlByAlias.get());
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
