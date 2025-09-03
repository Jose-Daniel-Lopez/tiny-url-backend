package entity;

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

    @Indexed
    private Long numericId;

    @NotBlank
    private String originalUrl;

    @CreatedDate
    private Date createdDate;

    private Long clickCount = 0L;

    @Version
    private Long version;
}