/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.culturalvault.model;

import java.util.List;

/**
 *
 * @author parth
 */
public class UserPreferences {

    private List<String> favoriteGenres; // ADVENTURE, HISTORICAL, MYSTERY, etc.
    private StoryLength preferredLength = StoryLength.MEDIUM;
    private List<String> interests; // ANCIENT_ROME, RENAISSANCE_ART, etc.
    private String language = "en";

    // Default Constructor
    public UserPreferences() {
        this.favoriteGenres = List.of("HISTORICAL", "ADVENTURE");
        this.interests = List.of();
    }

    // Constructor with essential fields
    public UserPreferences(List<String> favoriteGenres, StoryLength preferredLength,
                          List<String> interests, String language) {
        this.favoriteGenres = favoriteGenres;
        this.preferredLength = preferredLength;
        this.interests = interests;
        this.language = language;
    }

    // Getters and Setters
    public List<String> getFavoriteGenres() {
        return favoriteGenres;
    }

    public void setFavoriteGenres(List<String> favoriteGenres) {
        this.favoriteGenres = favoriteGenres;
    }

    public StoryLength getPreferredLength() {
        return preferredLength;
    }

    public void setPreferredLength(StoryLength preferredLength) {
        this.preferredLength = preferredLength;
    }

    public List<String> getInterests() {
        return interests;
    }

    public void setInterests(List<String> interests) {
        this.interests = interests;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
