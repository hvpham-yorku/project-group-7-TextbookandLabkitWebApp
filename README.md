# YorkU Circle
## Textbook & Course Materials Exchange Platform

Rebook YorkU is a Java Spring Boot web application for York University students to buy, sell, request, and exchange course materials within the university community.

The platform allows students to browse listings, manage their own listings, contact sellers, track exchanges, request missing materials, and interact with other users in a York-only environment.

---

## Project Overview

This application supports:

- York University student sign up and login (`@my.yorku.ca` email restriction)
- Login and logout with session-based authentication
- User profile viewing and editing
- Browse all listings with search and sorting
- Create, edit, and delete personal listings
- Listing details page
- Contact seller flow
- Seller inbox for received contact messages
- Transaction tracking for exchanges
- Material request system with demand board insights
- General chat room
- Feedback and ratings for users
- Blocking and unblocking users
- Image upload for listings
- Support for both stub data mode and PostgreSQL database mode

---

## Main Features

### 1. Authentication
- Sign up with a York University email
- Log in and log out
- Session-based access control for protected pages

### 2. Profile Management
- View personal profile
- Edit profile details such as:
  - name
  - phone number
  - about me
  - program
  - campus

### 3. Listings
- Create listings for:
  - textbooks
  - lab kits
  - notes
  - other course materials
- Add listing details such as:
  - title
  - description
  - price
  - course code
  - semester
  - material type
  - condition
  - exchange type
  - ISBN
  - bookstore price
  - image
- Edit and delete own listings
- View listing details
- Browse all listings
- Search listings by keyword, course, or ISBN
- Sort listings by:
  - newest
  - price
  - course
  - condition

### 4. Course Materials
- Search by course code
- View seeded course materials for a course
- View matching listings
- Submit requests for missing materials
- See demand board insights and recent requests

### 5. Messaging and Inbox
- Contact sellers through listing pages
- Sellers receive contact messages in their inbox
- Inbox supports transaction starting from a buyer message

### 6. Transactions
- Start an exchange from a contact message
- Track exchange progress through the transactions page
- Confirm, cancel, and manage exchanges
- Report issues on transactions

### 7. Community Features
- General chat room
- Leave feedback and ratings for users
- Block and unblock other users

---

## Technologies Used

- Java 21
- Maven
- Spring Boot 3.2.5
- Spring MVC
- Thymeleaf
- Spring JDBC
- PostgreSQL
- JUnit / Spring Boot Test

---

## Project Structure

- `src/main/java/com/example/demo/domain` — domain models
- `src/main/java/com/example/demo/repository` — repository layer
- `src/main/java/com/example/demo/service` — business logic
- `src/main/java/com/example/demo/web` — controllers
- `src/main/resources/templates` — Thymeleaf HTML templates
- `src/main/resources/static/css` — CSS styling
- `src/main/resources/db` — database schema and seed data
- `docs/ITR0` — iteration 0 planning documents

---

## Profiles

The project supports two runtime modes:

### Stub Profile
- Active by default
- Uses in-memory stub repositories
- No database setup required

### DB Profile
- Uses PostgreSQL with JDBC repositories
- Loads schema and seed data from:
  - `src/main/resources/db/schema.sql`
  - `src/main/resources/db/data.sql`

---

## How to Run the Project

### Option 1: Run in Stub Mode (default)

This is the easiest way to run the app.

1. Open the project in your IDE
2. Run:

```bash
mvn clean install
mvn spring-boot:run

Open your browser and go to:
http://localhost:8080

### Option 2: Run in Database Mode (PostgreSQL)

Use this mode if you want to run the app with the PostgreSQL database.

#### Prerequisites
- PostgreSQL installed and running  
- A database created for the project  
- Your database username and password configured in `application-db.properties`

#### Example Database Configuration

Make sure your `src/main/resources/application-db.properties` file contains:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/rebook
spring.datasource.username=postgres
spring.datasource.password=YOUR_PASSWORD
spring.datasource.driver-class-name=org.postgresql.Driver
spring.sql.init.mode=always
spring.sql.init.schema-locations=classpath:db/schema.sql
spring.sql.init.data-locations=classpath:db/data.sql

Steps
Start PostgreSQL server
Create the database:
CREATE DATABASE rebook;
Update your database credentials in application-db.properties
Open the project in your IDE
Run the application using the db profile:
mvn spring-boot:run -Dspring-boot.run.profiles=db
Open your browser and go to:
http://localhost:8080



\## Team Information



Course: EECS 2311    

Team #: 7



Team Members:

* Saif Alaleeli

\-  Omar 

\- Ayesha

