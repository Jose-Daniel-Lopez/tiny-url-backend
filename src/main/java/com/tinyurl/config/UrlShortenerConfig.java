package com.tinyurl.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "url-shortener")
@Data
public class UrlShortenerConfig {
    private String allowedCharacters;
    private int keyLength;
    private String baseUrl;
}

