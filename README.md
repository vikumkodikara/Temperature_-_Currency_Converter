# 🔄 ConvertHub — Temperature & Currency Converter

<div align="center">

[![Live Demo](https://img.shields.io/badge/🌐_Live_Demo-converterhub.vikumkodikara.dev-00C853?style=for-the-badge&logoColor=white)](https://converterhub.vikumkodikara.dev/)

![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![Java](https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![MongoDB](https://img.shields.io/badge/MongoDB-4EA94B?style=for-the-badge&logo=mongodb&logoColor=white)
![HTML5](https://img.shields.io/badge/HTML5-E34F26?style=for-the-badge&logo=html5&logoColor=white)
![CSS3](https://img.shields.io/badge/CSS3-1572B6?style=for-the-badge&logo=css3&logoColor=white)
![JavaScript](https://img.shields.io/badge/JavaScript-F7DF1E?style=for-the-badge&logo=javascript&logoColor=black)

**A full-stack microservices application featuring Temperature and Currency conversion with a modern glassmorphism UI, built with Spring Boot and MongoDB.**

🔗 **[View Live Demo →](https://converterhub.vikumkodikara.dev/)**

[Features](#-features) • [Architecture](#-architecture) • [Getting Started](#-getting-started) • [API Reference](#-api-reference) • [Screenshots](#-screenshots)

</div>

---

## ✨ Features

- 🌡️ **Temperature Conversion** — Convert between Celsius, Fahrenheit, and Kelvin with smart unit normalization
- 💱 **Currency Conversion** — Convert USD to LKR with a configurable exchange rate
- 📊 **Conversion History** — All conversions are logged and persisted in MongoDB with timestamps
- 🎨 **Modern UI** — Premium glassmorphism design with animated gradient backgrounds and micro-interactions
- 🏗️ **Microservices Architecture** — Each converter runs as an independent Spring Boot service
- 🔄 **RESTful APIs** — Clean REST endpoints with proper error handling and CORS support

---

## 🏗 Architecture (Dockerized)

```
┌──────────────────────────────────────────────────────────┐
│                    Browser (Port 80)                     │
└──────────────────────────┬───────────────────────────────┘
                           │
┌──────────────────────────▼───────────────────────────────┐
│                 Nginx (Reverse Proxy)                    │
│            Serves static HTML/CSS/JS files               │
└──────────┬──────────────────────────────┬────────────────┘
           │                              │
    /api/temperatures              /api/currency
           │                              │
┌──────────▼──────────┐     ┌─────────────▼───────────────┐
│  Temperature Conv.  │     │    Currency Converter        │
│  (Spring Boot :8081)│     │    (Spring Boot :8082)      │
└──────────┬──────────┘     └─────────────┬───────────────┘
           │                              │
           └───────────────┬──────────────┘
                           │
                    Host 27018 -> temp_db
                    Host 27017 -> currency_db

                ┌──────────────────────────▼───────────────────────────────┐
                │                 temp-mongodb (mongo:7.0)                 │
                │                 currency-mongodb (mongo:7.0)             │
                └──────────────────────────────────────────────────────────┘
```

---

## 📁 Project Structure

```
Temperature_-_Currency_Converter/
│
├── docker-compose.yml                 # 🐳 Docker Compose orchestration
├── tempconv/                          # 🌡️ Temperature Converter Microservice
│   ├── Dockerfile                     # Multi-stage Maven build
│   ├── pom.xml                        # Maven dependencies (Spring Boot 4.0.5)
│   └── src/
│       └── ...
│
├── currencyconvertor/                 # 💱 Currency Converter Microservice
│   ├── Dockerfile                     # Multi-stage Maven build
│   ├── pom.xml                        # Maven dependencies (Spring Boot 4.0.6)
│   └── src/
│       └── ...
│
└── frontend/                          # 🎨 Web Frontend
    ├── Dockerfile                     # Nginx static file server
    ├── nginx.conf                     # Reverse proxy configuration
    ├── index.html                     # Main HTML structure
    ├── style.css                      # Glassmorphism CSS styling
    └── app.js                         # API integration & DOM logic
```

---

## 🚀 Getting Started

### Prerequisites

| Requirement | Version |
|-------------|---------|
| **Docker** | Latest |
| **Docker Compose** | V2 |

### 🐳 Run with Docker (Recommended)

The easiest way to run the application is using Docker Compose. It will automatically build the backend JARs, set up the Nginx proxy, and start the MongoDB database.

```bash
# Build and start all containers in detached mode
docker compose up -d --build

# View logs for all containers
docker compose logs -f

# Stop and remove containers
docker compose down

# Stop and remove containers AND delete the database volume
docker compose down -v
```

Once running, simply open your browser to **http://localhost**

> **Note:** The first build may take 5-10 minutes as it downloads the Maven dependencies and Docker images. Subsequent builds will be much faster.

### 🛠️ Run Manually (For Development)

If you prefer to run the services without Docker, you will need Java 21 and MongoDB installed locally.

**1. Start MongoDB**
Start two local MongoDB instances if you run the apps outside Docker: Temperature on port `27018` and Currency on port `27017`.

**2. Start Temperature Converter**
```bash
cd tempconv
./mvnw spring-boot:run
```
*(Starts on http://localhost:8081)*

**3. Start Currency Converter**
```bash
cd currencyconvertor
./mvnw spring-boot:run
```
*(Starts on http://localhost:8082)*

**4. Open the Frontend**
Open `frontend/index.html` directly in your browser. (Note: When running manually without Nginx, you will need to update the API URLs in `app.js` to point to `localhost:8081` and `localhost:8082`).

---

## 📡 API Reference

### 🌡️ Temperature Converter — `http://localhost:8081`

#### Convert Temperature

```http
POST /api/temperatures/convert?value={value}&unit={unit}
```

| Parameter | Type     | Description                                      |
|-----------|----------|--------------------------------------------------|
| `value`   | `double` | **Required.** Temperature value to convert        |
| `unit`    | `string` | **Required.** Input unit (`celsius`, `fahrenheit`, `kelvin`, `c`, `f`, `k`) |

**Example Request:**

```bash
curl -X POST "http://localhost:8081/api/temperatures/convert?value=100&unit=celsius"
```

**Example Response:**

```json
{
  "id": "664a1b2c3d4e5f6a7b8c9d0e",
  "inputTemperature": 100.0,
  "inputUnit": "Celsius",
  "outputTemperature": 212.0,
  "outputUnit": "Fahrenheit",
  "timestamp": "2026-05-08T00:15:30.123Z"
}
```

#### Get Conversion History

```http
GET /api/temperatures/history
```

**Example:**

```bash
curl http://localhost:8081/api/temperatures/history
```

---

### 💱 Currency Converter — `http://localhost:8082`

#### Convert USD to LKR

```http
POST /api/currency/convert?usdAmount={amount}
```

| Parameter   | Type     | Description                           |
|-------------|----------|---------------------------------------|
| `usdAmount` | `double` | **Required.** USD amount to convert   |

**Header Requirements:**
- `X-API-KEY`: **Required.** (e.g., `SUPER-SECRET-DEV-KEY-123`)

**Example Request:**

```bash
curl -X POST "http://localhost:8082/api/currency/convert?usdAmount=50" \
  -H "X-API-KEY: SUPER-SECRET-DEV-KEY-123"
```

**Example Response:**

```json
{
  "id": "664a1b2c3d4e5f6a7b8c9d0f",
  "inputAmount": 50.0,
  "inputCurrency": "USD",
  "outputAmount": 15000.0,
  "outputCurrency": "LKR",
  "exchangeRate": 300.0,
  "timestamp": "2026-05-08T00:20:45.456"
}
```

#### Rate Check

```http
GET /api/currency/rate-check?usdAmount={amount}
```

**Example Request:**

```bash
curl "http://localhost:8082/api/currency/rate-check?usdAmount=50"
```

**Example Response (Text):**
```text
The conversion of $50.0 USD to 15000.0 LKR is a standard transaction.
```

#### Get Conversion History

```http
GET /api/currency/history
```

**Example:**

```bash
curl http://localhost:8082/api/currency/history
```

#### Filter History

```http
GET /api/currency/history/filter?currency={currency}
```

**Example:**

```bash
curl "http://localhost:8082/api/currency/history/filter?currency=USD"
```

---

## 🖼 Screenshots

### 🎨 Frontend UI

The unified web interface features:

- **Tab-based navigation** between Currency and Temperature converters
- **Animated gradient background** with floating orbs
- **Glassmorphism cards** with frosted glass effect
- **Real-time conversion results** with smooth animations
- **Conversion history tables** with zebra striping and hover effects
- **Toast notifications** for success/error feedback
- **Fully responsive** design for mobile and desktop

---

## 🛠 Tech Stack

| Layer        | Technology                  | Purpose                              |
|--------------|-----------------------------|--------------------------------------|
| **Backend**  | Spring Boot 4.0 (Java 21)  | REST API microservices               |
| **Database** | MongoDB                     | NoSQL document storage               |
| **ORM**      | Spring Data MongoDB         | Repository pattern data access       |
| **Utility**  | Lombok                      | Boilerplate code reduction           |
| **Frontend** | HTML5 / CSS3 / JavaScript   | Responsive web interface             |
| **Fonts**    | Google Fonts (Inter)        | Modern typography                    |
| **Build**    | Maven                       | Dependency management & build tool   |

---

## 🧪 Running Tests

```bash
# Temperature Converter tests
cd tempconv
./mvnw test

# Currency Converter tests
cd currencyconvertor
./mvnw test
```

---

## ⚙️ Configuration

### Temperature Converter (`tempconv/src/main/resources/application.yaml`)

```yaml
spring:
  application:
    name: tempconv
  data:
    mongodb:
      host: ${MONGODB_HOST:localhost}
      port: ${MONGODB_PORT:27018}
      database: temp_db

server:
  port: 8081
```

### Currency Converter (`currencyconvertor/src/main/resources/application.yaml`)

```yaml
spring:
  application:
    name: currencyconvertor
  data:
    mongodb:
      host: ${MONGODB_HOST:localhost}
      port: ${MONGODB_PORT:27017}
      database: currency_db

server:
  port: 8082
```

---

## 🤝 Contributing

Contributions are welcome! Here's how you can help:

1. **Fork** the repository
2. **Create** a feature branch (`git checkout -b feat/amazing-feature`)
3. **Commit** your changes (`git commit -m 'feat: add amazing feature'`)
4. **Push** to the branch (`git push origin feat/amazing-feature`)
5. **Open** a Pull Request

---

## 📄 License

This project is open source and available under the [MIT License](LICENSE).

---

<div align="center">

**Built with ❤️ using Spring Boot & MongoDB**

</div>
