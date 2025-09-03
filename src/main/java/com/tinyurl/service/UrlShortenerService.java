package com.tinyurl.service;

import com.tinyurl.DTO.UrlListResponse;
import com.tinyurl.DTO.UrlInfoResponse;
import com.tinyurl.config.UrlShortenerConfig;
import com.tinyurl.entity.UrlEntity;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.tinyurl.repository.UrlRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class UrlShortenerService {

    private final UrlRepository urlRepository;
    private final UrlShortenerConfig config;

    public UrlShortenerService(UrlRepository urlRepository,
                               UrlShortenerConfig config) {
        this.urlRepository = urlRepository;
        this.config = config;
    }

    public String shortenUrl(String originalUrl, String alias) {
        // Check if alias already exists
        if (alias == null || alias.trim().isEmpty()) {
            throw new IllegalArgumentException("Alias is required");
        }

        Optional<UrlEntity> existingAlias = urlRepository.findByAlias(alias.trim());
        if (existingAlias.isPresent()) {
            throw new IllegalArgumentException("Alias '" + alias + "' is already taken");
        }

        UrlEntity urlEntity = new UrlEntity();
        urlEntity.setOriginalUrl(originalUrl);
        urlEntity.setAlias(alias.trim());

        // Save to database
        urlRepository.save(urlEntity);

        return config.getBaseUrl() + alias.trim();
    }

    @Cacheable(value = "urls", key = "#shortCode", sync = true)
    public String getOriginalUrl(String shortCode) throws EntityNotFoundException {
        // Only search by alias now
        Optional<UrlEntity> urlEntityByAlias = urlRepository.findByAlias(shortCode);

        if (!urlEntityByAlias.isPresent()) {
            throw new EntityNotFoundException("Short URL not found");
        }

        UrlEntity entity = urlEntityByAlias.get();

        // Increment click count
        entity.setClickCount(entity.getClickCount() + 1);
        urlRepository.save(entity);

        return entity.getOriginalUrl();
    }

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

    public UrlInfoResponse getUrlInfo(String shortCode) throws EntityNotFoundException {
        // Only search by alias now
        Optional<UrlEntity> urlEntityByAlias = urlRepository.findByAlias(shortCode);

        if (!urlEntityByAlias.isPresent()) {
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

    public void deleteUrl(String id) throws EntityNotFoundException {
        if (!urlRepository.existsById(id)) {
            throw new EntityNotFoundException("URL not found with id: " + id);
        }
        urlRepository.deleteById(id);
    }
}
