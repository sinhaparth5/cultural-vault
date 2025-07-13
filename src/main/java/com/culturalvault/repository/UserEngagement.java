/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */

package com.culturalvault.repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * @author parth
 */
public interface UserEngagement {
    String getId(); // userId
    int getTotalInteractions();
    List<String> getUniqueArtifacts();
    int getUniqueArtifactCount();
    List<String> getActions();
    LocalDateTime getLastActivity();
}
