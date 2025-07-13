/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.culturalvault.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.culturalvault.model.Artifact;
import com.culturalvault.service.RecommendationService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author parth
 */
@RestController
@RequestMapping("/api/recommendations")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class RecommendationController {

    private final RecommendationService recommendationService;
    
    @GetMapping("/for-me")
    public ResponseEntity<List<Artifact>> getRecommendationsForCurrentUser(
            @RequestParam(defaultValue = "6") int count) {
        
        log.debug("GET /api/recommendations/for-me?count={}", count);
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        
        // For now, we'll use a placeholder user ID - in a real app, you'd get the actual user ID
        List<Artifact> recommendations = recommendationService.getRecommendationsForUser(username, count);
        
        return ResponseEntity.ok(recommendations);
    }
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Artifact>> getRecommendationsForUser(
            @PathVariable String userId,
            @RequestParam(defaultValue = "6") int count) {
        
        log.debug("GET /api/recommendations/user/{}?count={}", userId, count);
        
        List<Artifact> recommendations = recommendationService.getRecommendationsForUser(userId, count);
        return ResponseEntity.ok(recommendations);
    }
    
    @GetMapping("/similar/{artifactId}")
    public ResponseEntity<List<Artifact>> getSimilarArtifacts(
            @PathVariable String artifactId,
            @RequestParam(defaultValue = "6") int count) {
        
        log.debug("GET /api/recommendations/similar/{}?count={}", artifactId, count);
        
        List<Artifact> similar = recommendationService.getSimilarArtifacts(artifactId, count);
        return ResponseEntity.ok(similar);
    }
    
    @GetMapping("/by-interests")
    public ResponseEntity<List<Artifact>> getArtifactsByUserInterests(
            @RequestParam(defaultValue = "6") int count) {
        
        log.debug("GET /api/recommendations/by-interests?count={}", count);
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        
        List<Artifact> artifacts = recommendationService.getArtifactsByUserInterests(username, count);
        return ResponseEntity.ok(artifacts);
    }
    
    @GetMapping("/based-on-favorites")
    public ResponseEntity<List<Artifact>> getFavoriteBasedRecommendations(
            @RequestParam(defaultValue = "6") int count) {
        
        log.debug("GET /api/recommendations/based-on-favorites?count={}", count);
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        
        List<Artifact> recommendations = recommendationService.getFavoriteBasedRecommendations(username, count);
        return ResponseEntity.ok(recommendations);
    }
    
    @GetMapping("/popular")
    public ResponseEntity<List<Artifact>> getPopularArtifacts(
            @RequestParam(defaultValue = "6") int count) {
        
        log.debug("GET /api/recommendations/popular?count={}", count);
        
        List<Artifact> popular = recommendationService.getPopularArtifacts(count);
        return ResponseEntity.ok(popular);
    }
    
    @GetMapping("/trending")
    public ResponseEntity<List<Artifact>> getTrendingArtifacts(
            @RequestParam(defaultValue = "6") int count) {
        
        log.debug("GET /api/recommendations/trending?count={}", count);
        
        List<Artifact> trending = recommendationService.getTrendingArtifacts(count);
        return ResponseEntity.ok(trending);
    }
    
    @GetMapping("/category/{category}")
    public ResponseEntity<List<Artifact>> getRecommendationsByCategory(
            @PathVariable String category,
            @RequestParam(defaultValue = "6") int count) {
        
        log.debug("GET /api/recommendations/category/{}?count={}", category, count);
        
        List<Artifact> recommendations = recommendationService.getRecommendationsByCategory(category, count);
        return ResponseEntity.ok(recommendations);
    }
    
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> healthCheck() {
        log.debug("GET /api/recommendations/health");
        
        Map<String, Object> health = Map.of(
                "status", "UP",
                "service", "RecommendationController",
                "timestamp", System.currentTimeMillis()
        );
        
        return ResponseEntity.ok(health);
    }
}
