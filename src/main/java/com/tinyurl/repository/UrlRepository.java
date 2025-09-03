package com.tinyurl.repository;

import com.tinyurl.entity.UrlEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface UrlRepository extends MongoRepository<UrlEntity, String> {

    Optional<UrlEntity> findByAlias(String alias);

    @Query("{ 'expiresDate' : { $lt: ?0 } }")
    List<UrlEntity> findExpiredUrls(Date currentDate);
}
