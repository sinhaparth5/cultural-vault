/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.culturalvault.config;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.culturalvault.model.Artifact;
import com.culturalvault.model.Role;
import com.culturalvault.model.User;
import com.culturalvault.model.UserPreferences;
import com.culturalvault.service.ArtifactService;
import com.culturalvault.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author parth
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements  CommandLineRunner {
 
    private final ArtifactService artifactService;
    private final UserService userService;
    
    @Override
    public void run(String... args) throws Exception {
        log.info("Starting data initialization...");
        
        try {
            // Test MongoDB connection first
            testMongoConnection();
            
            // Initialize sample artifacts if database is empty
            if (artifactService.getTotalCount() == 0) {
                log.info("Database is empty, initializing sample data...");
                initializeSampleArtifacts();
            } else {
                log.info("Database already contains {} artifacts, skipping artifact initialization", 
                        artifactService.getTotalCount());
            }
            
            // Initialize admin user if no users exist
            if (userService.getTotalUserCount() == 0) {
                log.info("No users found, creating initial users...");
                initializeUsers();
            } else {
                log.info("Database already contains {} users, skipping user initialization", 
                        userService.getTotalUserCount());
            }
            
            log.info("Data initialization completed successfully!");
            
        } catch (Exception e) {
            log.error("Error during data initialization: {}", e.getMessage());
            log.warn("Application will continue without sample data. Please check your MongoDB connection.");
            // Don't throw the exception - let the application start without sample data
        }
    }
    
    private void testMongoConnection() {
        log.info("Testing MongoDB connection...");
        try {
            long count = artifactService.getTotalCount();
            log.info("✅ MongoDB connection successful. Current artifact count: {}", count);
        } catch (Exception e) {
            log.error("❌ MongoDB connection failed: {}", e.getMessage());
            throw new RuntimeException("MongoDB connection test failed", e);
        }
    }
    
    private void initializeSampleArtifacts() {
        log.info("Creating sample artifacts...");
        
        try {
            List<Artifact> sampleArtifacts = List.of(
                createArtifact(
                    "Roman Gold Aureus of Augustus",
                    "A magnificent gold coin featuring Emperor Augustus, minted between 27 BC - 14 AD. This coin represents the power and prosperity of the early Roman Empire, showcasing the emperor's portrait with divine attributes.",
                    "COIN",
                    "ROMAN",
                    "ANCIENT",
                    "Gold",
                    "https://upload.wikimedia.org/wikipedia/commons/thumb/6/6a/Augustus_aureus.jpg/256px-Augustus_aureus.jpg"
                ),
                createArtifact(
                    "Greek Red-Figure Amphora",
                    "An elegant amphora decorated with the sophisticated red-figure pottery technique, depicting scenes from Greek mythology. Created around 450 BC during the height of Athenian artistic achievement.",
                    "POTTERY",
                    "GREEK",
                    "ANCIENT",
                    "Ceramic",
                    "https://upload.wikimedia.org/wikipedia/commons/thumb/5/5a/Amphora_red_figure.jpg/256px-Amphora_red_figure.jpg"
                ),
                createArtifact(
                    "Egyptian Canopic Jar of Duamutef",
                    "A limestone canopic jar used in the mummification process, decorated with hieroglyphs and topped with the head of Duamutef, one of the four Sons of Horus who protected the stomach of the deceased.",
                    "SCULPTURE",
                    "EGYPTIAN",
                    "ANCIENT",
                    "Limestone",
                    "https://upload.wikimedia.org/wikipedia/commons/thumb/8/8a/Canopic_jar.jpg/256px-Canopic_jar.jpg"
                ),
                createArtifact(
                    "Byzantine Gold Solidus",
                    "A gold solidus featuring Emperor Justinian I, minted in Constantinople around 540 AD. This coin symbolizes the wealth and power of the Byzantine Empire at its zenith.",
                    "COIN",
                    "BYZANTINE",
                    "MEDIEVAL",
                    "Gold",
                    "https://upload.wikimedia.org/wikipedia/commons/thumb/1/1f/Justinian_solidus.jpg/256px-Justinian_solidus.jpg"
                ),
                createArtifact(
                    "Chinese Tang Dynasty Horse",
                    "A beautiful ceramic horse sculpture from the Tang Dynasty (618-907 AD), representing the importance of horses in Chinese culture and the Silk Road trade networks.",
                    "SCULPTURE",
                    "CHINESE",
                    "MEDIEVAL",
                    "Ceramic",
                    "https://upload.wikimedia.org/wikipedia/commons/thumb/2/2c/Tang_horse.jpg/256px-Tang_horse.jpg"
                ),
                createArtifact(
                    "Viking Silver Arm Ring",
                    "A twisted silver arm ring used as currency and jewelry by Vikings in the 10th century. These rings demonstrate the Vikings' extensive trade networks and craftsmanship skills.",
                    "JEWELRY",
                    "VIKING",
                    "MEDIEVAL",
                    "Silver",
                    "https://upload.wikimedia.org/wikipedia/commons/thumb/b/b4/Viking_arm_ring.jpg/256px-Viking_arm_ring.jpg"
                )
            );
            
            int successCount = 0;
            for (Artifact artifact : sampleArtifacts) {
                try {
                    artifactService.saveArtifact(artifact);
                    successCount++;
                    log.debug("✅ Created artifact: {}", artifact.getTitle());
                } catch (Exception e) {
                    log.warn("❌ Failed to create artifact {}: {}", artifact.getTitle(), e.getMessage());
                }
            }
            
            log.info("✅ Successfully created {}/{} sample artifacts", successCount, sampleArtifacts.size());
            
        } catch (Exception e) {
            log.error("Error creating sample artifacts: {}", e.getMessage());
            throw e;
        }
    }
    
    private Artifact createArtifact(String title, String description, String category, 
                                  String culture, String period, String material, String imageUrl) {
        Artifact artifact = new Artifact();
        artifact.setTitle(title);
        artifact.setDescription(description);
        artifact.setCategory(category);
        artifact.setCulture(culture);
        artifact.setPeriod(period);
        artifact.setMaterial(material);
        artifact.setImageUrl(imageUrl);
        artifact.setSource("SAMPLE_DATA");
        artifact.setCreatedAt(LocalDateTime.now());
        artifact.setUpdatedAt(LocalDateTime.now());
        
        // Add some sample metadata
        artifact.setMetadata(Map.of(
            "creator", "Unknown",
            "currentLocation", "Museum Collection",
            "acquisitionDate", "Various",
            "dimensions", "Varies",
            "condition", "Good",
            "provenance", "Museum acquisition"
        ));
        
        return artifact;
    }
    
    private void initializeUsers() {
        log.info("Creating initial users...");
        
        try {
            // Create admin user
            User admin = new User();
            admin.setUsername("admin");
            admin.setEmail("admin@culturalvault.com");
            admin.setPassword("admin123"); // Will be encoded by the service
            admin.setFirstName("CulturalVault");
            admin.setLastName("Administrator");
            admin.setRole(Role.ADMIN);
            
            // Set default preferences
            UserPreferences adminPreferences = new UserPreferences();
            adminPreferences.setFavoriteGenres(List.of("HISTORICAL", "EDUCATIONAL"));
            adminPreferences.setInterests(List.of("ANCIENT_ROME", "GREEK_ART", "EGYPTIAN_CULTURE"));
            admin.setPreferences(adminPreferences);
            
            userService.createUser(admin);
            log.info("✅ Admin user created successfully - Username: admin, Password: admin123");
            
            // Create a regular test user
            User testUser = new User();
            testUser.setUsername("testuser");
            testUser.setEmail("test@culturalvault.com");
            testUser.setPassword("test123");
            testUser.setFirstName("Test");
            testUser.setLastName("User");
            testUser.setRole(Role.USER);
            
            UserPreferences testPreferences = new UserPreferences();
            testPreferences.setFavoriteGenres(List.of("ADVENTURE", "MYSTERY"));
            testPreferences.setInterests(List.of("MEDIEVAL_ART", "VIKING_CULTURE"));
            testUser.setPreferences(testPreferences);
            
            userService.createUser(testUser);
            log.info("✅ Test user created successfully - Username: testuser, Password: test123");
            
        } catch (Exception e) {
            log.error("Error creating initial users: {}", e.getMessage());
            throw e;
        }
    }
}
