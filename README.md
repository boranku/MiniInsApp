# Social Media Backend API

A Spring Boot-based social media backend API with authentication, post management, comments, likes, and user management features.

## Technologies Used

- **Java 21**
- **Spring Boot 3+**
- **PostgreSQL**
- **Maven**
- **Lombok**
- **JPA/Hibernate**

## Setup Instructions

### 1. Project Initialisation

####Go to https://start.spring.io/index.html.
####Select 
  Project -> Maven
  Language -> Java
  Spring Boot -> 3.4
  Packaging -> Jar
  Configuration -> Yaml
  Java -> 21

####Click on Add Dependencies
####Add
  Spring Web
  Spring Data JPA
  PostgreSQL Driver
  Lombok
  Validation

####Download the zip file
####Extract and open in Intellij IDEA.

### 2. Database Configuration

####Create a PostgreSQL database named `MyApp` (or any name you prefer).

### 3. Application Configuration

####Go to Settings->Compiler->Annotation Processors and click on Enable annotation processing.
####Add environment variables "DB_USERNAME" and "DB_PASSWORD" and set it up according to your database credentials.
####Paste the following content to your `src/main/resources/application.yaml` file:
```
spring:
  application:
    name: app // your app name

  datasource:
    url: jdbc:postgresql://localhost:5432/MyApp
    username: ${DB_USERNAME:}
    password: ${DB_PASSWORD:}
    driver-class-name: org.postgresql.Driver

  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
    properties:
      hibernate:
        format_sql: true

# Token expiration time
app:
  token:
    expiration-hours: 24
```

####If lombok does not work, we need to provide the lombok version manually
  Open your pom.xml file
  Add the following code in your dependencies:
  ```
  <dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <version>your_version</version> //change your_version to your lombok version. //added line
    <scope>provided</scope>         //added line
</dependency>
```
####Add the following code in your annotation proccessors paths:
```
                <annotationProcessorPaths>
                    <path>
                        <groupId>org.projectlombok</groupId>
                        <artifactId>lombok</artifactId>
                        <version>your_version</version> //change your_version to your lombok version. //added line
                    </path>
                </annotationProcessorPaths>
```

####Click on reload maven project from the Maven tab.

### 4.  Build and Run

####When the application starts for the first time, an admin user is automatically created by DataInitializer.java class.
      Username: admin
      Password: admin123
      Role: ADMIN


### 5.  Postman API Testing
####Open Postman
####Import the "MiniIns API.postman_collection.json" file located in the docs folder.
####Test the endpoints according to the flow: signup → login → token related proccesses → logout.


## Assumptions and Constraints

### Authentication & Authorization
####Custom token-based authentication (Spring Security NOT used)
####Token expiration: 24 hours (configurable)
####Role-based access control (ADMIN/USER)
####Automatic token cleanup (hourly)

### Data Validation
####Username: 3-50 characters
####Password: 6-100 characters
####Post description: 1-2000 characters
####Comment text: 1-1000 characters

### Business Rules
####Users can only delete/edit their own posts/comments
####ADMIN can delete any user/post/comment
####Users can delete their own comments
####Post owners can delete any comment on their posts
####One like per user per post
####View count increments on explicit view endpoint call

### Security
####Passwords are hashed with SHA-256 + salt
####Token validation on all protected endpoints
####Public endpoints: login, signup, logout
