/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */

package com.culturalvault.repository;

/**
 *
 * @author parth
 */
public interface GenreStats {
    String getId(); // genre name
    int getCount();
    double getAvgRating();
    int getTotalRatings();
}
