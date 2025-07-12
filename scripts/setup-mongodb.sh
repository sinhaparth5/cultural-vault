#!/bin/bash

echo "Setting up MongoDB for CulturalVault..."

# Start MongoDB service (adjust for your OS)
# Linux/Ubuntu:
# sudo systemctl start mongod

# macOS with Homebrew:
# brew services start mongodb-community

# Connect to MongoDB and create database
mongosh <<EOF
use cultural_vault

// Create collections with validation
db.createCollection("artifacts", {
   validator: {
      \$jsonSchema: {
         bsonType: "object",
         required: ["title", "category", "imageUrl"],
         properties: {
            title: { bsonType: "string" },
            category: { bsonType: "string" },
            imageUrl: { bsonType: "string" }
         }
      }
   }
})

db.createCollection("users")
db.createCollection("stories")
db.createCollection("user_interactions")

// Create indexes
db.artifacts.createIndex({ "title": "text", "description": "text" })
db.artifacts.createIndex({ "category": 1 })
db.artifacts.createIndex({ "culture": 1 })
db.artifacts.createIndex({ "period": 1 })
db.artifacts.createIndex({ "createdAt": -1 })

db.users.createIndex({ "username": 1 }, { unique: true })
db.users.createIndex({ "email": 1 }, { unique: true })

db.stories.createIndex({ "artifactId": 1 })
db.stories.createIndex({ "userId": 1 })
db.stories.createIndex({ "generatedAt": -1 })

db.user_interactions.createIndex({ "userId": 1, "artifactId": 1 })
db.user_interactions.createIndex({ "timestamp": -1 })

print("MongoDB setup complete!")
