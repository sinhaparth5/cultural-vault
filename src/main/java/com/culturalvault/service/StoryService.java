/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.culturalvault.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.culturalvault.model.GenerationParams;
import com.culturalvault.model.Story;
import com.culturalvault.model.StoryFeedback;
import com.culturalvault.model.StoryGenre;
import com.culturalvault.model.StoryLength;
import com.culturalvault.repository.GenreStats;
import com.culturalvault.repository.StoryRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author parth
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class StoryService  {
    private final StoryRepository storyRepository;
    public Story createStory(Story story) {
        log.info("Creating new story for artifact: {}", story.getArtifactId());
        
        // Set timestamps
        story.setGeneratedAt(LocalDateTime.now());
        story.setUpdatedAt(LocalDateTime.now());
        
        return storyRepository.save(story);
    }
    
    public Optional<Story> getStoryById(String id) {
        log.debug("Fetching story by id: {}", id);
        return storyRepository.findById(id);
    }
    
    public Story updateStory(String id, Story updatedStory) {
        log.info("Updating story with id: {}", id);
        
        return storyRepository.findById(id)
                .map(story -> {
                    story.setTitle(updatedStory.getTitle());
                    story.setContent(updatedStory.getContent());
                    story.setGenre(updatedStory.getGenre());
                    story.setLength(updatedStory.getLength());
                    story.setUpdatedAt(LocalDateTime.now());
                    
                    return storyRepository.save(story);
                })
                .orElseThrow(() -> new RuntimeException("Story not found with id: " + id));
    }
    
    public void deleteStory(String id) {
        log.info("Deleting story with id: {}", id);
        storyRepository.deleteById(id);
    }
    
    public Page<Story> getAllStories(Pageable pageable) {
        log.debug("Fetching all stories with pagination");
        return storyRepository.findAll(pageable);
    }
    
    public Page<Story> getStoriesByArtifact(String artifactId, Pageable pageable) {
        log.debug("Fetching stories for artifact: {}", artifactId);
        return storyRepository.findByArtifactId(artifactId, pageable);
    }
    
    public Page<Story> getStoriesByUser(String userId, Pageable pageable) {
        log.debug("Fetching stories by user: {}", userId);
        return storyRepository.findByUserId(userId, pageable);
    }
    
    public List<Story> getStoriesForUserAndArtifact(String userId, String artifactId) {
        log.debug("Fetching stories for user {} and artifact {}", userId, artifactId);
        return storyRepository.findByArtifactIdAndUserId(artifactId, userId);
    }
    
    public Optional<Story> getLatestStoryForUserAndArtifact(String userId, String artifactId) {
        log.debug("Fetching latest story for user {} and artifact {}", userId, artifactId);
        return storyRepository.findLatestStoryByArtifactIdAndUserId(artifactId, userId);
    }
    
    public Page<Story> getStoriesByGenre(StoryGenre genre, Pageable pageable) {
        log.debug("Fetching stories by genre: {}", genre);
        return storyRepository.findByGenre(genre, pageable);
    }
    
    public Page<Story> getStoriesByLength(StoryLength length, Pageable pageable) {
        log.debug("Fetching stories by length: {}", length);
        return storyRepository.findByLength(length, pageable);
    }
    
    public Page<Story> getStoriesByCriteria(StoryGenre genre, StoryLength length, double minRating, Pageable pageable) {
        log.debug("Fetching stories by criteria - Genre: {}, Length: {}, MinRating: {}", genre, length, minRating);
        return storyRepository.findStoriesByCriteria(genre, length, minRating, pageable);
    }
    
    public Page<Story> searchStories(String searchTerm, Pageable pageable) {
        log.debug("Searching stories with term: {}", searchTerm);
        return storyRepository.searchStories(searchTerm, pageable);
    }
    
    public Page<Story> getTopRatedStories(Pageable pageable) {
        log.debug("Fetching top rated stories");
        return storyRepository.findTopRatedStories(pageable);
    }
    
    public Page<Story> getRecentStories(Pageable pageable) {
        log.debug("Fetching recent stories");
        return storyRepository.findRecentStories(pageable);
    }
    
    public Page<Story> getPopularStories(int minRatingCount, Pageable pageable) {
        log.debug("Fetching popular stories with min rating count: {}", minRatingCount);
        return storyRepository.findPopularStories(minRatingCount, pageable);
    }
    
    public List<Story> getRandomStories(int count) {
        log.debug("Fetching {} random stories", count);
        return storyRepository.findRandomStories(Math.min(count, 20));
    }
    
    public List<Story> getRandomStoriesByGenre(StoryGenre genre, int count) {
        log.debug("Fetching {} random stories from genre: {}", count, genre);
        return storyRepository.findRandomStoriesByGenre(genre, Math.min(count, 10));
    }
    
    public Story addFeedback(String storyId, StoryFeedback feedback) {
        log.info("Adding feedback to story: {}", storyId);
        
        return storyRepository.findById(storyId)
                .map(story -> {
                    if (story.getFeedback() == null) {
                        story.setFeedback(List.of(feedback));
                    } else {
                        story.getFeedback().add(feedback);
                    }
                    
                    // Recalculate rating
                    updateStoryRating(story);
                    story.setUpdatedAt(LocalDateTime.now());
                    
                    return storyRepository.save(story);
                })
                .orElseThrow(() -> new RuntimeException("Story not found with id: " + storyId));
    }
    
    private void updateStoryRating(Story story) {
        if (story.getFeedback() != null && !story.getFeedback().isEmpty()) {
            double averageRating = story.getFeedback().stream()
                    .mapToInt(StoryFeedback::getRating)
                    .average()
                    .orElse(0.0);
            
            story.setRating(averageRating);
            story.setRatingCount(story.getFeedback().size());
        }
    }
    
    public Page<Story> getStoriesWithFeedback(Pageable pageable) {
        log.debug("Fetching stories with feedback");
        return storyRepository.findStoriesWithFeedback(pageable);
    }
    
    public Page<Story> getStoriesWithoutFeedback(Pageable pageable) {
        log.debug("Fetching stories without feedback");
        return storyRepository.findStoriesWithoutFeedback(pageable);
    }
    
    public long getTotalStoryCount() {
        return storyRepository.count();
    }
    
    public long getStoryCountByArtifact(String artifactId) {
        return storyRepository.countByArtifactId(artifactId);
    }
    
    public long getStoryCountByUser(String userId) {
        return storyRepository.countByUserId(userId);
    }
    
    public long getStoryCountByGenre(StoryGenre genre) {
        return storyRepository.countByGenre(genre);
    }
    
    public List<GenreStats> getGenreStatistics() {
        log.debug("Fetching genre statistics");
        return storyRepository.getGenreStatistics();
    }
    
    public Story generateStoryForArtifact(String artifactId, String userId, StoryGenre genre, 
                                        StoryLength length, GenerationParams params) {
        log.info("Generating story for artifact: {} by user: {}", artifactId, userId);
        
        // Create new story
        Story story = new Story();
        story.setArtifactId(artifactId);
        story.setUserId(userId);
        story.setGenre(genre);
        story.setLength(length);
        story.setGenerationParams(params);
        story.setTitle("Generated Story"); // Will be updated by AI service
        story.setContent("Story content will be generated..."); // Will be updated by AI service
        
        return createStory(story);
    }
    
    public Story updateGeneratedStory(String storyId, String title, String content) {
        log.info("Updating generated story content for id: {}", storyId);
        
        return storyRepository.findById(storyId)
                .map(story -> {
                    story.setTitle(title);
                    story.setContent(content);
                    story.setUpdatedAt(LocalDateTime.now());
                    return storyRepository.save(story);
                })
                .orElseThrow(() -> new RuntimeException("Story not found with id: " + storyId));
    }
    
    public boolean hasUserGeneratedStoryForArtifact(String userId, String artifactId) {
        return !storyRepository.findByArtifactIdAndUserId(artifactId, userId).isEmpty();
    }
}
