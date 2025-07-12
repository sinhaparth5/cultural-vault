/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */

package com.culturalvault.model;

/**
 *
 * @author parth
 */
public enum StoryLength {
    SHORT(100, 200),    // 100-200 words
    MEDIUM(300, 500),   // 300-500 words
    LONG(600, 1000);    // 600-1000 words
    
    private final int minWords;
    private final int maxWords;
    
    StoryLength(int minWords, int maxWords) {
        this.minWords = minWords;
        this.maxWords = maxWords;
    }
    
    public int getMinWords() {
        return minWords;
    }
    
    public int getMaxWords() {
        return maxWords;
    }
}
