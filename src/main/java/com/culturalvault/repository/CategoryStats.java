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
public interface CategoryStats {
    String getId(); // category name
    int getCount();
    List<String> getCultures();
    List<String> getPeriods();
}
