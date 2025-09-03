package com.tinyurl.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UrlListResponse {
    private String shortUrl;
    private String originalUrl;
    private Date createdDate;
    private Long clickCount;
}
