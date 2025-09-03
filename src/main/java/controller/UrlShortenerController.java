package controller;

import DTO.ShortenUrlRequest;
import DTO.ShortenUrlResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.EntityNotFoundException;
import service.UrlShortenerService;

import java.net.URI;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class UrlShortenerController {

    private final UrlShortenerService urlService;

    public UrlShortenerController(UrlShortenerService urlService) {
        this.urlService = urlService;
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
}

