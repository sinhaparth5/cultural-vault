/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.culturalvault.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.culturalvault.model.UserInteraction;
import com.culturalvault.repository.PopularArtifact;
import com.culturalvault.repository.UserEngagement;
import com.culturalvault.service.UserInteractionService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author parth
 */
@RestController
@RequestMapping("/api/interactions")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class UserInterationController {

    private final UserInteractionService interactionService;
    
    public ResponseEntity<UserInteraction> recordInteraction(
            @RequestBody Map<String, String> interactionData,
            HttpServletRequest request) {
        
        String artifactId = interactionData.get("artifactId");
        String action = interactionData.get("action");
        
        if (artifactId == null || action == null) {
            return ResponseEntity.badRequest().build();
        }
        
        log.debug("POST /api/interactions/record - Artifact: {}, Action: {}", artifactId, action);
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        
        // Get additional request info
        String sessionId = request.getSession().getId();
        String ipAddress = getClientIpAddress(request);
        String userAgent = request.getHeader("User-Agent");
        
        try {
            UserInteraction interaction = interactionService.recordInteraction(
                    username, artifactId, action, sessionId, ipAddress, userAgent);
            
            return ResponseEntity.status(HttpStatus.CREATED).body(interaction);
        } catch (Exception e) {
            log.error("Error recording interaction: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
    
    @GetMapping("/my-activity")
    public ResponseEntity<List<UserInteraction>> getCurrentUserInteractions() {
        log.debug("GET /api/interactions/my-activity");
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        
        List<UserInteraction> interactions = interactionService.getUserInteractions(username);
        return ResponseEntity.ok(interactions);
    }
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<UserInteraction>> getUserInteractions(@PathVariable String userId) {
        log.debug("GET /api/interactions/user/{}", userId);
        
        List<UserInteraction> interactions = interactionService.getUserInteractions(userId);
        return ResponseEntity.ok(interactions);
    }
    
    @GetMapping("/artifact/{artifactId}")
    public ResponseEntity<List<UserInteraction>> getArtifactInteractions(@PathVariable String artifactId) {
        log.debug("GET /api/interactions/artifact/{}", artifactId);
        
        List<UserInteraction> interactions = interactionService.getArtifactInteractions(artifactId);
        return ResponseEntity.ok(interactions);
    }
    
    @GetMapping("/popular-artifacts")
    public ResponseEntity<List<PopularArtifact>> getMostPopularArtifacts(
            @RequestParam(defaultValue = "10") int limit) {
        
        log.debug("GET /api/interactions/popular-artifacts?limit={}", limit);
        
        List<PopularArtifact> popular = interactionService.getMostPopularArtifacts(limit);
        return ResponseEntity.ok(popular);
    }
    
    @GetMapping("/user-engagement")
    public ResponseEntity<List<UserEngagement>> getUserEngagementStatistics() {
        log.debug("GET /api/interactions/user-engagement");
        
        List<UserEngagement> engagement = interactionService.getUserEngagementStatistics();
        return ResponseEntity.ok(engagement);
    }
    
    @GetMapping("/recent")
    public ResponseEntity<List<UserInteraction>> getRecentInteractions(
            @RequestParam(defaultValue = "24") int hours) {
        
        log.debug("GET /api/interactions/recent?hours={}", hours);
        
        List<UserInteraction> recent = interactionService.getRecentInteractions(hours);
        return ResponseEntity.ok(recent);
    }
    
    @GetMapping("/check/{artifactId}")
    public ResponseEntity<Map<String, Boolean>> checkUserInteractions(@PathVariable String artifactId) {
        log.debug("GET /api/interactions/check/{}", artifactId);
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        
        Map<String, Boolean> interactions = Map.of(
                "viewed", interactionService.hasUserViewedArtifact(username, artifactId),
                "liked", interactionService.hasUserLikedArtifact(username, artifactId),
                "saved", interactionService.hasUserSavedArtifact(username, artifactId)
        );
        
        return ResponseEntity.ok(interactions);
    }
    
    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Object>> getInteractionStatistics() {
        log.debug("GET /api/interactions/statistics");
        
        Map<String, Object> stats = Map.of(
                "totalInteractions", interactionService.getTotalInteractionCount(),
                "recentInteractions24h", interactionService.getRecentInteractionCount(24),
                "viewCount", interactionService.getActionCount("VIEW"),
                "likeCount", interactionService.getActionCount("LIKE"),
                "saveCount", interactionService.getActionCount("SAVE"),
                "shareCount", interactionService.getActionCount("SHARE")
        );
        
        return ResponseEntity.ok(stats);
    }
    
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> healthCheck() {
        log.debug("GET /api/interactions/health");
        
        long totalCount = interactionService.getTotalInteractionCount();
        
        Map<String, Object> health = Map.of(
                "status", "UP",
                "service", "UserInteractionController",
                "totalInteractions", totalCount,
                "timestamp", System.currentTimeMillis()
        );
        
        return ResponseEntity.ok(health);
    }
    
    private String getClientIpAddress(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        
        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty()) {
            return xRealIp;
        }
        
        return request.getRemoteAddr();
    }
}
