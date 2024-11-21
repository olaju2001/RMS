# Resource Management System - User Guide

## Table of Contents
1. [Prerequisites](#1-prerequisites)
2. [Initial Setup](#2-initial-setup)
3. [Starting the System](#3-starting-the-system)
4. [Verifying System Components](#4-verifying-system-components)
5. [Testing Scenarios](#5-testing-scenarios)
6. [Troubleshooting](#6-troubleshooting)
7. [System Shutdown](#7-system-shutdown)

## 1. Prerequisites

Before starting, ensure you have the following installed:
- Java Development Kit (JDK) 17
- Docker Desktop
- Git
- IntelliJ IDEA (Community Edition is sufficient)
- PostgreSQL with pgAdmin (for local development)

Required ports should be available:
- 8080: Spring Boot application
- 5432: PostgreSQL
- 9092: Kafka
- 2181: Zookeeper
- 9090: Prometheus
- 3000: Grafana

## 2. Initial Setup

### 2.1 Clone and Configure the Project

```bash
# Clone the repository
git clone [your-repository-url]
cd resource-management-system

# Create necessary directories
mkdir -p logs
```

### 2.2 Configure Environment

1. Update `application.yml`:
```yaml
spring:
  datasource:
    password: your_password  # Match with docker-compose.yml

external:
  weather:
    api:
      key: your-api-key  # Update if using weather API
```

2. Verify `docker-compose.yml` configurations match your environment.

## 3. Starting the System

### 3.1 Start Infrastructure Services

```bash
# Start all containers
docker-compose up -d
```

Wait approximately 30 seconds for all services to initialize.

### 3.2 Start Spring Boot Application

Using IntelliJ IDEA:
1. Open the project
2. Wait for Maven to download dependencies
3. Run `ResourceManagementSystemApplication.java`

Alternatively, use Maven:
```bash
./mvnw spring-boot:run
```

## 4. Verifying System Components

### 4.1 PostgreSQL
```bash
# Connect to PostgreSQL
psql -h localhost -p 5432 -U postgres -d resource_management_db
# Enter password: your_password

# Verify tables
\dt
```

Expected output should show tables: `resources`, `monitoring_metrics`, `utilization_records`, etc.

### 4.2 Kafka
```bash
# List Kafka topics
docker exec kafka kafka-topics.sh --list --bootstrap-server localhost:9092
```

Expected topics:
- resource-metrics
- processed-metrics
- weather-updates

### 4.3 Application Status
Access these URLs in your browser:

- Swagger UI: http://localhost:8080/swagger-ui.html
- Health Check: http://localhost:8080/actuator/health
- Prometheus Metrics: http://localhost:8080/actuator/prometheus

### 4.4 Monitoring Tools
- Grafana: http://localhost:3000 (admin/123456)
- Prometheus: http://localhost:9090

## 5. Testing Scenarios

### 5.1 Creating Resources (via Swagger UI)

1. Navigate to http://localhost:8080/swagger-ui.html
2. Expand "Resource Management" section
3. Click POST /api/v1/resources
4. Use this sample payload:
```json
{
  "type": "SOLAR_PANEL",
  "capacity": 100.00,
  "status": "ACTIVE",
  "location": {
    "latitude": "51.5074",
    "longitude": "-0.1278",
    "address": "London, UK"
  }
}
```

### 5.2 Verify Data Persistence

In pgAdmin or psql:
```sql
SELECT * FROM resources ORDER BY created_at DESC LIMIT 1;
```

### 5.3 Test Monitoring Metrics

1. Use Swagger UI to create a monitoring metric:
```json
{
  "timestamp": "2024-03-21T10:00:00",
  "resourceId": 1,
  "energyOutput": 75.5,
  "efficiency": 0.85,
  "weatherConditions": "SUNNY"
}
```

2. Verify Kafka message processing:
```bash
# Monitor Kafka topics
docker exec kafka kafka-console-consumer.sh \
  --bootstrap-server localhost:9092 \
  --topic resource-metrics \
  --from-beginning
```

### 5.4 Monitoring in Grafana

1. Login to Grafana (http://localhost:3000)
2. Navigate to Dashboards
3. Import the default dashboard (if available) or create a new one
4. Add panels for:
   - Resource efficiency
   - Energy output
   - System metrics

## 6. Troubleshooting

### 6.1 Database Connection Issues
```bash
# Check PostgreSQL container
docker logs resource_management_db

# Verify connection settings
docker exec resource_management_db pg_isready -h localhost
```

### 6.2 Kafka Issues
```bash
# Check Kafka logs
docker logs kafka

# Verify Kafka connection
docker exec kafka kafka-topics.sh --describe --bootstrap-server localhost:9092
```

### 6.3 Application Issues

Check application logs:
```bash
tail -f logs/application.log
```

Common issues and solutions:
- Port conflicts: Check and free required ports
- Database migration fails: Check `logs/application.log` for Flyway errors
- Kafka connection fails: Ensure Kafka and Zookeeper are running

## 7. System Shutdown

### 7.1 Graceful Shutdown Order

1. Stop Spring Boot application
   - Press Ctrl+C if running in terminal
   - Stop the application in IntelliJ

2. Stop Docker containers:
```bash
# Stop all containers
docker-compose down

# To also remove volumes (caution: removes data)
docker-compose down -v
```

### 7.2 Cleanup (if needed)
```bash
# Remove unused Docker volumes
docker volume prune

# Clear logs
rm -rf logs/*
```

## Additional Notes

- Always check the application logs (`logs/application.log`) for detailed error messages
- Keep Docker resources monitored using `docker stats`
- Regularly backup PostgreSQL data if running in production
- Monitor disk space used by logs and Docker volumes

For additional support or specific issues, refer to the project's documentation or create an issue in the repository.
