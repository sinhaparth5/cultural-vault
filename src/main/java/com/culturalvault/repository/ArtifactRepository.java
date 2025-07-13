/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */

package com.culturalvault.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.culturalvault.model.Artifact;

/**
 *
 * @author parth
 */

@Repository
public interface ArtifactRepository extends MongoRepository<Artifact, String> {
    // Basic finders by individual fields
    Page<Artifact> findByCategory(String category, Pageable pageable);
    Page<Artifact> findByCulture(String culture, Pageable pageable);
    Page<Artifact> findByPeriod(String period, Pageable pageable);
    Page<Artifact> findByMaterial(String material, Pageable pageable);
    Page<Artifact> findBySource(String source, Pageable pageable);
    
    // Case-insensitive searches
    Page<Artifact> findByCategoryIgnoreCase(String category, Pageable pageable);
    Page<Artifact> findByCultureIgnoreCase(String culture, Pageable pageable);
    Page<Artifact> findByPeriodIgnoreCase(String period, Pageable pageable);
    
    // Text search using MongoDB text index
    @Query("{ $text: { $search: ?0 } }")
    Page<Artifact> findByTextSearch(String searchText, Pageable pageable);
    
    // Advanced text search with score
    @Query(value = "{ $text: { $search: ?0 } }", 
           sort = "{ score: { $meta: 'textScore' } }")
    List<Artifact> findByTextSearchWithScore(String searchText);
    
    // Multi-criteria search
    @Query("{ $and: [ " +
           "{ $or: [ {category: {$regex: ?0, $options: 'i'}}, {category: {$exists: false}} ] }, " +
           "{ $or: [ {culture: {$regex: ?1, $options: 'i'}}, {culture: {$exists: false}} ] }, " +
           "{ $or: [ {period: {$regex: ?2, $options: 'i'}}, {period: {$exists: false}} ] } " +
           "] }")
    Page<Artifact> findByCriteria(String category, String culture, String period, Pageable pageable);
    
    // Find artifacts with non-null analysis (AI processed) - FIXED TYPO
    @Query("{ 'analysis': { $exists: true, $ne: null } }")
    Page<Artifact> findAnalyzedArtifacts(Pageable pageable);
    
    // Find artifacts without analysis (need AI processing)
    @Query("{ $or: [ {'analysis': {$exists: false}}, {'analysis': null} ] }")
    Page<Artifact> findUnanalyzedArtifacts(Pageable pageable);
    
    // Random sampling for recommendations
    @Aggregation(pipeline = {
        "{ $sample: { size: ?0 } }"
    })
    List<Artifact> findRandomArtifacts(int count);
    
    // Random artifacts by category
    @Aggregation(pipeline = {
        "{ $match: { category: ?0 } }",
        "{ $sample: { size: ?1 } }"
    })
    List<Artifact> findRandomArtifactsByCategory(String category, int count);
    
    // Find similar artifacts by culture and period
    @Query("{ $and: [ " +
           "{ culture: ?0 }, " +
           "{ period: ?1 }, " +
           "{ _id: { $ne: ?2 } } " +
           "] }")
    List<Artifact> findSimilarArtifacts(String culture, String period, String excludeId, Pageable pageable);
    
    // Find artifacts created within date range
    List<Artifact> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    // Find artifacts by source and sourceId
    Optional<Artifact> findBySourceAndSourceId(String source, String sourceId);
    
    // Distinct values for filters
    @Query(value = "{}", fields = "{ category: 1 }")
    List<String> findDistinctCategories();
    
    @Query(value = "{}", fields = "{ culture: 1 }")
    List<String> findDistinctCultures();
    
    @Query(value = "{}", fields = "{ period: 1 }")
    List<String> findDistinctPeriods();
    
    @Query(value = "{}", fields = "{ material: 1 }")
    List<String> findDistinctMaterials();
    
    // Count queries
    long countByCategory(String category);
    long countByCulture(String culture);
    long countByPeriod(String period);
    long countBySource(String source);
    
    // Find artifacts with images
    @Query("{ $and: [ " +
           "{ imageUrl: { $exists: true, $ne: null, $ne: '' } }, " +
           "{ $or: [ " +
           "  { thumbnailUrl: { $exists: true, $ne: null, $ne: '' } }, " +
           "  { r2ImageKey: { $exists: true, $ne: null, $ne: '' } } " +
           "] } " +
           "] }")
    Page<Artifact> findArtifactsWithImages(Pageable pageable);
    
    // Complex aggregation for statistics
    @Aggregation(pipeline = {
        "{ $group: { " +
        "    _id: '$category', " +
        "    count: { $sum: 1 }, " +
        "    cultures: { $addToSet: '$culture' }, " +
        "    periods: { $addToSet: '$period' } " +
        "} }",
        "{ $sort: { count: -1 } }"
    })
    List<CategoryStats> getCategoryStatistics();
}