# Order Management System

The Order Management System (OMS) is designed to streamline and automate the process of managing orders for small to medium-sized enterprises (SMEs). Written in Java and utilizing MongoDB as its database, the system provides a robust and scalable solution to handle order processing, tracking, and fulfillment efficiently.

## Core Features (in Progress)

- **Order Tracking**: Allows users to track the status of orders in real-time, from placement through to delivery.
- **Inventory Management**: Automatically updates inventory levels based on order placements and cancellations.
- **Customer Management**: Keeps track of customer information, order history, and preferences to enhance customer service.
- **Reporting**: Generates detailed reports on sales, customer behavior, and inventory status to aid in decision-making.

## Getting Started

These instructions will get your project up and running on your local machine for development and testing purposes.

### Prerequisites

Follow these instructions to get your App up and running on your local machine for development and testing purposes.

```text
Java JDK 11 or higher
Maven (to manage project dependencies and builds)
MongoDB (as the system's database)
```

### Installing

A step-by-step guide to getting a development environment running.

1. **Clone the repository**

```bash
git clone https://github.com/Hamza-cpp/java-mongo.git
```

2. **Navigate to the project directory**

```bash
cd ./java-mongo
```

3. **Build the project (with Maven)**

```bash
mvn clean install
```

4. **Run the application**

```bash
java -jar ./target/java-mongo-0.0.1-shaded.jar
```

## Project Structure

Outline of the primary directories and files, illustrating their purpose and relationships.

```bash
src
├── main
│   ├── java
│   │   └── com
│   │       └── hamza_ok
│   │           ├── config
│   │           ├── mappers
│   │           ├── models
│   │           ├── repositories
│   │           └── App.java
│   └── resources
└── test
    └── java
        └── com
            └── hamza_ok
```

- **models:** Contains domain entities.

- **repositories:** Data access layer with generic and entity-specific interfaces/implementations.

- **servicies:** Business logic.
- **mappers:** Converts between domain models and Mongo Documents.

## Workflow and Tasks

- Initialization and domain modeling
- Implementing the repository pattern
- Creating service layer logic
- Defining DTOs and mappers

- Exception handling and utility functions
- Documentation
