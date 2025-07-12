/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.culturalvault.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 *
 * @author parth
 */
@Document(collection="artifacts")
public class Artifact {
    @Id
    private String id;

    @TextIndexed
    private String title;

    @TextIndexed
    private String description;

    @Indexed
    private String category;

    @Indexed
    private String culture;

    @Indexed
    private String period;

    private String material;

    private String imageUrl;
    private String thumbnailUrl;
    private String r2ImageKey;

    // Metadata from external APIs
    private Map<String, Object> metadata;

    // AI Analysis Result 
    private ArtifactAnalysis analysis;

    // Source information
    private String source;
    private String sourceId;
    private String sourceUrl;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public Artifact() { }

    public Artifact(String title, String description, String category, String culture, 
                   String period, String material, String imageUrl, String source) {
        this.title = title;
        this.description = description;
        this.category = category;
        this.culture = culture;
        this.period = period;
        this.material = material;
        this.imageUrl = imageUrl;
        this.source = source;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public Artifact(String id, String title, String description, String category, String culture,
                   String period, String material, String imageUrl, String thumbnailUrl,
                   String r2ImageKey, Map<String, Object> metadata, ArtifactAnalysis analysis,
                   String source, String sourceId, String sourceUrl, LocalDateTime createdAt,
                   LocalDateTime updatedAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.category = category;
        this.culture = culture;
        this.period = period;
        this.material = material;
        this.imageUrl = imageUrl;
        this.thumbnailUrl = thumbnailUrl;
        this.r2ImageKey = r2ImageKey;
        this.metadata = metadata;
        this.analysis = analysis;
        this.source = source;
        this.sourceId = sourceId;
        this.sourceUrl = sourceUrl;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCulture() {
        return culture;
    }

    public void setCulture(String culture) {
        this.culture = culture;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getR2ImageKey() {
        return r2ImageKey;
    }

    public void setR2ImageKey(String r2ImageKey) {
        this.r2ImageKey = r2ImageKey;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }

    public ArtifactAnalysis getAnalysis() {
        return analysis;
    }

    public void setAnalysis(ArtifactAnalysis analysis) {
        this.analysis = analysis;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "Artifact{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", category='" + category + '\'' +
                ", culture='" + culture + '\'' +
                ", period='" + period + '\'' +
                ", source='" + source + '\'' +
                '}';
    }
}

class ArtifactAnalysis {
    private List<YoloDetection> detections;
    private List<Double> imageEmbedding; // 512-dimensional vector
    private List<Double> textEmbedding; // 768-dimensional vector
    private List<NamedEntity> entities;
    private Map<String, Double> culturalTags;
    private LocalDateTime analyzedAt;

    // Default Constructor
    public ArtifactAnalysis() {
    }

    // Constructor with essential fields
    public ArtifactAnalysis(List<YoloDetection> detections, List<Double> imageEmbedding,
                           List<Double> textEmbedding, List<NamedEntity> entities) {
        this.detections = detections;
        this.imageEmbedding = imageEmbedding;
        this.textEmbedding = textEmbedding;
        this.entities = entities;
        this.analyzedAt = LocalDateTime.now();
    }

    // Full Constructor
    public ArtifactAnalysis(List<YoloDetection> detections, List<Double> imageEmbedding,
                           List<Double> textEmbedding, List<NamedEntity> entities,
                           Map<String, Double> culturalTags, LocalDateTime analyzedAt) {
        this.detections = detections;
        this.imageEmbedding = imageEmbedding;
        this.textEmbedding = textEmbedding;
        this.entities = entities;
        this.culturalTags = culturalTags;
        this.analyzedAt = analyzedAt;
    }

    // Getters and Setters
    public List<YoloDetection> getDetections() {
        return detections;
    }

    public void setDetections(List<YoloDetection> detections) {
        this.detections = detections;
    }

    public List<Double> getImageEmbedding() {
        return imageEmbedding;
    }

    public void setImageEmbedding(List<Double> imageEmbedding) {
        this.imageEmbedding = imageEmbedding;
    }

    public List<Double> getTextEmbedding() {
        return textEmbedding;
    }

    public void setTextEmbedding(List<Double> textEmbedding) {
        this.textEmbedding = textEmbedding;
    }

    public List<NamedEntity> getEntities() {
        return entities;
    }

    public void setEntities(List<NamedEntity> entities) {
        this.entities = entities;
    }

    public Map<String, Double> getCulturalTags() {
        return culturalTags;
    }

    public void setCulturalTags(Map<String, Double> culturalTags) {
        this.culturalTags = culturalTags;
    }

    public LocalDateTime getAnalyzedAt() {
        return analyzedAt;
    }

    public void setAnalyzedAt(LocalDateTime analyzedAt) {
        this.analyzedAt = analyzedAt;
    }
}

class YoloDetection {
    private String className;
    private double confidence;
    private List<Double> bbox; // [x, y, width, height]

    // Default Constructor
    public YoloDetection() {
    }

    // Constructor
    public YoloDetection(String className, double confidence, List<Double> bbox) {
        this.className = className;
        this.confidence = confidence;
        this.bbox = bbox;
    }

    // Getters and Setters
    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public double getConfidence() {
        return confidence;
    }

    public void setConfidence(double confidence) {
        this.confidence = confidence;
    }

    public List<Double> getBbox() {
        return bbox;
    }

    public void setBbox(List<Double> bbox) {
        this.bbox = bbox;
    }
}

class NamedEntity {
    private String text;
    private String label; // PERSON, LOCATION, ORGANIZATION, DATE, etc.
    private double confidence;

    // Default Constructor
    public NamedEntity() {
    }

    // Constructor
    public NamedEntity(String text, String label, double confidence) {
        this.text = text;
        this.label = label;
        this.confidence = confidence;
    }

    // Getters and Setters
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public double getConfidence() {
        return confidence;
    }

    public void setConfidence(double confidence) {
        this.confidence = confidence;
    }
}
