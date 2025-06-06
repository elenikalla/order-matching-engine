# Order Matching Engine

A Java + React-based matching engine that processes buy/sell orders in real-time using FIFO matching logic and Kafka for asynchronous order handling.

---

## Features

- Place BUY / SELL orders via UI
- Match orders based on price-time priority (FIFO)
- Record matched trades in the backend
- View all orders and trade history from a web interface
- Kafka integration for decoupled asynchronous order handling

---

## Tech Stack

**Backend**
- Java 21
- Spring Boot
- Apache Kafka (local)
- H2 (in-memory database)
- Maven

**Frontend**
- React + Vite
- Tailwind CSS

---
## Getting Started

### Backend (Spring Boot)

1.Clone the repository
   git clone https://github.com/elenikalla/order-matching-engine.git
   cd order-matching-engine

2. Start Kafka locally
   Make sure kafka is running on : localhost:9092

3. Run the Spring Boot app 
    The backend will be available at: http://localhost:8080

### Frontend (React)

1. cd order-ui
2.npm install
3.npm run dev 

The frontend will be available at: http://localhost:5173
