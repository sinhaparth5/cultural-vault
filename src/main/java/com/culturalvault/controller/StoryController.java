/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.culturalvault.controller;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.culturalvault.model.Story;
import com.culturalvault.model.StoryFeedback;
import com.culturalvault.model.StoryGenre;
import com.culturalvault.model.StoryLength;
import com.culturalvault.repository.GenreStats;
import com.culturalvault.service.StoryService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author parth
 */
@RestController
@RequestMapping("/api/stories")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class StoryController {
    private final StoryService storyService;
    
    @GetMapping
    public ResponseEntity<Page<Story>> getAllStories(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "generatedAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        
        log.debug("GET /api/stories - Page: {}, Size: {}", page, size);
        
        Sort sort = sortDir.equalsIgnoreCase("desc") 
            ? Sort.by(sortBy).descending() 
            : Sort.by(sortBy).ascending();
        
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Story> stories = storyService.getAllStories(pageable);
        
        return ResponseEntity.ok(stories);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Story> getStoryById(@PathVariable String id) {
        log.debug("GET /api/stories/{}", id);
        
        return storyService.getStoryById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<Story> createStory(@Valid @RequestBody Story story) {
        log.info("POST /api/stories - Creating story for artifact: {}", story.getArtifactId());
        
        try {
            Story savedStory = storyService.createStory(story);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedStory);
        } catch (Exception e) {
            log.error("Error creating story: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Story> updateStory(
            @PathVariable String id,
            @Valid @RequestBody Story story) {
        
        log.info("PUT /api/stories/{}", id);
        
        try {
            Story updatedStory = storyService.updateStory(id, story);
            return ResponseEntity.ok(updatedStory);
        } catch (RuntimeException e) {
            log.error("Error updating story {}: {}", id, e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStory(@PathVariable String id) {
        log.info("DELETE /api/stories/{}", id);
        
        if (storyService.getStoryById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        storyService.deleteStory(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/artifact/{artifactId}")
    public ResponseEntity<Page<Story>> getStoriesByArtifact(
            @PathVariable String artifactId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        log.debug("GET /api/stories/artifact/{}", artifactId);
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("generatedAt").descending());
        Page<Story> stories = storyService.getStoriesByArtifact(artifactId, pageable);
        
        return ResponseEntity.ok(stories);
    }
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<Story>> getStoriesByUser(
            @PathVariable String userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        log.debug("GET /api/stories/user/{}", userId);
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("generatedAt").descending());
        Page<Story> stories = storyService.getStoriesByUser(userId, pageable);
        
        return ResponseEntity.ok(stories);
    }
    
    @GetMapping("/genre/{genre}")
    public ResponseEntity<Page<Story>> getStoriesByGenre(
            @PathVariable StoryGenre genre,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        log.debug("GET /api/stories/genre/{}", genre);
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("rating").descending());
        Page<Story> stories = storyService.getStoriesByGenre(genre, pageable);
        
        return ResponseEntity.ok(stories);
    }
    
    @GetMapping("/length/{length}")
    public ResponseEntity<Page<Story>> getStoriesByLength(
            @PathVariable StoryLength length,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        log.debug("GET /api/stories/length/{}", length);
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("rating").descending());
        Page<Story> stories = storyService.getStoriesByLength(length, pageable);
        
        return ResponseEntity.ok(stories);
    }
    
    @GetMapping("/search")
    public ResponseEntity<Page<Story>> searchStories(
            @RequestParam String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        log.debug("GET /api/stories/search?q={}", q);
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("rating").descending());
        Page<Story> stories = storyService.searchStories(q, pageable);
        
        return ResponseEntity.ok(stories);
    }
    
    @GetMapping("/top-rated")
    public ResponseEntity<Page<Story>> getTopRatedStories(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        log.debug("GET /api/stories/top-rated");
        
        Pageable pageable = PageRequest.of(page, size);
        Page<Story> stories = storyService.getTopRatedStories(pageable);
        
        return ResponseEntity.ok(stories);
    }
    
    @GetMapping("/recent")
    public ResponseEntity<Page<Story>> getRecentStories(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        log.debug("GET /api/stories/recent");
        
        Pageable pageable = PageRequest.of(page, size);
        Page<Story> stories = storyService.getRecentStories(pageable);
        
        return ResponseEntity.ok(stories);
    }
    
    @GetMapping("/popular")
    public ResponseEntity<Page<Story>> getPopularStories(
            @RequestParam(defaultValue = "5") int minRatingCount,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        log.debug("GET /api/stories/popular?minRatingCount={}", minRatingCount);
        
        Pageable pageable = PageRequest.of(page, size);
        Page<Story> stories = storyService.getPopularStories(minRatingCount, pageable);
        
        return ResponseEntity.ok(stories);
    }
    
    @GetMapping("/random")
    public ResponseEntity<List<Story>> getRandomStories(
            @RequestParam(defaultValue = "5") int count) {
        
        log.debug("GET /api/stories/random?count={}", count);
        
        List<Story> stories = storyService.getRandomStories(count);
        return ResponseEntity.ok(stories);
    }
    
    @GetMapping("/random/genre/{genre}")
    public ResponseEntity<List<Story>> getRandomStoriesByGenre(
            @PathVariable StoryGenre genre,
            @RequestParam(defaultValue = "5") int count) {
        
        log.debug("GET /api/stories/random/genre/{}?count={}", genre, count);
        
        List<Story> stories = storyService.getRandomStoriesByGenre(genre, count);
        return ResponseEntity.ok(stories);
    }
    
    @PostMapping("/{storyId}/feedback")
    public ResponseEntity<Story> addFeedback(
            @PathVariable String storyId,
            @Valid @RequestBody StoryFeedback feedback) {
        
        log.info("POST /api/stories/{}/feedback", storyId);
        
        try {
            Story updatedStory = storyService.addFeedback(storyId, feedback);
            return ResponseEntity.ok(updatedStory);
        } catch (RuntimeException e) {
            log.error("Error adding feedback to story {}: {}", storyId, e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/user/{userId}/artifact/{artifactId}")
    public ResponseEntity<List<Story>> getStoriesForUserAndArtifact(
            @PathVariable String userId,
            @PathVariable String artifactId) {
        
        log.debug("GET /api/stories/user/{}/artifact/{}", userId, artifactId);
        
        List<Story> stories = storyService.getStoriesForUserAndArtifact(userId, artifactId);
        return ResponseEntity.ok(stories);
    }
    
    @GetMapping("/user/{userId}/artifact/{artifactId}/latest")
    public ResponseEntity<Story> getLatestStoryForUserAndArtifact(
            @PathVariable String userId,
            @PathVariable String artifactId) {
        
        log.debug("GET /api/stories/user/{}/artifact/{}/latest", userId, artifactId);
        
        return storyService.getLatestStoryForUserAndArtifact(userId, artifactId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/statistics/genres")
    public ResponseEntity<List<GenreStats>> getGenreStatistics() {
        log.debug("GET /api/stories/statistics/genres");
        
        List<GenreStats> stats = storyService.getGenreStatistics();
        return ResponseEntity.ok(stats);
    }
    
    @GetMapping("/statistics/count")
    public ResponseEntity<Map<String, Long>> getStoryCounts() {
        log.debug("GET /api/stories/statistics/count");
        
        Map<String, Long> counts = Map.of(
                "total", storyService.getTotalStoryCount()
        );
        
        return ResponseEntity.ok(counts);
    }
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> healthCheck() {
        log.debug("GET /api/stories/health");
        
        long totalCount = storyService.getTotalStoryCount();
        
        Map<String, Object> health = Map.of(
                "status", "UP",
                "service", "StoryController",
                "totalStories", totalCount,
                "timestamp", System.currentTimeMillis()
        );
        
        return ResponseEntity.ok(health);
    }
}
