/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.culturalvault.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.culturalvault.model.Artifact;
import com.culturalvault.model.User;
import com.culturalvault.model.UserInteraction;
import com.culturalvault.repository.PopularArtifact;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author parth
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class RecommendationService {

    private final ArtifactService artifactService;
    private final UserService userService;
    private final UserInteractionService interactionService;
    
    public List<Artifact> getRecommendationsForUser(String userId, int count) {
        log.debug("Generating recommendations for user: {}", userId);
        
        return userService.getUserById(userId)
                .map(user -> generateRecommendations(user, count))
                .orElse(getPopularArtifacts(count));
    }
    
    private List<Artifact> generateRecommendations(User user, int count) {
        // Get user's interaction history
        List<UserInteraction> userInteractions = interactionService.getUserInteractions(user.getId());
        
        if (userInteractions.isEmpty()) {
            // New user - return popular artifacts
            return getPopularArtifacts(count);
        }
        
        // Get artifacts user has interacted with
        Set<String> viewedArtifactIds = userInteractions.stream()
                .map(UserInteraction::getArtifactId)
                .collect(Collectors.toSet());
        
        // Find similar artifacts based on user's preferences
        List<Artifact> recommendations = findSimilarArtifacts(viewedArtifactIds, user, count * 2);
        
        // Filter out already viewed artifacts
        recommendations = recommendations.stream()
                .filter(artifact -> !viewedArtifactIds.contains(artifact.getId()))
                .limit(count)
                .collect(Collectors.toList());
        
        // If not enough recommendations, fill with popular artifacts
        if (recommendations.size() < count) {
            List<Artifact> popularArtifacts = getPopularArtifacts(count - recommendations.size());
            popularArtifacts.stream()
                    .filter(artifact -> !viewedArtifactIds.contains(artifact.getId()))
                    .forEach(recommendations::add);
        }
        
        return recommendations.stream().limit(count).collect(Collectors.toList());
    }
    
    private List<Artifact> findSimilarArtifacts(Set<String> viewedArtifactIds, User user, int count) {
        // Get user preferences
        List<String> preferredGenres = user.getPreferences() != null ? 
                user.getPreferences().getFavoriteGenres() : List.of();
        List<String> interests = user.getPreferences() != null ? 
                user.getPreferences().getInterests() : List.of();
        
        // Find artifacts similar to user's interests
        List<Artifact> similar = List.of();
        
        // Try to find by culture/period from interests
        for (String interest : interests) {
            if (interest.contains("ROMAN")) {
                similar.addAll(artifactService.getArtifactsByCulture("ROMAN", PageRequest.of(0, 5)).getContent());
            } else if (interest.contains("GREEK")) {
                similar.addAll(artifactService.getArtifactsByCulture("GREEK", PageRequest.of(0, 5)).getContent());
            } else if (interest.contains("EGYPTIAN")) {
                similar.addAll(artifactService.getArtifactsByCulture("EGYPTIAN", PageRequest.of(0, 5)).getContent());
            }
        }
        
        // If no specific interests, get random artifacts
        if (similar.isEmpty()) {
            similar = artifactService.getRandomArtifacts(count);
        }
        
        return similar.stream().limit(count).collect(Collectors.toList());
    }
    
    public List<Artifact> getSimilarArtifacts(String artifactId, int count) {
        log.debug("Finding similar artifacts to: {}", artifactId);
        return artifactService.getSimilarArtifacts(artifactId, count);
    }
    
    public List<Artifact> getArtifactsByUserInterests(String userId, int count) {
        log.debug("Getting artifacts based on user interests: {}", userId);
        
        return userService.getUserById(userId)
                .map(user -> {
                    if (user.getPreferences() != null && user.getPreferences().getInterests() != null) {
                        return getArtifactsByInterests(user.getPreferences().getInterests(), count);
                    }
                    return getPopularArtifacts(count);
                })
                .orElse(getPopularArtifacts(count));
    }
    
    private List<Artifact> getArtifactsByInterests(List<String> interests, int count) {
        List<Artifact> artifacts = List.of();
        
        for (String interest : interests) {
            // Map interests to cultures/categories
            if (interest.contains("ANCIENT")) {
                artifacts.addAll(artifactService.getArtifactsByPeriod("ANCIENT", PageRequest.of(0, count/interests.size())).getContent());
            } else if (interest.contains("COIN")) {
                artifacts.addAll(artifactService.getArtifactsByCategory("COIN", PageRequest.of(0, count/interests.size())).getContent());
            } else if (interest.contains("ART")) {
                artifacts.addAll(artifactService.getArtifactsByCategory("PAINTING", PageRequest.of(0, count/interests.size())).getContent());
            }
        }
        
        return artifacts.stream().limit(count).collect(Collectors.toList());
    }
    
    public List<Artifact> getPopularArtifacts(int count) {
        log.debug("Getting popular artifacts based on interactions");
        
        // Get most popular artifacts from interactions
        List<String> popularArtifactIds = interactionService.getMostPopularArtifacts(count * 2)
                .stream()
                .map(PopularArtifact::getId)
                .collect(Collectors.toList());
        
        // Fetch actual artifact objects
        List<Artifact> popularArtifacts = popularArtifactIds.stream()
                .map(artifactService::getArtifactById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .limit(count)
                .collect(Collectors.toList());
        
        // If not enough popular artifacts, fill with random ones
        if (popularArtifacts.size() < count) {
            List<Artifact> randomArtifacts = artifactService.getRandomArtifacts(count - popularArtifacts.size());
            popularArtifacts.addAll(randomArtifacts);
        }
        
        return popularArtifacts.stream().limit(count).collect(Collectors.toList());
    }
    
    public List<Artifact> getTrendingArtifacts(int count) {
        log.debug("Getting trending artifacts from recent interactions");
        
        // Get artifacts with recent high interaction
        List<UserInteraction> recentInteractions = interactionService.getRecentInteractions(24); // Last 24 hours
        
        // Count interactions per artifact
        List<String> trendingArtifactIds = recentInteractions.stream()
                .collect(Collectors.groupingBy(UserInteraction::getArtifactId, Collectors.counting()))
                .entrySet().stream()
                .sorted((e1, e2) -> Long.compare(e2.getValue(), e1.getValue()))
                .limit(count)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
        
        // Fetch actual artifacts
        return trendingArtifactIds.stream()
                .map(artifactService::getArtifactById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }
    
    public List<Artifact> getRecommendationsByCategory(String category, int count) {
        log.debug("Getting recommendations for category: {}", category);
        return artifactService.getRandomArtifactsByCategory(category, count);
    }
    
    public List<Artifact> getFavoriteBasedRecommendations(String userId, int count) {
        log.debug("Getting recommendations based on user favorites: {}", userId);
        
        return userService.getUserById(userId)
                .map(user -> {
                    if (user.getFavoriteArtifacts() != null && !user.getFavoriteArtifacts().isEmpty()) {
                        // Get similar artifacts to user's favorites
                        return user.getFavoriteArtifacts().stream()
                                .flatMap(artifactId -> getSimilarArtifacts(artifactId, 3).stream())
                                .limit(count)
                                .collect(Collectors.toList());
                    }
                    return getPopularArtifacts(count);
                })
                .orElse(getPopularArtifacts(count));
    }
}
