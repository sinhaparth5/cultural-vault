/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.culturalvault.controller;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

import com.culturalvault.model.User;
import com.culturalvault.model.UserPreferences;
import com.culturalvault.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author parth
 */
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class UserController {

    private final UserService userService;
    
    @GetMapping("/profile")
    public ResponseEntity<User> getCurrentUserProfile() {
        log.debug("GET /api/users/profile");
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        
        return userService.getUserByUsername(username)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable String id) {
        log.debug("GET /api/users/{}", id);
        
        return userService.getUserById(id)
                .map(user -> {
                    // Don't expose sensitive information
                    user.setPassword(null);
                    return ResponseEntity.ok(user);
                })
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PutMapping("/profile")
    public ResponseEntity<User> updateCurrentUserProfile(@Valid @RequestBody User updatedUser) {
        log.info("PUT /api/users/profile");
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        
        return userService.getUserByUsername(username)
                .map(user -> {
                    try {
                        User updated = userService.updateUser(user.getId(), updatedUser);
                        updated.setPassword(null); // Don't expose password
                        return ResponseEntity.ok(updated);
                    } catch (RuntimeException e) {
                        log.error("Error updating user profile: {}", e.getMessage());
                        return ResponseEntity.badRequest().<User>build();
                    }
                })
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PutMapping("/profile/preferences")
    public ResponseEntity<User> updateUserPreferences(@Valid @RequestBody UserPreferences preferences) {
        log.info("PUT /api/users/profile/preferences");
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        
        return userService.getUserByUsername(username)
                .map(user -> {
                    try {
                        User updated = userService.updateUserPreferences(user.getId(), preferences);
                        updated.setPassword(null);
                        return ResponseEntity.ok(updated);
                    } catch (RuntimeException e) {
                        log.error("Error updating user preferences: {}", e.getMessage());
                        return ResponseEntity.badRequest().<User>build();
                    }
                })
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping("/profile/favorites/{artifactId}")
    public ResponseEntity<User> addFavoriteArtifact(@PathVariable String artifactId) {
        log.info("POST /api/users/profile/favorites/{}", artifactId);
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        
        return userService.getUserByUsername(username)
                .map(user -> {
                    try {
                        User updated = userService.addFavoriteArtifact(user.getId(), artifactId);
                        updated.setPassword(null);
                        return ResponseEntity.ok(updated);
                    } catch (RuntimeException e) {
                        log.error("Error adding favorite artifact: {}", e.getMessage());
                        return ResponseEntity.badRequest().<User>build();
                    }
                })
                .orElse(ResponseEntity.notFound().build());
    }
    
    @DeleteMapping("/profile/favorites/{artifactId}")
    public ResponseEntity<User> removeFavoriteArtifact(@PathVariable String artifactId) {
        log.info("DELETE /api/users/profile/favorites/{}", artifactId);
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        
        return userService.getUserByUsername(username)
                .map(user -> {
                    try {
                        User updated = userService.removeFavoriteArtifact(user.getId(), artifactId);
                        updated.setPassword(null);
                        return ResponseEntity.ok(updated);
                    } catch (RuntimeException e) {
                        log.error("Error removing favorite artifact: {}", e.getMessage());
                        return ResponseEntity.badRequest().<User>build();
                    }
                })
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/profile/favorites/{artifactId}/check")
    public ResponseEntity<Map<String, Boolean>> checkFavoriteArtifact(@PathVariable String artifactId) {
        log.debug("GET /api/users/profile/favorites/{}/check", artifactId);
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        
        return userService.getUserByUsername(username)
                .map(user -> {
                    boolean isFavorite = userService.isFavoriteArtifact(user.getId(), artifactId);
                    Map<String, Boolean> result = Map.of("isFavorite", isFavorite);
                    return ResponseEntity.ok(result);
                })
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping("/profile/change-password")
    public ResponseEntity<Map<String, String>> changePassword(@RequestBody Map<String, String> passwordData) {
        log.info("POST /api/users/profile/change-password");
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        
        String oldPassword = passwordData.get("oldPassword");
        String newPassword = passwordData.get("newPassword");
        
        if (oldPassword == null || newPassword == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Old and new passwords are required"));
        }
        
        return userService.getUserByUsername(username)
                .map(user -> {
                    boolean success = userService.changePassword(user.getId(), oldPassword, newPassword);
                    if (success) {
                        return ResponseEntity.ok(Map.of("message", "Password changed successfully"));
                    } else {
                        return ResponseEntity.badRequest().body(Map.of("error", "Invalid old password"));
                    }
                })
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping
    public ResponseEntity<Page<User>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        log.debug("GET /api/users - Page: {}, Size: {}", page, size);
        
        Pageable pageable = PageRequest.of(page, size);
        Page<User> users = userService.getAllUsers(pageable);
        
        // Remove passwords from response
        users.getContent().forEach(user -> user.setPassword(null));
        
        return ResponseEntity.ok(users);
    }
    
    @GetMapping("/search")
    public ResponseEntity<Page<User>> searchUsers(
            @RequestParam String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        log.debug("GET /api/users/search?q={}", q);
        
        Pageable pageable = PageRequest.of(page, size);
        Page<User> users = userService.searchUsers(q, pageable);
        
        // Remove passwords from response
        users.getContent().forEach(user -> user.setPassword(null));
        
        return ResponseEntity.ok(users);
    }
    
    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Object>> getUserStatistics() {
        log.debug("GET /api/users/statistics");
        
        Map<String, Object> stats = Map.of(
                "totalUsers", userService.getTotalUserCount(),
                "activeUsers", userService.getActiveUserCount()
        );
        
        return ResponseEntity.ok(stats);
    }
    
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> healthCheck() {
        log.debug("GET /api/users/health");
        
        long totalCount = userService.getTotalUserCount();
        
        Map<String, Object> health = Map.of(
                "status", "UP",
                "service", "UserController",
                "totalUsers", totalCount,
                "timestamp", System.currentTimeMillis()
        );
        
        return ResponseEntity.ok(health);
    }
}
