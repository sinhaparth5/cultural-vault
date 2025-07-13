/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.culturalvault.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 *
 * @author parth
 */
@Document(collection = "user_interaction")
public class UserInteraction {

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
