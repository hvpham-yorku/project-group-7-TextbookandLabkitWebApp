

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
- Added profile endpoint with session-based access control.
- Upgraded login and dashboard UI using Bootstrap.
- Expanded AuthServiceTest with additional login/logout and domain validation tests.
- Verified updated login flow and session handling.


## Feb 28, 2026
### User Story: Edit Student Profile
**Attendees:** Saif  
**Branch:** saif-edit-profile  

Saif:
- Added Edit Profile navigation and created profile-edit.html.
- Implemented GET and POST mappings for profile/edit.
- Preserved immutable Userby creating updated instance.
- Added updateUser() in repository with Stub implementation.
- Moved update logic to AuthService and updated session handling.
- Added JUnit tests for profile update flow.


## Mar 1, 2026  
### User Story: Listing Management + UI Updates  
**Attendees:** Saif, Ayesha, Omar  
**Branch:** saif-listing  

Meeting:

- Reviewed Delivery 1 checklist.
- Assigned presentation roles.
- Planned demo flow.
- Confirmed GitHub tags and final repository state.

Time spent: 1 hours

Saif:

- Added create listing backend logic (repository + service).
- Added delete listing with ownership validation.
- Added controller endpoints for create and delete.
- Updated `my-listings.html`:
  - Add listing form.
  - Delete button with confirmation.
  - Improved layout and Bootstrap styling.
- Refactored listing flow:
  - Separate edit page.
  - Read-only listing table.
  - Added Edit button per listing.
  - Fixed redirect behavior.
- Added JUnit tests for ListingService (create, delete, update, validation).

Time spent: 8 hours

## Mar 8, 2026
**Attendees:** Saif, Ayesha  

Meeting:

- review parts assigned and started working on them



## Mar 9, 2026
### User Story: Browse Listings Preparation
**Attendees:** Saif  
**Branch:** kan-13-browse-listings  

- Reviewed the Listing domain model for course-based listings.
- Updated the Listing class to add courseCode, semester, materialType, condition, and exchangeType.
- Updated stub repository with realistic course-material sample listings.
- Updated repository, service, and controller to pass the new fields.
Updated contributor identity and project log.

Time spent: 3 hours


## Mar 13, 2026
Implemented Browse Listings feature:
repository findAll(), service getAllListings(), /browse controller, browse page, navbar link, empty state, login restriction, updated tests.
Implemented listing detail page, added view-details links from browse listings, styled pages consistently, and updated tests for getAllListings().

Time spent: 5 hours




	




 
  