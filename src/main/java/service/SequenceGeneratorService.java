package service;

import entity.UrlEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

@Service
public class SequenceGeneratorService {

    private final MongoTemplate mongoTemplate;

    public SequenceGeneratorService(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public Long generateSequence() {
        Query query = new Query().with(Sort.by(Sort.Direction.DESC, "numericId")).limit(1);
        UrlEntity lastUrl = mongoTemplate.findOne(query, UrlEntity.class);

        return (lastUrl != null) ? lastUrl.getNumericId() + 1 : 1L;
    }
}

