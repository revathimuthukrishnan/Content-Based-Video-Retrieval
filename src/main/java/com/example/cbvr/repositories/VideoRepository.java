package com.example.cbvr.repositories;

import com.example.cbvr.models.VideoMetadata;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VideoRepository extends MongoRepository<VideoMetadata, String> {

    // Custom query to search by caption (supports extra words and case-insensitive)
    @Query("{ 'caption' : { $regex: ?0, $options: 'i' } }")
    List<VideoMetadata> findByCaptionContainingRegex(String caption);
}

