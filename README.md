# Community Connection App

## Getting Started

### Prerequisites

- Java Development Kit (JDK) 8 or higher
- Gradle
- MySQL or any other compatible relational database

### Installation

1. Clone the repository to your local machine.
   git clone https://github.com/ArmanVardanyann/community_connection_app.git

2. Navigate to the project directory.
   cd community-connection-app

3. Build the application using Gradle.
   ./gradlew build

4. Configure the database connection settings in the `application.properties` file located in the `src/main/resources` directory.

5. Run the application.
   ./gradlew bootRun

## API Documentation

The microservice provides the following APIs for managing community connections and parking space bookings:

1. `POST /api/users` - Create a new user account.
2. `GET /api/users/{userId}` - Retrieve user information by ID.
3. `PUT /api/users/{userId}` - Update user information.
4. `DELETE /api/users/{userId}` - Delete a user account.
5. `POST /api/parkings` - Create a new parking space.
6. `GET /api/parkings/{spaceId}` - Retrieve parking space information by ID.
7. `PUT /api/parkings/{spaceId}` - Update parking space information.
8. `DELETE /api/parkings/{spaceId}` - Delete a parking space.
9. `POST /api/bookings/book` - Create a new parking space booking.
10. `GET /api/bookings/{bookingId}` - Retrieve booking information by ID.
11. `PUT /api/bookings/{bookingId}` - Update booking information.
12. `DELETE /api/bookings/{bookingId}` - Delete a booking.

## Contact

If you have any questions or need further assistance, please contact armanvardanyan200@gmail.com.



