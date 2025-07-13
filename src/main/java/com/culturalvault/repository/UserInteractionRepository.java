/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */

package com.culturalvault.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.culturalvault.model.UserInteraction;

/**
 *
 * @author parth
 */

@Repository
public interface UserInteractionRepository extends MongoRepository<UserInteraction, String> {
    
    // Find interactions by user
    List<UserInteraction> findByUserIdOrderByTimestampDesc(String userId);
    List<UserInteraction> findByUserId(String userId);
    
    // Find interactions by artifact
    List<UserInteraction> findByArtifactIdOrderByTimestampDesc(String artifactId);
    List<UserInteraction> findByArtifactId(String artifactId);
    
    // Find interactions by action
    List<UserInteraction> findByAction(String action);
    List<UserInteraction> findByActionOrderByTimestampDesc(String action);
    
    // Find specific user-artifact interaction
    Optional<UserInteraction> findByUserIdAndArtifactIdAndAction(String userId, String artifactId, String action);
    List<UserInteraction> findByUserIdAndArtifactId(String userId, String artifactId);
    
    // Find recent interactions
    List<UserInteraction> findByTimestampAfterOrderByTimestampDesc(LocalDateTime timestamp);
    List<UserInteraction> findByTimestampBetween(LocalDateTime start, LocalDateTime end);
    
    // Popular artifacts (most interactions)
    @Aggregation(pipeline = {
        "{ $group: { " +
        "    _id: '$artifactId', " +
        "    totalInteractions: { $sum: 1 }, " +
        "    uniqueUsers: { $addToSet: '$userId' }, " +
        "    actions: { $addToSet: '$action' } " +
        "} }",
        "{ $addFields: { uniqueUserCount: { $size: '$uniqueUsers' } } }",
        "{ $sort: { totalInteractions: -1 } }",
        "{ $limit: ?0 }"
    })
    List<PopularArtifact> findMostPopularArtifacts(int limit);
    
    // User engagement statistics
    @Aggregation(pipeline = {
        "{ $group: { " +
        "    _id: '$userId', " +
        "    totalInteractions: { $sum: 1 }, " +
        "    uniqueArtifacts: { $addToSet: '$artifactId' }, " +
        "    actions: { $addToSet: '$action' }, " +
        "    lastActivity: { $max: '$timestamp' } " +
        "} }",
        "{ $addFields: { uniqueArtifactCount: { $size: '$uniqueArtifacts' } } }",
        "{ $sort: { totalInteractions: -1 } }"
    })
    List<UserEngagement> getUserEngagementStats();
    
    // Count interactions
    long countByUserId(String userId);
    long countByArtifactId(String artifactId);
    long countByAction(String action);
    long countByTimestampAfter(LocalDateTime timestamp);
}