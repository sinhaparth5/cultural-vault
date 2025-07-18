/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.culturalvault.model;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 *
 * @author parth
 */
@Document(collection="stories")
public class Story {
    @Id
    private String id;
    
    @Indexed
    private String artifactId;
    
    @Indexed
    private String userId;
    
    private String title;
    private String content;
    private StoryGenre genre;
    private StoryLength length;
    
    private double rating = 0.0;
    private int ratingCount = 0;
    
    private List<StoryFeedback> feedback;
    
    // AI Generation metadata
    private GenerationParams generationParams;
    
    private LocalDateTime generatedAt;
    private LocalDateTime updatedAt;

    // Default Constructor
    public Story() {
        this.generatedAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // Constructor with essential fields
    public Story(String artifactId, String userId, String title, String content,
                StoryGenre genre, StoryLength length) {
        this();
        this.artifactId = artifactId;
        this.userId = userId;
        this.title = title;
        this.content = content;
        this.genre = genre;
        this.length = length;
    }

    // Full Constructor
    public Story(String id, String artifactId, String userId, String title, String content,
                StoryGenre genre, StoryLength length, double rating, int ratingCount,
                List<StoryFeedback> feedback, GenerationParams generationParams,
                LocalDateTime generatedAt, LocalDateTime updatedAt) {
        this.id = id;
        this.artifactId = artifactId;
        this.userId = userId;
        this.title = title;
        this.content = content;
        this.genre = genre;
        this.length = length;
        this.rating = rating;
        this.ratingCount = ratingCount;
        this.feedback = feedback;
        this.generationParams = generationParams;
        this.generatedAt = generatedAt;
        this.updatedAt = updatedAt;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getArtifactId() {
        return artifactId;
    }

    public void setArtifactId(String artifactId) {
        this.artifactId = artifactId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public StoryGenre getGenre() {
        return genre;
    }

    public void setGenre(StoryGenre genre) {
        this.genre = genre;
    }

    public StoryLength getLength() {
        return length;
    }

    public void setLength(StoryLength length) {
        this.length = length;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getRatingCount() {
        return ratingCount;
    }

    public void setRatingCount(int ratingCount) {
        this.ratingCount = ratingCount;
    }

    public List<StoryFeedback> getFeedback() {
        return feedback;
    }

    public void setFeedback(List<StoryFeedback> feedback) {
        this.feedback = feedback;
    }

    public GenerationParams getGenerationParams() {
        return generationParams;
    }

    public void setGenerationParams(GenerationParams generationParams) {
        this.generationParams = generationParams;
    }

    public LocalDateTime getGeneratedAt() {
        return generatedAt;
    }

    public void setGeneratedAt(LocalDateTime generatedAt) {
        this.generatedAt = generatedAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "Story{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", artifactId='" + artifactId + '\'' +
                ", userId='" + userId + '\'' +
                ", genre=" + genre +
                ", length=" + length +
                '}';
    }
}