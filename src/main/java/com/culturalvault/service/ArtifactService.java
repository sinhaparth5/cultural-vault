/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.culturalvault.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.culturalvault.model.Artifact;
import com.culturalvault.repository.ArtifactRepository;
import com.culturalvault.repository.CategoryStats;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

// @TODO need to fix the find repository in artifacts repository to use the correct field names

/**
 *
 * @author parth
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ArtifactService {
    private final ArtifactRepository artifactRepository;

    public Page<Artifact> getAllArtifacts(Pageable pagebale) {
        log.debug("Fetching all artifacts with pagination: {}", pagebale);
        return artifactRepository.findAll(pagebale);
    }

    public Optional<Artifact> getArtifactById(String id) {
        log.debug("Fetching artifact by ID: {}", id);
        return artifactRepository.findById(id);
    }

    public Artifact saveArtifact(Artifact artifact) {
        log.debug("Saving artifact: {}", artifact.getTitle());
        // Set timestamp
        if(artifact.getId() == null) {
            artifact.setCreatedAt(LocalDateTime.now());
        }
        artifact.setUpdatedAt(LocalDateTime.now());
        return artifactRepository.save(artifact);
    }

    public Artifact updateArtifact(String id, Artifact updatedArtifact) {
        log.debug("Updating artifact with ID: {}", id);

        return artifactRepository.findById(id)
                .map(artifact -> {
                    artifact.setTitle(updatedArtifact.getTitle());
                    artifact.setDescription(updatedArtifact.getDescription());
                    artifact.setCategory(updatedArtifact.getCategory());
                    artifact.setCulture(updatedArtifact.getCulture());
                    artifact.setPeriod(updatedArtifact.getPeriod());
                    artifact.setMaterial(updatedArtifact.getMaterial());
                    artifact.setImageUrl(updatedArtifact.getImageUrl());
                    artifact.setThumbnailUrl(updatedArtifact.getThumbnailUrl());
                    artifact.setMetadata(updatedArtifact.getMetadata());
                    artifact.setUpdatedAt(LocalDateTime.now());
                    
                    return artifactRepository.save(artifact);
                })
                .orElseThrow(() -> new RuntimeException("Artifact not found with ID: " + id));
    }

    public void deleteArtifact(String id) {
        log.info("Deleting artifact with id: {}", id);
        artifactRepository.deleteById(id);
    }
    
    // ===== Search and Filtering =====
    
    public Page<Artifact> searchArtifacts(String searchText, Pageable pageable) {
        log.debug("Searching artifacts with text: {}", searchText);
        
        if (searchText == null || searchText.trim().isEmpty()) {
            return getAllArtifacts(pageable);
        }
        
        return artifactRepository.findByTextSearch(searchText.trim(), pageable);
    }
    
    public Page<Artifact> getArtifactsByCategory(String category, Pageable pageable) {
        log.debug("Fetching artifacts by category: {}", category);
        return artifactRepository.findByCategoryIgnoreCase(category, pageable);
    }
    
    public Page<Artifact> getArtifactsByCulture(String culture, Pageable pageable) {
        log.debug("Fetching artifacts by culture: {}", culture);
        return artifactRepository.findByCultureIgnoreCase(culture, pageable);
    }
    
    public Page<Artifact> getArtifactsByPeriod(String period, Pageable pageable) {
        log.debug("Fetching artifacts by period: {}", period);
        return artifactRepository.findByPeriodIgnoreCase(period, pageable);
    }
    
    public Page<Artifact> getArtifactsByCriteria(String category, String culture, String period, Pageable pageable) {
        log.debug("Fetching artifacts by criteria - Category: {}, Culture: {}, Period: {}", category, culture, period);
        return artifactRepository.findByCriteria(category, culture, period, pageable);
    }
    
    // ===== Recommendations =====
    
    public List<Artifact> getRandomArtifacts(int count) {
        log.debug("Fetching {} random artifacts", count);
        return artifactRepository.findRandomArtifacts(Math.min(count, 50)); // Limit to 50
    }
    
    public List<Artifact> getRandomArtifactsByCategory(String category, int count) {
        log.debug("Fetching {} random artifacts from category: {}", count, category);
        return artifactRepository.findRandomArtifactsByCategory(category, Math.min(count, 20));
    }
    
    public List<Artifact> getSimilarArtifacts(String artifactId, int count) {
        log.debug("Fetching similar artifacts for artifact: {}", artifactId);
        
        return artifactRepository.findById(artifactId)
                .map(artifact -> {
                    Pageable pageable = PageRequest.of(0, count);
                    return artifactRepository.findSimilarArtifacts(
                            artifact.getCulture(), 
                            artifact.getPeriod(), 
                            artifactId, 
                            pageable
                    );
                })
                .orElse(List.of());
    }
    
    // ===== Analytics and Metadata =====
    
    public List<String> getDistinctCategories() {
        log.debug("Fetching distinct categories");
        return artifactRepository.findAll()
                .stream()
                .map(Artifact::getCategory)
                .filter(category -> category != null && !category.trim().isEmpty())
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }
    
    public List<String> getDistinctCultures() {
        log.debug("Fetching distinct cultures");
        return artifactRepository.findAll()
                .stream()
                .map(Artifact::getCulture)
                .filter(culture -> culture != null && !culture.trim().isEmpty())
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }
    
    public List<String> getDistinctPeriods() {
        log.debug("Fetching distinct periods");
        return artifactRepository.findAll()
                .stream()
                .map(Artifact::getPeriod)
                .filter(period -> period != null && !period.trim().isEmpty())
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }
    
    public List<String> getDistinctMaterials() {
        log.debug("Fetching distinct materials");
        return artifactRepository.findAll()
                .stream()
                .map(Artifact::getMaterial)
                .filter(material -> material != null && !material.trim().isEmpty())
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }
    
    public Map<String, Object> getArtifactMetadata() {
        log.debug("Fetching artifact metadata and statistics");
        
        long totalCount = artifactRepository.count();
        List<CategoryStats> categoryStats = artifactRepository.getCategoryStatistics();
        
        return Map.of(
                "totalArtifacts", totalCount,
                "categories", getDistinctCategories(),
                "cultures", getDistinctCultures(),
                "periods", getDistinctPeriods(),
                "materials", getDistinctMaterials(),
                "categoryStatistics", categoryStats
        );
    }
    
    // ===== AI Analysis Support =====
    
    public Page<Artifact> getUnanalyzedArtifacts(Pageable pageable) {
        log.debug("Fetching unanalyzed artifacts for AI processing");
        return artifactRepository.findUnanalyzedArtifacts(pageable);
    }
    
    public Page<Artifact> getAnalyzedArtifacts(Pageable pageable) {
        log.debug("Fetching artifacts with AI analysis");
        return artifactRepository.findAnalyzedArtifacts(pageable);
    }
    
    public Page<Artifact> getArtifactsWithImages(Pageable pageable) {
        log.debug("Fetching artifacts with images");
        return artifactRepository.findArtifactsWithImages(pageable);
    }
    
    // ===== Data Import Support =====
    
    public Optional<Artifact> findBySourceAndSourceId(String source, String sourceId) {
        log.debug("Finding artifact by source: {} and sourceId: {}", source, sourceId);
        return artifactRepository.findBySourceAndSourceId(source, sourceId);
    }
    
    public Page<Artifact> getArtifactsBySource(String source, Pageable pageable) {
        log.debug("Fetching artifacts by source: {}", source);
        return artifactRepository.findBySource(source, pageable);
    }
    
    public List<Artifact> getArtifactsBySource(String source) {
        log.debug("Fetching all artifacts by source: {}", source);
        // Get first 1000 results (reasonable limit)
        Page<Artifact> page = artifactRepository.findBySource(source, PageRequest.of(0, 1000));
        return page.getContent();
    }

    public List<Artifact> getRecentArtifacts(int days) {
        log.debug("Fetching artifacts from last {} days", days);
        LocalDateTime since = LocalDateTime.now().minusDays(days);
        return artifactRepository.findByCreatedAtBetween(since, LocalDateTime.now());
    }
    
    // ===== Statistics =====
    
    public Map<String, Long> getCategoryStatistics() {
        log.debug("Fetching category statistics");
        List<String> categories = getDistinctCategories();
        
        return categories.stream()
                .collect(Collectors.toMap(
                        category -> category,
                        category -> artifactRepository.countByCategory(category)
                ));
    }
    
    public Map<String, Long> getCultureStatistics() {
        log.debug("Fetching culture statistics");
        List<String> cultures = getDistinctCultures();
        
        return cultures.stream()
                .collect(Collectors.toMap(
                        culture -> culture,
                        culture -> artifactRepository.countByCulture(culture)
                ));
    }
    
    public boolean existsById(String id) {
        return artifactRepository.existsById(id);
    }
    
    public long getTotalCount() {
        return artifactRepository.count();
    }
}
