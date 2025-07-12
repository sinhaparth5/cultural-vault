/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */

package com.culturalvault.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author parth
 */
@Document(collection = "user_interaction")
class UserInteraction {
    @Id
    private String id;
    
    @Indexed
    private String userId;
    
    @Indexed
    private String artifactId;
    
    private String action; // VIEW, LIKE, SHARE, SAVE, FAVORITE
    private LocalDateTime timestamp;
    private String sessionId;
    private String ipAddress;
    private String userAgent;
    
    // Constructors
    public UserInteraction() {
        this.timestamp = LocalDateTime.now();
    }
    
    public UserInteraction(String userId, String artifactId, String action) {
        this();
        this.userId = userId;
        this.artifactId = artifactId;
        this.action = action;
    }
    
    // Full constructor
    public UserInteraction(String id, String userId, String artifactId, String action,
                          LocalDateTime timestamp, String sessionId, String ipAddress, String userAgent) {
        this.id = id;
        this.userId = userId;
        this.artifactId = artifactId;
        this.action = action;
        this.timestamp = timestamp;
        this.sessionId = sessionId;
        this.ipAddress = ipAddress;
        this.userAgent = userAgent;
    }
    
    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    
    public String getArtifactId() { return artifactId; }
    public void setArtifactId(String artifactId) { this.artifactId = artifactId; }
    
    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }
    
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    
    public String getSessionId() { return sessionId; }
    public void setSessionId(String sessionId) { this.sessionId = sessionId; }
    
    public String getIpAddress() { return ipAddress; }
    public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }
    
    public String getUserAgent() { return userAgent; }
    public void setUserAgent(String userAgent) { this.userAgent = userAgent; }
}

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

// DTOs for aggregation results
interface PopularArtifact {
    String getId(); // artifactId
    int getTotalInteractions();
    List<String> getUniqueUsers();
    int getUniqueUserCount();
    List<String> getActions();
}

interface UserEngagement {
    String getId(); // userId
    int getTotalInteractions();
    List<String> getUniqueArtifacts();
    int getUniqueArtifactCount();
    List<String> getActions();
    LocalDateTime getLastActivity();
}