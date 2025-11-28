# Serverless Global URL Shortener

![Spring Boot 3.2](https://img.shields.io/badge/Spring_Boot-3.2-6DB33F?logo=springboot&logoColor=white)
![AWS Serverless](https://img.shields.io/badge/AWS-Serverless-232F3E?logo=amazonaws&logoColor=white)
![DynamoDB](https://img.shields.io/badge/Database-DynamoDB-4053D6?logo=amazondynamodb&logoColor=white)
![Java 17](https://img.shields.io/badge/Java-17-ED8B00?logo=openjdk&logoColor=white)

A **high-performance, fully serverless** URL shortening service built with **Spring Boot 3**, **AWS Lambda**, and **Amazon DynamoDB**.  
Designed for **sub-500ms cold starts**, **zero-cost when idle**, and **perfect analytics accuracy** under extreme concurrency (e.g., viral "celebrity tweet" scenarios).

Live Demo: [Link](https://2nzlphq3qb.execute-api.ap-south-2.amazonaws.com/dev/health)


Try shortening: `POST` to `/api/v1/shorten`

---

## Overview

This is a cloud-native, event-driven URL shortener that scales instantly from 0 to thousands of RPS with **zero servers to manage**.

Key differentiators from typical tutorials:
- Solves Java cold starts using **AWS Lambda SnapStart** (CRaC)
- Race-condition-free click analytics using **DynamoDB atomic counters**
- Collision-safe custom aliases via **optimistic locking**
- Production-ready architecture using the **Adapter Pattern**

---

Base Url(API Gateway): https://2nzlphq3qb.execute-api.ap-south-2.amazonaws.com/dev


### 1. Shorten a URL
```
POST {{baseUrl}}/api/v1/shorten
Content-Type: application/json

{
  "originalUrl": "https://google.com",
}
```

### 2. Get Url(Redirect Url)
```
GET {{baseUrl}}/{shortCode}
```

### 3. Stats Endpoint
```
GET {{baseUrl}}/{shortCode}/stats
```

## Architecture

```mermaid
graph LR
    User((User)) -->|HTTPS Request| APIG[API Gateway<br/>REST API]
    APIG -->|Invoke| Lambda[AWS Lambda<br/>Java 17 + SnapStart]
    Lambda -->|Read/Write| DDB[DynamoDB<br/>On-Demand]
    Lambda -->|302 Redirect| User
    Lambda -->|Atomic Update| DDB
```

