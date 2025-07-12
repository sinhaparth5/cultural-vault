/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */

package com.culturalvault.repository;

import java.util.List;

/**
 *
 * @author parth
 */
public interface ArtifactStoryStats {

    String getId(); // artifactId
    int getStoryCount();
    double getAvgRating();
    List<String> getGenres();
    List<String> getLengths();
}
