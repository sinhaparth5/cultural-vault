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
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.culturalvault.model.User;
import com.culturalvault.model.Role;

/**
 *
 * @author parth
 */

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    // Authentication queries
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Optional<User> findByUsernameOrEmail(String username, String email);
    
    // Check existence
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    
    // Find by role
    Page<User> findByRole(Role role, Pageable pageable);
    List<User> findByRole(Role role);
    
    // Find active users
    Page<User> findByEnabled(boolean enabled, Pageable pageable);
    
    // Find users with recent activity
    List<User> findByLastLoginAtAfter(LocalDateTime dateTime);
    
    // Find users by creation date range
    List<User> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    // Search users by name
    @Query("{ $or: [ " +
           "{ firstName: { $regex: ?0, $options: 'i' } }, " +
           "{ lastName: { $regex: ?0, $options: 'i' } }, " +
           "{ username: { $regex: ?0, $options: 'i' } } " +
           "] }")
    Page<User> searchByName(String searchTerm, Pageable pageable);
    
    // Find users with favorite artifacts
    @Query("{ 'favoriteArtifacts': { $exists: true, $not: { $size: 0 } } }")
    List<User> findUsersWithFavorites();
    
    // Find users who favorited specific artifact
    @Query("{ 'favoriteArtifacts': ?0 }")
    List<User> findByFavoriteArtifactsContaining(String artifactId);
    
    // Count queries
    long countByRole(Role role);
    long countByEnabled(boolean enabled);
    long countByCreatedAtAfter(LocalDateTime dateTime);
    
    // Find users by preferences
    @Query("{ 'preferences.favoriteGenres': { $in: ?0 } }")
    List<User> findByPreferredGenres(List<String> genres);
    
    @Query("{ 'preferences.interests': { $in: ?0 } }")
    List<User> findByInterests(List<String> interests);
}
