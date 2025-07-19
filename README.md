# CulturalVault 🏛️

**AI-Powered Cultural Heritage Storytelling Platform**

CulturalVault is a full-stack web application that combines cultural heritage preservation with cutting-edge AI technology. Users can explore historical artifacts, discover their stories through AI-generated narratives, and receive personalized recommendations based on their interests.

![CulturalVault Demo](https://via.placeholder.com/800x400/7c3aed/ffffff?text=CulturalVault+Demo)

## 🌟 Features

### 🎨 **Frontend (Vue.js)**
- **Modern UI/UX** with Vue.js 3 + Composition API
- **Responsive Design** with Tailwind CSS
- **Advanced Search & Filtering** by category, culture, period
- **User Authentication** and profile management
- **Personalized Recommendations** based on user preferences
- **Interactive Artifact Browsing** with detailed views
- **Real-time User Interactions** (likes, saves, shares)

### ⚙️ **Backend (Spring Boot)**
- **RESTful API** with comprehensive endpoints
- **MongoDB Integration** with cloud database support
- **JWT Authentication** and role-based authorization
- **Advanced Search** with text indexing and filtering
- **Recommendation Engine** with multiple algorithms
- **User Interaction Tracking** for analytics
- **File Storage** ready for Cloudflare R2 integration

### 🤖 **AI Integration** (Planned)
- **YOLO v8** for artifact image analysis
- **NLP Models** for story generation (Hugging Face)
- **Embedding Storage** for similarity matching
- **Cultural Context Analysis** with entity extraction

### 📊 **Analytics & Data**
- **User Behavior Tracking** (views, likes, saves)
- **Popular Content Analysis** and trending artifacts
- **Engagement Metrics** and user statistics
- **Content Performance** monitoring

## 🛠️ Technology Stack

### Backend
- **Java 21** with Spring Boot 3.2+
- **MongoDB** with Spring Data MongoDB
- **Spring Security** with JWT authentication
- **Maven** for dependency management
- **Lombok** for code generation

### Frontend
- **Vue.js 3** with Composition API
- **Vite** for fast development and building
- **Vue Router** for navigation
- **Pinia** for state management
- **Axios** for API communication
- **Tailwind CSS** for styling
- **Headless UI** for accessible components

### Database & Storage
- **MongoDB Atlas** (cloud database)
- **Cloudflare R2** (object storage - planned)

### AI & ML (Planned)
- **Python** microservice with Flask/FastAPI
- **YOLO v8** for computer vision
- **Hugging Face Transformers** for NLP
- **OpenCV** for image processing

## 🚀 Quick Start

### Prerequisites
- **Java 21+**
- **Node.js 18+** and npm
- **MongoDB Atlas** account or local MongoDB
- **Git**

### Backend Setup

1. **Clone the repository**
```bash
git clone <repository-url>
cd cultural-vault
```

2. **Set environment variables**
```bash
export MONGODB_URI="mongodb+srv://username:password@cluster.mongodb.net/cultural_vault"
```

3. **Run the Spring Boot application**
```bash
# Using Maven wrapper
./mvnw spring-boot:run

# Or compile and run
./mvnw clean package
java -jar target/cultural-vault-0.0.1-SNAPSHOT.jar
```

4. **Verify backend is running**
```bash
curl http://localhost:8080/api/health
```

### Frontend Setup

1. **Navigate to frontend directory**
```bash
cd cultural-vault-frontend
```

2. **Install dependencies**
```bash
npm install
```

3. **Start development server**
```bash
npm run dev
```

4. **Access the application**
- Frontend: http://localhost:5173
- Backend API: http://localhost:8080/api

### Default Users
The application creates default users on first startup:
- **Admin**: `admin` / `admin123`
- **Test User**: `testuser` / `test123`

## 📁 Project Structure

```
cultural-vault/
├── 📂 backend/                          # Spring Boot Backend
│   ├── 📂 src/main/java/com/culturalvault/
│   │   ├── 📂 config/                   # Configuration classes
│   │   ├── 📂 controller/               # REST controllers
│   │   ├── 📂 service/                  # Business logic
│   │   ├── 📂 repository/               # Data access layer
│   │   ├── 📂 model/                    # Entity models
│   │   └── 📄 CulturalVaultApplication.java
│   ├── 📂 src/main/resources/
│   │   ├── 📄 application.yml           # Configuration
│   │   └── 📂 static/                   # Static resources
│   └── 📄 pom.xml                       # Maven dependencies
│
├── 📂 frontend/                         # Vue.js Frontend
│   ├── 📂 src/
│   │   ├── 📂 components/               # Vue components
│   │   │   ├── 📂 artifacts/            # Artifact-related components
│   │   │   ├── 📂 common/               # Shared components
│   │   │   └── 📂 user/                 # User-related components
│   │   ├── 📂 views/                    # Page components
│   │   ├── 📂 stores/                   # Pinia stores
│   │   ├── 📂 services/                 # API services
│   │   ├── 📂 composables/              # Vue composables
│   │   └── 📂 router/                   # Vue Router config
│   ├── 📄 package.json                  # npm dependencies
│   ├── 📄 vite.config.js                # Vite configuration
│   └── 📄 tailwind.config.js            # Tailwind CSS config
│
├── 📂 ai-service/                       # AI Microservice (Planned)
│   ├── 📂 models/                       # ML models
│   ├── 📂 services/                     # AI processing services
│   ├── 📄 app.py                        # Flask/FastAPI app
│   └── 📄 requirements.txt              # Python dependencies
│
└── 📄 README.md                         # This file
```

## 🔗 API Endpoints

### Public Endpoints
```
GET    /api/health                       # Application health
GET    /api/artifacts                    # List all artifacts
GET    /api/artifacts/{id}               # Get artifact by ID
GET    /api/artifacts/search?q={query}   # Search artifacts
GET    /api/artifacts/category/{category} # Filter by category
GET    /api/artifacts/random             # Random artifacts
GET    /api/recommendations/popular      # Popular recommendations
```

### Authenticated Endpoints
```
GET    /api/users/profile                # Current user profile
PUT    /api/users/profile                # Update profile
POST   /api/users/profile/favorites/{id} # Add favorite
POST   /api/interactions/record          # Record interaction
GET    /api/recommendations/for-me       # Personalized recommendations
```

### Admin Endpoints
```
GET    /api/users                        # List all users
GET    /api/users/search                 # Search users
GET    /api/interactions/statistics      # Interaction analytics
```

## 🧪 Testing

### Backend Testing
```bash
# Run all tests
./mvnw test

# Run with coverage
./mvnw test jacoco:report

# Integration tests
./mvnw verify
```

### API Testing with curl
```bash
# Health check
curl http://localhost:8080/api/health

# Get artifacts
curl http://localhost:8080/api/artifacts

# Search artifacts
curl "http://localhost:8080/api/artifacts/search?q=roman"

# Authenticated request
curl -u admin:admin123 http://localhost:8080/api/users/profile
```

### Frontend Testing
```bash
cd cultural-vault-frontend

# Run unit tests
npm run test:unit

# Run tests with coverage
npm run test:coverage

# Lint code
npm run lint

# Format code
npm run format
```

## 🚀 Deployment

### Docker Deployment
```bash
# Build and run with Docker Compose
docker-compose up -d

# Or build individual containers
docker build -t cultural-vault-backend .
docker build -t cultural-vault-frontend ./frontend
```

### Production Configuration
1. **Environment Variables**
```bash
export MONGODB_URI="production-mongodb-url"
export JWT_SECRET="production-jwt-secret"
export R2_ACCESS_KEY_ID="cloudflare-access-key"
export R2_SECRET_ACCESS_KEY="cloudflare-secret-key"
```

2. **Build for Production**
```bash
# Backend
./mvnw clean package -Pprod

# Frontend
cd frontend
npm run build
```

## 🔧 Configuration

### Backend Configuration (application.yml)
```yaml
spring:
  data:
    mongodb:
      uri: ${MONGODB_URI}
  security:
    user:
      name: admin
      password: admin123

app:
  jwt:
    secret: ${JWT_SECRET}
    expiration: 86400000
```

### Frontend Configuration (vite.config.js)
```javascript
export default defineConfig({
  plugins: [vue()],
  server: {
    proxy: {
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true,
      }
    }
  }
})
```

## 📚 Sample Data

The application includes sample cultural artifacts:
- **Roman Gold Aureus** of Augustus (27 BC - 14 AD)
- **Greek Red-Figure Amphora** (450 BC)
- **Egyptian Canopic Jar** with hieroglyphs
- **Byzantine Gold Solidus** of Justinian I
- **Chinese Tang Dynasty Horse** sculpture
- **Viking Silver Arm Ring** (10th century)

Each artifact includes:
- High-quality images
- Detailed descriptions
- Cultural and historical context
- Metadata (materials, dimensions, provenance)

## 🤝 Contributing

1. **Fork the repository**
2. **Create a feature branch**
```bash
git checkout -b feature/amazing-feature
```
3. **Commit your changes**
```bash
git commit -m 'Add amazing feature'
```
4. **Push to the branch**
```bash
git push origin feature/amazing-feature
```
5. **Open a Pull Request**

### Development Guidelines
- Follow **Java conventions** for backend code
- Use **Vue.js Composition API** for frontend components
- Write **comprehensive tests** for new features
- Update **documentation** for API changes
- Use **semantic commit messages**

## 🔮 Roadmap

### Phase 1: Core Platform ✅
- [x] Spring Boot REST API
- [x] MongoDB integration
- [x] User authentication
- [x] Basic artifact management
- [x] Vue.js frontend foundation

### Phase 2: Enhanced Features 🚧
- [ ] Advanced search with faceted filtering
- [ ] User interaction analytics
- [ ] Recommendation algorithms
- [ ] Mobile-responsive design
- [ ] Performance optimization

### Phase 3: AI Integration 📋
- [ ] YOLO v8 image analysis
- [ ] NLP story generation
- [ ] Cultural context extraction
- [ ] Similarity matching
- [ ] Automated content tagging

### Phase 4: Advanced Features 📋
- [ ] Cloudflare R2 file storage
- [ ] External API integration (Europeana, Smithsonian)
- [ ] Real-time notifications
- [ ] Social features (sharing, comments)
- [ ] Multi-language support

### Phase 5: Production Ready 📋
- [ ] Comprehensive monitoring
- [ ] Performance optimization
- [ ] Security hardening
- [ ] CI/CD pipeline
- [ ] Load testing

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🙏 Acknowledgments

- **Spring Boot** team for the excellent framework
- **Vue.js** community for the reactive framework
- **MongoDB** for flexible document storage
- **Tailwind CSS** for utility-first styling
- **Cultural institutions** providing open access to artifacts
- **Open source community** for incredible tools and libraries

## 📞 Support

- **Documentation**: Check this README and inline code comments
- **Issues**: Create an issue on GitHub for bugs or feature requests
- **Discussions**: Use GitHub Discussions for questions and ideas

## 🔗 Links

- **Live Demo**: [Coming Soon]
- **API Documentation**: http://localhost:8080/swagger-ui.html (when running)
- **Frontend Demo**: http://localhost:5173 (development)

---

**Built with ❤️ for cultural heritage preservation and AI innovation**

*CulturalVault - Where History Meets Technology* 🏛️✨