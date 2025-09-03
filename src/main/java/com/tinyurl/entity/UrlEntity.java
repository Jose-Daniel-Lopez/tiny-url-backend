package com.tinyurl.entity;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "urls")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UrlEntity {

    @Id
    private String id;

    @NotBlank
    private String originalUrl;

    @Indexed(unique = true)
    private String alias;

    @CreatedDate
    private Date createdDate;

    private Long clickCount = 0L;

    @Version
    private Long version;

    public boolean isEmpty() {
        return (originalUrl == null || originalUrl.trim().isEmpty());
    }
}