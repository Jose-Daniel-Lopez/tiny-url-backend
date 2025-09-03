package com.tinyurl.service;

import org.springframework.stereotype.Service;

@Service
public class Base62EncodingService {

    private static final String ALLOWED_CHARACTERS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final int BASE = ALLOWED_CHARACTERS.length();

    public String encode(long id) {
        StringBuilder encodedString = new StringBuilder();

        while (id > 0) {
            encodedString.append(ALLOWED_CHARACTERS.charAt((int) (id % BASE)));
            id = id / BASE;
        }

        return encodedString.reverse().toString();
    }

    public long decode(String encodedString) {
        long decodedNumber = 0;

        for (int i = 0; i < encodedString.length(); i++) {
            decodedNumber = decodedNumber * BASE +
                    ALLOWED_CHARACTERS.indexOf(encodedString.charAt(i));
        }

        return decodedNumber;
    }
}
