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

import com.culturalvault.model.Artifact;
import com.culturalvault.service.ArtifactService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author parth
 */
@RestController
@RequestMapping("/api/artifacts")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class ArtifactController {

    private final ArtifactService artifactService;
    
    @GetMapping
    public ResponseEntity<Page<Artifact>> getAllArtifacts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        
        log.debug("GET /api/artifacts - Page: {}, Size: {}, Sort: {} {}", page, size, sortBy, sortDir);
        
        Sort sort = sortDir.equalsIgnoreCase("desc") 
            ? Sort.by(sortBy).descending() 
            : Sort.by(sortBy).ascending();
        
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Artifact> artifacts = artifactService.getAllArtifacts(pageable);
        
        return ResponseEntity.ok(artifacts);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Artifact> getArtifactById(@PathVariable String id) {
        log.debug("GET /api/artifacts/{}", id);
        
        return artifactService.getArtifactById(id)
                .map(artifact -> ResponseEntity.ok(artifact))
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<Artifact> createArtifact(@Valid @RequestBody Artifact artifact) {
        log.info("POST /api/artifacts - Creating artifact: {}", artifact.getTitle());
        
        try {
            Artifact savedArtifact = artifactService.saveArtifact(artifact);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedArtifact);
        } catch (Exception e) {
            log.error("Error creating artifact: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Artifact> updateArtifact(
            @PathVariable String id, 
            @Valid @RequestBody Artifact artifact) {
        
        log.info("PUT /api/artifacts/{} - Updating artifact", id);
        
        try {
            Artifact updatedArtifact = artifactService.updateArtifact(id, artifact);
            return ResponseEntity.ok(updatedArtifact);
        } catch (RuntimeException e) {
            log.error("Error updating artifact {}: {}", id, e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArtifact(@PathVariable String id) {
        log.info("DELETE /api/artifacts/{}", id);
        
        if (!artifactService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        
        artifactService.deleteArtifact(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/search")
    public ResponseEntity<Page<Artifact>> searchArtifacts(
            @RequestParam String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        
        log.debug("GET /api/artifacts/search?q={}", q);
        
        Sort sort = sortDir.equalsIgnoreCase("desc") 
            ? Sort.by(sortBy).descending() 
            : Sort.by(sortBy).ascending();
        
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Artifact> artifacts = artifactService.searchArtifacts(q, pageable);
        
        return ResponseEntity.ok(artifacts);
    }
    
    @GetMapping("/category/{category}")
    public ResponseEntity<Page<Artifact>> getArtifactsByCategory(
            @PathVariable String category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size) {
        
        log.debug("GET /api/artifacts/category/{}", category);
        
        Pageable pageable = PageRequest.of(page, size);
        Page<Artifact> artifacts = artifactService.getArtifactsByCategory(category, pageable);
        
        return ResponseEntity.ok(artifacts);
    }
    
    @GetMapping("/culture/{culture}")
    public ResponseEntity<Page<Artifact>> getArtifactsByCulture(
            @PathVariable String culture,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size) {
        
        log.debug("GET /api/artifacts/culture/{}", culture);
        
        Pageable pageable = PageRequest.of(page, size);
        Page<Artifact> artifacts = artifactService.getArtifactsByCulture(culture, pageable);
        
        return ResponseEntity.ok(artifacts);
    }
    
    @GetMapping("/period/{period}")
    public ResponseEntity<Page<Artifact>> getArtifactsByPeriod(
            @PathVariable String period,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size) {
        
        log.debug("GET /api/artifacts/period/{}", period);
        
        Pageable pageable = PageRequest.of(page, size);
        Page<Artifact> artifacts = artifactService.getArtifactsByPeriod(period, pageable);
        
        return ResponseEntity.ok(artifacts);
    }
    
    @GetMapping("/filter")
    public ResponseEntity<Page<Artifact>> getArtifactsByCriteria(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String culture,
            @RequestParam(required = false) String period,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size) {
        
        log.debug("GET /api/artifacts/filter - Category: {}, Culture: {}, Period: {}", 
                 category, culture, period);
        
        Pageable pageable = PageRequest.of(page, size);
        Page<Artifact> artifacts = artifactService.getArtifactsByCriteria(category, culture, period, pageable);
        
        return ResponseEntity.ok(artifacts);
    }
    
    @GetMapping("/random")
    public ResponseEntity<List<Artifact>> getRandomArtifacts(
            @RequestParam(defaultValue = "6") int count) {
        
        log.debug("GET /api/artifacts/random?count={}", count);
        
        List<Artifact> artifacts = artifactService.getRandomArtifacts(count);
        return ResponseEntity.ok(artifacts);
    }
    
    @GetMapping("/random/category/{category}")
    public ResponseEntity<List<Artifact>> getRandomArtifactsByCategory(
            @PathVariable String category,
            @RequestParam(defaultValue = "6") int count) {
        
        log.debug("GET /api/artifacts/random/category/{}?count={}", category, count);
        
        List<Artifact> artifacts = artifactService.getRandomArtifactsByCategory(category, count);
        return ResponseEntity.ok(artifacts);
    }
    
    @GetMapping("/{id}/similar")
    public ResponseEntity<List<Artifact>> getSimilarArtifacts(
            @PathVariable String id,
            @RequestParam(defaultValue = "6") int count) {
        
        log.debug("GET /api/artifacts/{}/similar?count={}", id, count);
        
        List<Artifact> artifacts = artifactService.getSimilarArtifacts(id, count);
        return ResponseEntity.ok(artifacts);
    }
    
    @GetMapping("/metadata")
    public ResponseEntity<Map<String, Object>> getArtifactMetadata() {
        log.debug("GET /api/artifacts/metadata");
        
        Map<String, Object> metadata = artifactService.getArtifactMetadata();
        return ResponseEntity.ok(metadata);
    }
    
    @GetMapping("/categories")
    public ResponseEntity<List<String>> getDistinctCategories() {
        log.debug("GET /api/artifacts/categories");
        
        List<String> categories = artifactService.getDistinctCategories();
        return ResponseEntity.ok(categories);
    }
    
    @GetMapping("/cultures")
    public ResponseEntity<List<String>> getDistinctCultures() {
        log.debug("GET /api/artifacts/cultures");
        
        List<String> cultures = artifactService.getDistinctCultures();
        return ResponseEntity.ok(cultures);
    }
    
    @GetMapping("/periods")
    public ResponseEntity<List<String>> getDistinctPeriods() {
        log.debug("GET /api/artifacts/periods");
        
        List<String> periods = artifactService.getDistinctPeriods();
        return ResponseEntity.ok(periods);
    }
    
    @GetMapping("/statistics/categories")
    public ResponseEntity<Map<String, Long>> getCategoryStatistics() {
        log.debug("GET /api/artifacts/statistics/categories");
        
        Map<String, Long> stats = artifactService.getCategoryStatistics();
        return ResponseEntity.ok(stats);
    }
    
    @GetMapping("/statistics/cultures")
    public ResponseEntity<Map<String, Long>> getCultureStatistics() {
        log.debug("GET /api/artifacts/statistics/cultures");
        
        Map<String, Long> stats = artifactService.getCultureStatistics();
        return ResponseEntity.ok(stats);
    }
    
    @GetMapping("/unanalyzed")
    public ResponseEntity<Page<Artifact>> getUnanalyzedArtifacts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        log.debug("GET /api/artifacts/unanalyzed");
        
        Pageable pageable = PageRequest.of(page, size);
        Page<Artifact> artifacts = artifactService.getUnanalyzedArtifacts(pageable);
        
        return ResponseEntity.ok(artifacts);
    }
    
    @GetMapping("/analyzed")
    public ResponseEntity<Page<Artifact>> getAnalyzedArtifacts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        log.debug("GET /api/artifacts/analyzed");
        
        Pageable pageable = PageRequest.of(page, size);
        Page<Artifact> artifacts = artifactService.getAnalyzedArtifacts(pageable);
        
        return ResponseEntity.ok(artifacts);
    }
    
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> healthCheck() {
        log.debug("GET /api/artifacts/health");
        
        long totalCount = artifactService.getTotalCount();
        
        Map<String, Object> health = Map.of(
                "status", "UP",
                "service", "ArtifactController",
                "totalArtifacts", totalCount,
                "timestamp", System.currentTimeMillis()
        );
        
        return ResponseEntity.ok(health);
    }
}
