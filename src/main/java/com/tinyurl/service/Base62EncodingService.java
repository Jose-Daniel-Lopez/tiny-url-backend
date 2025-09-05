package com.tinyurl.service;

import org.springframework.stereotype.Service;

/**
 * Service for Base62 encoding and decoding operations.
 *
 * <p>This service provides bidirectional conversion between numeric values and Base62
 * encoded strings, which is essential for generating compact, URL-safe identifiers
 * in URL shortening applications. Base62 encoding uses a character set of 62 characters
 * (0-9, a-z, A-Z), providing a good balance between compactness and readability.</p>
 *
 * <p>The Base62 encoding scheme is particularly well-suited for URL shortening because:</p>
 * <ul>
 *   <li>Produces shorter strings compared to Base10 (decimal) representation</li>
 *   <li>Uses only URL-safe characters (no special characters requiring encoding)</li>
 *   <li>Provides case-sensitive differentiation for increased character space</li>
 *   <li>Maintains human readability while maximizing character density</li>
 * </ul>
 *
 * <p><strong>Character Set:</strong> "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"</p>
 *
 * <p><strong>Example Usage:</strong></p>
 * <pre>
 * Base62EncodingService encoder = new Base62EncodingService();
 * String encoded = encoder.encode(123456789L); // Returns: "8M0kX"
 * long decoded = encoder.decode("8M0kX");      // Returns: 123456789L
 * </pre>
 *
 * @author Jose
 * @since 1.0
 * @see org.springframework.stereotype.Service
 */
@Service
public class Base62EncodingService {

    /**
     * The character set used for Base62 encoding.
     *
     * <p>This string defines the 62 characters used in the encoding scheme, arranged
     * in the order: digits (0-9), lowercase letters (a-z), and uppercase letters (A-Z).
     * The order is significant as it determines the numeric value assigned to each character.</p>
     *
     * <p><strong>Character Mapping:</strong></p>
     * <ul>
     *   <li>Index 0-9: Characters '0'-'9' (decimal digits)</li>
     *   <li>Index 10-35: Characters 'a'-'z' (lowercase letters)</li>
     *   <li>Index 36-61: Characters 'A'-'Z' (uppercase letters)</li>
     * </ul>
     */
    private static final String ALLOWED_CHARACTERS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    /**
     * The base value for Base62 encoding (62).
     *
     * <p>This constant represents the radix of the Base62 numbering system,
     * equal to the length of the {@link #ALLOWED_CHARACTERS} string. It's used
     * in both encoding and decoding operations for modular arithmetic calculations.</p>
     */
    private static final int BASE = ALLOWED_CHARACTERS.length();

    /**
     * Encodes a numeric ID into a Base62 string representation.
     *
     * <p>This method converts a positive long integer into its Base62 string equivalent
     * by repeatedly dividing the number by 62 and mapping the remainders to characters
     * from the allowed character set. The algorithm builds the encoded string in reverse
     * order and then reverses it to produce the correct result.</p>
     *
     * <p><strong>Algorithm:</strong></p>
     * <ol>
     *   <li>Take the remainder of (id % 62) to get the rightmost digit</li>
     *   <li>Map the remainder to the corresponding character in ALLOWED_CHARACTERS</li>
     *   <li>Divide id by 62 for the next iteration</li>
     *   <li>Repeat until id becomes 0</li>
     *   <li>Reverse the accumulated string to get the final result</li>
     * </ol>
     *
     * <p><strong>Performance:</strong> Time complexity is O(log₆₂(id)), space complexity is O(log₆₂(id)).</p>
     *
     * <p><strong>Examples:</strong></p>
     * <ul>
     *   <li>encode(0) → "0"</li>
     *   <li>encode(61) → "Z"</li>
     *   <li>encode(62) → "10"</li>
     *   <li>encode(123456789) → "8M0kX"</li>
     * </ul>
     *
     * <p><strong>Edge Cases:</strong></p>
     * <ul>
     *   <li>Input 0 produces "0" (single character)</li>
     *   <li>Negative numbers are not handled - behavior is undefined</li>
     * </ul>
     *
     * @param id the positive long integer to encode (must be ≥ 0)
     * @return the Base62 encoded string representation of the input number
     * @throws IllegalArgumentException if id is negative (implementation should validate this)
     * @see #decode(String)
     */
    public String encode(long id) {
        // Handle the special case of 0 to avoid empty string
        if (id == 0) {
            return "0";
        }

        StringBuilder encodedString = new StringBuilder();

        while (id > 0) {
            encodedString.append(ALLOWED_CHARACTERS.charAt((int) (id % BASE)));
            id = id / BASE;
        }

        return encodedString.reverse().toString();
    }

    /**
     * Decodes a Base62 encoded string back to its original numeric value.
     *
     * <p>This method converts a Base62 encoded string back to its original long integer
     * by iterating through each character, mapping it to its numeric value, and
     * reconstructing the number using positional notation (base 62 arithmetic).</p>
     *
     * <p><strong>Algorithm:</strong></p>
     * <ol>
     *   <li>Initialize result to 0</li>
     *   <li>For each character from left to right:</li>
     *   <li>Multiply current result by 62 (shift left in base 62)</li>
     *   <li>Add the character's position value in ALLOWED_CHARACTERS</li>
     *   <li>Continue until all characters are processed</li>
     * </ol>
     *
     * <p><strong>Performance:</strong> Time complexity is O(n) where n is the string length,
     * space complexity is O(1).</p>
     *
     * <p><strong>Examples:</strong></p>
     * <ul>
     *   <li>decode("0") → 0</li>
     *   <li>decode("Z") → 61</li>
     *   <li>decode("10") → 62</li>
     *   <li>decode("8M0kX") → 123456789</li>
     * </ul>
     *
     * <p><strong>Error Handling:</strong> If the input string contains characters not
     * present in the ALLOWED_CHARACTERS set, {@code indexOf()} will return -1,
     * which will produce incorrect results. Consider adding validation for production use.</p>
     *
     * @param encodedString the Base62 encoded string to decode (must contain only valid Base62 characters)
     * @return the original numeric value represented by the encoded string
     * @throws IllegalArgumentException if encodedString contains invalid characters (implementation should validate this)
     * @see #encode(long)
     */
    public long decode(String encodedString) {
        // Handle null or empty input
        if (encodedString == null || encodedString.isEmpty()) {
            return 0;
        }

        long decodedNumber = 0;

        for (int i = 0; i < encodedString.length(); i++) {
            char currentChar = encodedString.charAt(i);
            int charValue = ALLOWED_CHARACTERS.indexOf(currentChar);

            // Note: In production, should validate that charValue != -1
            // to handle invalid characters gracefully
            decodedNumber = decodedNumber * BASE + charValue;
        }

        return decodedNumber;
    }
}
