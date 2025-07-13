/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.culturalvault.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.culturalvault.model.UserInteraction;
import com.culturalvault.repository.PopularArtifact;
import com.culturalvault.repository.UserEngagement;
import com.culturalvault.repository.UserInteractionRepository;

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
public class UserInteractionService {
    
    private final UserInteractionRepository interactionRepository;
    
    public UserInteraction recordInteraction(String userId, String artifactId, String action) {
        log.debug("Recording interaction - User: {}, Artifact: {}, Action: {}", userId, artifactId, action);
        
        UserInteraction interaction = new UserInteraction(userId, artifactId, action);
        return interactionRepository.save(interaction);
    }
    
    public UserInteraction recordInteraction(String userId, String artifactId, String action, 
                                           String sessionId, String ipAddress, String userAgent) {
        log.debug("Recording detailed interaction - User: {}, Artifact: {}, Action: {}", userId, artifactId, action);
        
        UserInteraction interaction = new UserInteraction(userId, artifactId, action);
        interaction.setSessionId(sessionId);
        interaction.setIpAddress(ipAddress);
        interaction.setUserAgent(userAgent);
        
        return interactionRepository.save(interaction);
    }
    
    public List<UserInteraction> getUserInteractions(String userId) {
        log.debug("Fetching interactions for user: {}", userId);
        return interactionRepository.findByUserIdOrderByTimestampDesc(userId);
    }
    
    public List<UserInteraction> getArtifactInteractions(String artifactId) {
        log.debug("Fetching interactions for artifact: {}", artifactId);
        return interactionRepository.findByArtifactIdOrderByTimestampDesc(artifactId);
    }
    
    public List<UserInteraction> getInteractionsByAction(String action) {
        log.debug("Fetching interactions by action: {}", action);
        return interactionRepository.findByActionOrderByTimestampDesc(action);
    }
    
    public List<UserInteraction> getUserArtifactInteractions(String userId, String artifactId) {
        log.debug("Fetching interactions for user {} and artifact {}", userId, artifactId);
        return interactionRepository.findByUserIdAndArtifactId(userId, artifactId);
    }
    
    public Optional<UserInteraction> getSpecificInteraction(String userId, String artifactId, String action) {
        log.debug("Fetching specific interaction - User: {}, Artifact: {}, Action: {}", userId, artifactId, action);
        return interactionRepository.findByUserIdAndArtifactIdAndAction(userId, artifactId, action);
    }
    
    public List<UserInteraction> getRecentInteractions(int hours) {
        log.debug("Fetching interactions from last {} hours", hours);
        LocalDateTime since = LocalDateTime.now().minusHours(hours);
        return interactionRepository.findByTimestampAfterOrderByTimestampDesc(since);
    }
    
    public List<UserInteraction> getInteractionsInRange(LocalDateTime start, LocalDateTime end) {
        log.debug("Fetching interactions between {} and {}", start, end);
        return interactionRepository.findByTimestampBetween(start, end);
    }
    
    // ===== Analytics and Recommendations =====
    
    public List<PopularArtifact> getMostPopularArtifacts(int limit) {
        log.debug("Fetching top {} popular artifacts", limit);
        return interactionRepository.findMostPopularArtifacts(limit);
    }
    
    public List<UserEngagement> getUserEngagementStatistics() {
        log.debug("Fetching user engagement statistics");
        return interactionRepository.getUserEngagementStats();
    }
    
    public boolean hasUserViewedArtifact(String userId, String artifactId) {
        return getSpecificInteraction(userId, artifactId, "VIEW").isPresent();
    }
    
    public boolean hasUserLikedArtifact(String userId, String artifactId) {
        return getSpecificInteraction(userId, artifactId, "LIKE").isPresent();
    }
    
    public boolean hasUserSavedArtifact(String userId, String artifactId) {
        return getSpecificInteraction(userId, artifactId, "SAVE").isPresent();
    }
    
    public long getTotalInteractionCount() {
        return interactionRepository.count();
    }
    
    public long getUserInteractionCount(String userId) {
        return interactionRepository.countByUserId(userId);
    }
    
    public long getArtifactInteractionCount(String artifactId) {
        return interactionRepository.countByArtifactId(artifactId);
    }
    
    public long getActionCount(String action) {
        return interactionRepository.countByAction(action);
    }
    
    public long getRecentInteractionCount(int hours) {
        LocalDateTime since = LocalDateTime.now().minusHours(hours);
        return interactionRepository.countByTimestampAfter(since);
    }
    public void deleteOldInteractions(int daysToKeep) {
        log.info("Cleaning up interactions older than {} days", daysToKeep);
        LocalDateTime cutoff = LocalDateTime.now().minusDays(daysToKeep);
        
        List<UserInteraction> oldInteractions = interactionRepository.findByTimestampBetween(
                LocalDateTime.of(2020, 1, 1, 0, 0), cutoff);
        
        if (!oldInteractions.isEmpty()) {
            interactionRepository.deleteAll(oldInteractions);
            log.info("Deleted {} old interactions", oldInteractions.size());
        }
    }
}
