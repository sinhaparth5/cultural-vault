/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */

package com.culturalvault.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.culturalvault.model.Story;
import com.culturalvault.model.StoryGenre;
import com.culturalvault.model.StoryLength;

/**
 *
 * @author parth
 */
@Repository
public interface StoryRepository extends MongoRepository<Story, String> {
        // Find stories by artifact
    Page<Story> findByArtifactId(String artifactId, Pageable pageable);
    List<Story> findByArtifactId(String artifactId);
    
    // Find stories by user
    Page<Story> findByUserId(String userId, Pageable pageable);
    List<Story> findByUserId(String userId);
    
    // Find by genre
    Page<Story> findByGenre(StoryGenre genre, Pageable pageable);
    
    // Find by length
    Page<Story> findByLength(StoryLength length, Pageable pageable);
    
    // Find by rating range
    Page<Story> findByRatingGreaterThanEqual(double minRating, Pageable pageable);
    Page<Story> findByRatingBetween(double minRating, double maxRating, Pageable pageable);
    
    // Top rated stories
    @Query(value = "{}", sort = "{ rating: -1, ratingCount: -1 }")
    Page<Story> findTopRatedStories(Pageable pageable);
    
    // Recent stories
    @Query(value = "{}", sort = "{ generatedAt: -1 }")
    Page<Story> findRecentStories(Pageable pageable);
    
    // Popular stories (high rating count)
    @Query(value = "{ ratingCount: { $gte: ?0 } }", sort = "{ ratingCount: -1, rating: -1 }")
    Page<Story> findPopularStories(int minRatingCount, Pageable pageable);
    
    // Find stories by multiple criteria
    @Query("{ $and: [ " +
           "{ $or: [ { genre: ?0 }, { $expr: { $eq: [?0, null] } } ] }, " +
           "{ $or: [ { length: ?1 }, { $expr: { $eq: [?1, null] } } ] }, " +
           "{ rating: { $gte: ?2 } } " +
           "] }")
    Page<Story> findStoriesByCriteria(StoryGenre genre, StoryLength length, double minRating, Pageable pageable);
    
    // Find stories for specific artifact and user - FIXED: Only one method now
    List<Story> findByArtifactIdAndUserId(String artifactId, String userId);
    
    // Find the most recent story for artifact and user
    @Query(value = "{ artifactId: ?0, userId: ?1 }", sort = "{ generatedAt: -1 }")
    Optional<Story> findLatestStoryByArtifactIdAndUserId(String artifactId, String userId);
    
    // Find stories by generation date range
    List<Story> findByGeneratedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    // Search stories by title or content
    @Query("{ $or: [ " +
           "{ title: { $regex: ?0, $options: 'i' } }, " +
           "{ content: { $regex: ?0, $options: 'i' } } " +
           "] }")
    Page<Story> searchStories(String searchTerm, Pageable pageable);
    
    // Random stories
    @Aggregation(pipeline = {
        "{ $sample: { size: ?0 } }"
    })
    List<Story> findRandomStories(int count);
    
    // Random stories by genre - FIXED: Use StoryGenre enum properly
    @Aggregation(pipeline = {
        "{ $match: { genre: ?0 } }",
        "{ $sample: { size: ?1 } }"
    })
    List<Story> findRandomStoriesByGenre(StoryGenre genre, int count);
    
    // Stories with feedback
    @Query("{ 'feedback': { $exists: true, $not: { $size: 0 } } }")
    Page<Story> findStoriesWithFeedback(Pageable pageable);
    
    // Stories without feedback
    @Query("{ $or: [ { 'feedback': { $exists: false } }, { 'feedback': { $size: 0 } } ] }")
    Page<Story> findStoriesWithoutFeedback(Pageable pageable);
    
    // Count queries
    long countByArtifactId(String artifactId);
    long countByUserId(String userId);
    long countByGenre(StoryGenre genre);
    long countByLength(StoryLength length);
    long countByRatingGreaterThanEqual(double minRating);
    
    // Statistics aggregation
    @Aggregation(pipeline = {
        "{ $group: { " +
        "    _id: '$genre', " +
        "    count: { $sum: 1 }, " +
        "    avgRating: { $avg: '$rating' }, " +
        "    totalRatings: { $sum: '$ratingCount' } " +
        "} }",
        "{ $sort: { count: -1 } }"
    })
    List<GenreStats> getGenreStatistics();
    
    // User story statistics
    @Aggregation(pipeline = {
        "{ $group: { " +
        "    _id: '$userId', " +
        "    storyCount: { $sum: 1 }, " +
        "    avgRating: { $avg: '$rating' }, " +
        "    totalRatings: { $sum: '$ratingCount' }, " +
        "    genres: { $addToSet: '$genre' } " +
        "} }",
        "{ $sort: { storyCount: -1 } }"
    })
    List<UserStoryStats> getUserStoryStatistics();
    
    // Artifact story statistics
    @Aggregation(pipeline = {
        "{ $group: { " +
        "    _id: '$artifactId', " +
        "    storyCount: { $sum: 1 }, " +
        "    avgRating: { $avg: '$rating' }, " +
        "    genres: { $addToSet: '$genre' }, " +
        "    lengths: { $addToSet: '$length' } " +
        "} }",
        "{ $sort: { storyCount: -1 } }"
    })
    List<ArtifactStoryStats> getArtifactStoryStatistics();
}