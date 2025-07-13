/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.culturalvault.controller;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author parth
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class HomeController {
    @GetMapping
    public ResponseEntity<Map<String, Object>> apiRoot() {
        log.debug("GET /api");
        
        Map<String, Object> response = Map.of(
                "application", "CulturalVault",
                "description", "AI-Powered Cultural Heritage Storytelling Platform",
                "version", "1.0.0",
                "status", "UP",
                "timestamp", LocalDateTime.now(),
                "endpoints", Map.of(
                        "artifacts", "/api/artifacts",
                        "stories", "/api/stories", 
                        "users", "/api/users",
                        "recommendations", "/api/recommendations",
                        "interactions", "/api/interactions"
                )
        );
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> globalHealthCheck() {
        log.debug("GET /api/health");
        
        Map<String, Object> health = Map.of(
                "status", "UP",
                "application", "CulturalVault",
                "timestamp", LocalDateTime.now(),
                "uptime", System.currentTimeMillis(),
                "services", Map.of(
                        "artifacts", "UP",
                        "stories", "UP",
                        "users", "UP",
                        "recommendations", "UP",
                        "interactions", "UP"
                )
        );
        
        return ResponseEntity.ok(health);
    }
    
    @GetMapping("/version")
    public ResponseEntity<Map<String, String>> getVersion() {
        log.debug("GET /api/version");
        
        Map<String, String> version = Map.of(
                "application", "CulturalVault",
                "version", "1.0.0",
                "buildTime", "2024-07-13",
                "environment", "development"
        );
        
        return ResponseEntity.ok(version);
    }
}
