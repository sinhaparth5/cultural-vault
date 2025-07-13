/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.culturalvault.model;

import java.time.LocalDateTime;

/**
 *
 * @author parth
 */
public class StoryFeedback {

    private String userId;
    private int rating; // 1-5 stars
    private String comment;
    private LocalDateTime createdAt;

    // Default Constructor
    public StoryFeedback() {
        this.createdAt = LocalDateTime.now();
    }

    // Constructor
    public StoryFeedback(String userId, int rating, String comment) {
        this();
        this.userId = userId;
        this.rating = rating;
        this.comment = comment;
    }

    // Getters and Setters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
