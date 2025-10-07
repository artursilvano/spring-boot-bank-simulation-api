# Spring Boot Bank Simulation API

This project is a **Bank Simulation API** built with **Spring Boot**, providing a robust backend for managing banking operations in a simulated environment. The API is designed for educational purposes, prototyping, and as a starting point for more complex financial applications.

## Features

- **Account Management:** Create and retrieve bank accounts.
- **Transactions:** Transfer funds between accounts.
- **ATM:** Deposit and withdraw funds.
- **Balance Inquiry:** Check account balances and transaction history.
- **RESTful Endpoints:** Clean, RESTful APIs for all operations.
- **Persistence:** Data stored in a relational database using Spring Data JPA.
- **Validation and Error Handling:** Robust input validation and informative error responses.

## Technologies Used

- **Java**
- **Spring Boot**
- **Spring Data JPA**
- **Hibernate**
- **Maven**
- **Docker** (used to run a local database for the project)

## Getting Started

### Prerequisites

- Java 21+
- Maven
- Docker

### Installation & Running

1. **Clone the repository:**
   ```bash
   git clone https://github.com/artursilvano/spring-boot-bank-simulation-api.git
   cd spring-boot-bank-simulation-api
   ```
2. **Build the project:**
   ```bash
   mvn clean install
   ```
3. **Start the local database with Docker Compose:**
   ```bash
   docker compose up
   ```
   Make sure Docker is running before executing the command above. This will start the local database required for the API.

4. **Run the Spring Boot application:**

5. The API will be available at `http://localhost:8080`

### API Endpoints

| Method | Endpoint                       | Description                        |
|--------|--------------------------------|------------------------------------|
| POST   | `v1/account`                   | Create a new account               |
| GET    | `v1/account`                   | Get details of logged account      |
| GET    | `v1/accounts/{accountNumber}`  | Get details of account             |
| POST   | `v1/atm/deposit`               | Deposit funds                      |
| POST   | `v1/atm/withdraw`              | Withdraw funds                     |
| GET    | `v1/transactions/history`      | Get transaction history            |
| POST   | `v1/transactions`              | Transfer funds                     |

> For detailed request/response formats, refer to the API documentation or inspect the controller classes.

## Contributing

Contributions are welcome! Please open an issue to discuss your ideas or bug reports before submitting a pull request.

## Author

- [Artur Silvano](https://github.com/artursilvano)

## Acknowledgements

- Spring Boot documentation
- DevDojo and other community resources
