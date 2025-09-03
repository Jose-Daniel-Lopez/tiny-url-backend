package com.tinyurl.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShortenUrlRequest {
    @NotBlank(message = "URL cannot be blank")
    @URL(message = "Invalid URL format")
    private String originalUrl;

    @Pattern(regexp = "^[a-zA-Z0-9_-]*$", message = "Alias can only contain letters, numbers, hyphens, and underscores")
    private String alias;
}
