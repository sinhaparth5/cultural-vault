/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.culturalvault.model;

/**
 *
 * @author parth
 */
public class GenerationParams {

    private String model; // GPT-2, DistilBERT, etc.
    private double temperature;
    private int maxTokens;
    private String prompt;

    // Default Constructor
    public GenerationParams() {
    }

    // Constructor
    public GenerationParams(String model, double temperature, int maxTokens, String prompt) {
        this.model = model;
        this.temperature = temperature;
        this.maxTokens = maxTokens;
        this.prompt = prompt;
    }

    // Getters and Setters
    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public int getMaxTokens() {
        return maxTokens;
    }

    public void setMaxTokens(int maxTokens) {
        this.maxTokens = maxTokens;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }
}
