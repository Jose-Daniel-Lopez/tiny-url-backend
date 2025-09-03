package com.tinyurl.service;

import com.tinyurl.DTO.UrlListResponse;
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
    private final Base62EncodingService encodingService;
    private final SequenceGeneratorService sequenceGenerator;
    private final UrlShortenerConfig config;

    public UrlShortenerService(UrlRepository urlRepository,
                               Base62EncodingService encodingService,
                                 SequenceGeneratorService sequenceGenerator,
                               UrlShortenerConfig config) {
        this.urlRepository = urlRepository;
        this.encodingService = encodingService;
        this.sequenceGenerator = sequenceGenerator;
        this.config = config;
    }

    public String shortenUrl(String originalUrl) {
        return shortenUrl(originalUrl, null);
    }

    public String shortenUrl(String originalUrl, String alias) {
        // Check if alias is provided and already exists
        if (alias != null && !alias.trim().isEmpty()) {
            Optional<UrlEntity> existingAlias = urlRepository.findByAlias(alias);
            if (existingAlias.isPresent()) {
                throw new IllegalArgumentException("Alias '" + alias + "' is already taken");
            }
        }

        UrlEntity urlEntity = new UrlEntity();
        Long numericId = sequenceGenerator.generateSequence();
        urlEntity.setNumericId(numericId);
        urlEntity.setOriginalUrl(originalUrl);

        // Set alias if provided
        if (alias != null && !alias.trim().isEmpty()) {
            urlEntity.setAlias(alias.trim());
        }

        // Save to database
        UrlEntity saved = urlRepository.save(urlEntity);

        // Generate short code - use alias if available, otherwise use numeric ID
        String shortCode = saved.getAlias() != null ? saved.getAlias() : encodingService.encode(saved.getNumericId());

        return config.getBaseUrl() + shortCode;
    }

    @Cacheable(value = "urls", key = "#shortCode", sync = true)
    public String getOriginalUrl(String shortCode) throws EntityNotFoundException {
        UrlEntity entity = null;

        // First try to find by alias
        Optional<UrlEntity> urlEntityByAlias = urlRepository.findByAlias(shortCode);
        if (urlEntityByAlias.isPresent()) {
            entity = urlEntityByAlias.get();
        } else {
            // If not found by alias, try to decode as numeric ID
            try {
                Long numericId = encodingService.decode(shortCode);
                Optional<UrlEntity> urlEntityByNumeric = urlRepository.findByNumericId(numericId);
                if (urlEntityByNumeric.isPresent()) {
                    entity = urlEntityByNumeric.get();
                }
            } catch (Exception e) {
                // Invalid encoding, entity remains null
            }
        }

        if (entity == null) {
            throw new EntityNotFoundException("Short URL not found");
        }

        // Increment click count
        entity.setClickCount(entity.getClickCount() + 1);
        urlRepository.save(entity);

        return entity.getOriginalUrl();
    }

    public List<UrlListResponse> getAllUrls() {
        List<UrlEntity> allUrls = urlRepository.findAll();

        return allUrls.stream()
                .map(entity -> {
                    // Use alias if available, otherwise use encoded numeric ID
                    String shortCode = entity.getAlias() != null ? entity.getAlias() : encodingService.encode(entity.getNumericId());
                    String shortUrl = config.getBaseUrl() + shortCode;
                    return new UrlListResponse(
                            shortUrl,
                            entity.getOriginalUrl(),
                            entity.getCreatedDate(),
                            entity.getClickCount()
                    );
                })
                .collect(Collectors.toList());
    }

    public void deleteUrl(String id) throws EntityNotFoundException {
        if (!urlRepository.existsById(id)) {
            throw new EntityNotFoundException("URL not found with id: " + id);
        }
        urlRepository.deleteById(id);
    }
}
