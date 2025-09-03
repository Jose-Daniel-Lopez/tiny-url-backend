package com.tinyurl.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UrlInfoResponse {
    private String shortUrl;
    private String originalUrl;
    private String alias;
    private Date createdDate;
    private Long clickCount;
}
