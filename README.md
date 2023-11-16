# Cinema REST API

## Project Overview

This REST API is designed for managing a cinema's seat booking system, where users can view available seats, purchase tickets, and return them if needed. It is implemented using Kotlin with the Spring Boot framework, showcasing endpoints for seat selection, ticket purchasing, and statistics retrieval.

## Features

- **Seat Viewing**: Allows users to view all available seats within the cinema.
- **Ticket Purchasing**: Users can purchase a ticket for a specific seat if it is available.
- **Statistics**: An authorized user can view cinema statistics including revenue, number of available seats, and number of purchased tickets.
- **Ticket Returning**: Users can return their purchased tickets using a unique token provided at the time of purchase.

## Installation

To set up the project environment:

1. Clone the repository:
    ```bash
    git clone https://github.com/kwin555/cinema-rest-practice.git
    ```
2. Navigate to the project directory:
    ```bash
    cd cinema-rest-practice
    ```
3. Build the project using Gradle:
    ```bash
    ./gradlew build
    ```

## Running the Application

Run the application using the following command:

```bash
./gradlew bootRun
```

The server will start, and the API endpoints will be available for interaction.
API Endpoints

    GET /seats: Fetches a list of all available seats in the cinema.
    POST /purchase: Allows a user to purchase a ticket for a specific seat.
    POST /return: Allows a user to return a ticket using its token.
    GET /stats: Retrieves the current statistics of the cinema.


Usage
View Seats

http

GET /seats

## Purchase Ticket

http

POST /purchase
Content-Type: application/json

{
    "row": 5,
    "column": 4
}

## Return Ticket

http

## POST /return
Content-Type: application/json

{
    "token": "unique-ticket-token"
}

## Get Statistics

http

GET /stats?password=super_secret


## Contributing

Contributions to this project are welcome. Please feel free to fork the repository, make changes, and submit a pull request.

## License

This project is open-sourced under the MIT License. See the LICENSE file for more details.


