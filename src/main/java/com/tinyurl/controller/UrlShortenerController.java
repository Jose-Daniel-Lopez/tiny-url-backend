package com.tinyurl.controller;

import com.tinyurl.DTO.ShortenUrlRequest;
import com.tinyurl.DTO.ShortenUrlResponse;
import com.tinyurl.DTO.UrlListResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.tinyurl.service.EntityNotFoundException;
import com.tinyurl.service.UrlShortenerService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class UrlShortenerController {

    private final UrlShortenerService urlService;

    public UrlShortenerController(UrlShortenerService urlService) {
        this.urlService = urlService;
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
            String shortUrl = urlService.shortenUrl(request.getOriginalUrl());
            return ResponseEntity.ok(new ShortenUrlResponse(shortUrl));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteUrl(@PathVariable String id) {
        try {
            urlService.deleteUrl(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
