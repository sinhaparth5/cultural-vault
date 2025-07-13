/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.culturalvault.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.culturalvault.model.Role;
import com.culturalvault.model.User;
import com.culturalvault.model.UserPreferences;
import com.culturalvault.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author parth
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("Loading user by username: {}", username);
        return userRepository.findByUsernameOrEmail(username, username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }

    public User createUser(User user) {
        log.info("Creating new user: {}", user.getUsername());

        // Validation
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Username already exists: " + user.getUsername());
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already exists: " + user.getEmail());
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(user.getRole() != null ? user.getRole() : Role.USER);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        user.setEnabled(true);

        if (user.getPreferences() == null) {
            user.setPreferences(new UserPreferences());
        }

        return userRepository.save(user);
    }

    public Optional<User> getUserById(String id) {
        log.debug("Fetching user by ID: {}", id);
        return userRepository.findById(id);
    }

    public Optional<User> getUserByUsername(String username) {
        log.debug("Fetching user by username: {}", username);
        return userRepository.findByUsername(username);
    }

    public Optional<User> getUserByEmail(String email) {
        log.debug("Fetching user by email: {}", email);
        return userRepository.findByEmail(email);
    }

    public User updateUser(String id, User updatedUser) {
        log.info("Updating user with id: {}", id);
        return userRepository.findById(id)
            .map(user -> {
                user.setFirstName(updatedUser.getFirstName());
                user.setLastName(updatedUser.getLastName());
                user.setEmail(updatedUser.getEmail());
                user.setPreferences(updatedUser.getPreferences());
                user.setUpdatedAt(LocalDateTime.now());

                return userRepository.save(user);
            })
            .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    public User updateUserPreferences(String userId, UserPreferences preferences) {
        log.info("Updating preferences for user: {}", userId);

        return userRepository.findById(userId)
            .map(user -> {
                user.setPreferences(preferences);
                user.setUpdatedAt(LocalDateTime.now());
                return userRepository.save(user);
            })
            .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
    }

    public void updateLastLogin(String userId) {
        log.debug("Updating last login for user: {}", userId);
        userRepository.findById(userId)
            .ifPresent(user -> {
                user.setLastLoginAt(LocalDateTime.now());
                userRepository.save(user);
            });
    }

    public void deleteUser(String id) {
        log.info("Deleting user with id: {}", id);
        userRepository.deleteById(id);
    }

    public Page<User> getAllUsers(Pageable pageable) {
        log.debug("Fetching all users with pagination");
        return userRepository.findAll(pageable);
    }

    public Page<User> searchUsers(String searchTerm, Pageable pageable) {
        log.debug("Searching users with term: {}", searchTerm);
        return userRepository.searchByName(searchTerm, pageable);
    }

    public Page<User> getUsersByRole(Role role, Pageable pageable) {
        log.debug("Fetching users by role: {}", role);
        return userRepository.findByRole(role, pageable);
    }

    public List<User> getActiveUsers() {
        log.debug("Fetching active users");
        return userRepository.findByEnabled(true, null).getContent();
    }

    public List<User> getRecentUsers(int days) {
        log.debug("Fetching users from last {} days", days);
        LocalDateTime since = LocalDateTime.now().minusDays(days);
        return userRepository.findByCreatedAtBetween(since, LocalDateTime.now());
    }

    public User addFavoriteArtifact(String userId, String artifactId) {
        log.info("Adding favorite artifact {} for user {}", artifactId, userId);
        
        return userRepository.findById(userId)
                .map(user -> {
                    if (user.getFavoriteArtifacts() == null) {
                        user.setFavoriteArtifacts(List.of(artifactId));
                    } else if (!user.getFavoriteArtifacts().contains(artifactId)) {
                        user.getFavoriteArtifacts().add(artifactId);
                    }
                    user.setUpdatedAt(LocalDateTime.now());
                    return userRepository.save(user);
                })
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
    }
    
    public User removeFavoriteArtifact(String userId, String artifactId) {
        log.info("Removing favorite artifact {} for user {}", artifactId, userId);
        
        return userRepository.findById(userId)
                .map(user -> {
                    if (user.getFavoriteArtifacts() != null) {
                        user.getFavoriteArtifacts().remove(artifactId);
                        user.setUpdatedAt(LocalDateTime.now());
                        return userRepository.save(user);
                    }
                    return user;
                })
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
    }
    
    public boolean isFavoriteArtifact(String userId, String artifactId) {
        return userRepository.findById(userId)
                .map(user -> user.getFavoriteArtifacts() != null && 
                           user.getFavoriteArtifacts().contains(artifactId))
                .orElse(false);
    }
    
    public List<User> getUsersWithFavorites() {
        log.debug("Fetching users with favorite artifacts");
        return userRepository.findUsersWithFavorites();
    }
    
    public long getTotalUserCount() {
        return userRepository.count();
    }
    
    public long getActiveUserCount() {
        return userRepository.countByEnabled(true);
    }
    
    public long getUserCountByRole(Role role) {
        return userRepository.countByRole(role);
    }
    
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }
    
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
    
    public boolean changePassword(String userId, String oldPassword, String newPassword) {
        log.info("Changing password for user: {}", userId);
        
        return userRepository.findById(userId)
                .map(user -> {
                    if (passwordEncoder.matches(oldPassword, user.getPassword())) {
                        user.setPassword(passwordEncoder.encode(newPassword));
                        user.setUpdatedAt(LocalDateTime.now());
                        userRepository.save(user);
                        return true;
                    }
                    return false;
                })
                .orElse(false);
    }
}
