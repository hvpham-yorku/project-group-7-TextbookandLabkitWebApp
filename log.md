

## Feb 16, 2026
### User Story: User Sign-In
**Attendees:** Saif  **

 Starting User domain class implementation.
- Branch: saif-auth
- Status: In Progress

Created UserRepository + StubUserRepository with sample users.


##Feb 20, 2026
### User story sign in
**Attendees:** Saif  


implemented AuthService login logic.

Added JUnit tests for login scenarios.
 All tests passing. 7:34am
 
 Built Swing LoginFrame and connected to AuthService.

 didnt stage userRespirotery and stubuser respirotery on feb 16
 
 -branch: saif-auth
 time spent 4 days
 
## Feb 22, 2026
### User Story: User Sign-Out & Session Management
**Attendees:** Saif  


- Branch: saif-log-in-out
- Status: In Progress
- Tasks Planned for Today:
  - KAN-34: Add session handling (currentUser variable) - *Estimated time: 1 hour* (done)
  *Actual: 30 minutes*
  - KAN-33: Implement logout functionality in AuthService - *Estimated time: 1 hour*(done)
  *Actual: 15 minutes*
  - KAN-36: Add Logout button to GUI - *Estimated time: 2 hours*
  - KAN-37: Write unit tests for logout - *Estimated time: 1.5 hours*
  

## Feb 24, 2026

**Branch:** saif-login-logot
**Attendees:** Saif  

- Continued work on Login/Logout feature.
- Migrating project from Swing to Spring Boot web architecture.
- Restructuring project to Maven format for ITR1 compliance.

## Feb 25, 2026
### User Story: Web-Based Authentication (Spring Boot Migration)
**Attendees:** Saif  
**Branch:** saif-log-in-out  

- Successfully migrated project from Swing desktop GUI to Spring Boot web application.
- Converted project structure to Maven (pom.xml, src/main/java, src/test/java).
- Implemented LoginController with HTTP GET/POST endpoints.
- Connected AuthService and StubUserRepository to web layer.
- Implemented session-based authentication using HttpSession.
- Added Thymeleaf templates: login.html and dashboard.html.
- Verified full login flow:
  - Invalid email rejected (@yorku.ca required)
  - Wrong password rejected
  - Successful login redirects to dashboard
  - Logout invalidates session
  - Direct access to dashboard without login is blocked
- Cleaned repository:
  - Removed old Swing files
  - Removed duplicate project folders
- Merged branch into main successfully.

- next waiting for omar to respond and start participating and waiting for ayesha to finish her part while working on the next part which is created a user profile page


## Feb 25, 2026
### User Story: Listing Domain Model
**Attendees:** Ayesha  

- Implemented `Listing` domain class.
- Added fields: id, title, courseCode, price, sellerEmail, sold.
- Included getters and setters for editable fields.
- Added `markSold()` method to update listing status.
- Structured model to support future listing management features.




## Feb 27, 2026
### User Story: Authentication Domain Update + Profile Page
**Attendees:** Saif, ayesha  
**Branch:** main  

Saif:
- Updated authentication domain from `@yorku.ca` to `@my.yorku.ca`.
- Modified AuthService validation logic and updated StubUserRepository test users.
- Updated LoginController error messaging.
- Added `/profile` endpoint with session-based access control.
- Upgraded login and dashboard UI using Bootstrap.
- Expanded AuthServiceTest with additional login/logout and domain validation tests.
- Verified updated login flow and session handling.


	




 
  