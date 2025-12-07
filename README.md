# ⛷️ Guidex - AI Skiing Coach Backend

> **Enterprise-grade distributed backend system for real-time skiing motion analysis.**
> *Reduced video processing latency from 2min+ to 3s via Asynchronous Architecture.*

[![Java](https://img.shields.io/badge/Java-8-orange)](https://www.java.com/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.5-green)](https://spring.io/projects/spring-boot)
[![AWS](https://img.shields.io/badge/Cloud-AWS-232F3E)](https://aws.amazon.com/)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

---

## 🎥 Project Demo (TestFlight Release)
![final1](https://github.com/user-attachments/assets/947162ae-5c2e-44e6-a0ed-ff9ac5ef6c32)


**Guidex** is an iOS application that helps skiers improve their form using Computer Vision. This repository hosts the **backend infrastructure** that powers the user system, video processing pipeline, and data analysis engine.

---

## 🏗️ System Architecture

### High-Level Design
* **API Gateway:** Nginx for load balancing and SSL termination.
* **Core Service:** **Java Spring Boot** application handling business logic and JWT authentication.
* **Async Engine:** Custom `ThreadPoolTaskExecutor` for non-blocking video upload & analysis.
* **AI Integration:** RESTful communication with Python/OpenPose microservices.
* **Data Layer:** MySQL (User Data, Metadata) + Redis (Hot Cache, Token Storage).

---

## ⚡ Key Technical Highlights

### 1. Asynchronous Processing Pipeline
> **Challenge:** OpenPose analysis is CPU-intensive and blocks the main thread, causing timeouts.
> **Solution:** Implemented a Producer-Consumer model.
> - Client uploads video -> Server returns `task_id` immediately (Non-blocking).
> - Backend submits task to a managed **Thread Pool**.
> - Frontend polls for status, reducing user perceived latency by **97%**.

### 2. Robust Security & Auth
- **JWT & OAuth2:** Stateless authentication integrated with Apple/Google Sign-In.
- **RBAC:** Role-Based Access Control designed for scalable user tiers.

### 3. Cloud Infrastructure & Storage
- **Compute:** Deployed directly on **AWS EC2** (Linux/Ubuntu) for high performance.
- **Multi-Cloud Storage:** Implemented a robust storage strategy using both **AWS S3** and **Google Cloud Storage (GCS)** to manage raw video assets and processed results securely.

---

## 🛠️ Tech Stack

- **Core:** Java 8, Spring Boot 2.5
- **Database:** MySQL 8.0, Redis 6.0
- **ORM:** MyBatis Plus
- **Build Tool:** Maven
- **DevOps:** Docker, Shell Scripting, Git

---

### 👤 Author
**Simon Zhiyuan Sun**
*Backend Engineer | Toronto, ON*
[LinkedIn](https://www.linkedin.com/in/simon-zhiyuan-sun) | [Email](mailto:simon.zhiyuan.sun@outlook.com)
