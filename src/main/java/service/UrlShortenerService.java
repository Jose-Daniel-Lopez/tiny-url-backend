package service;

import config.UrlShortenerConfig;
import entity.UrlEntity;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import repository.UrlRepository;

import java.net.URL;
import java.util.Optional;

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

        UrlEntity urlEntity = new UrlEntity();
        Long numericId = sequenceGenerator.generateSequence();
        urlEntity.setNumericId(numericId);
        urlEntity.setOriginalUrl(originalUrl);

        // Save to database
        UrlEntity saved = urlRepository.save(urlEntity);

        // Generate short code using the numeric ID
        String shortCode = encodingService.encode(saved.getNumericId());

        return config.getBaseUrl() + shortCode;
    }

    @Cacheable(value = "urls", key = "#shortCode", sync = true)
    public String getOriginalUrl(String shortCode) throws EntityNotFoundException {
        Long numericId = encodingService.decode(shortCode);

        Optional<UrlEntity> urlEntity = urlRepository.findByNumericId(numericId);

        if (urlEntity.isEmpty()) {
            throw new EntityNotFoundException("Short URL not found");
        }

        UrlEntity entity = urlEntity.get();

        // Increment click count
        entity.setClickCount(entity.getClickCount() + 1);
        urlRepository.save(entity);

        return entity.getOriginalUrl();
    }
}

