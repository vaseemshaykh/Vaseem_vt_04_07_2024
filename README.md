# Vaseem_vt_04_07_2024
URL Shortener Project

# URL Shortener Service

## Overview
This project implements a URL Shortener Service using Spring Boot and PostgreSQL, allowing users to create shortened URLs, retrieve the original URL, update destination URLs, and extend expiration dates.

## Features
- **Shorten URL**: Generates a shortened URL for a given destination URL.
- **Fetch Original URL**: Retrieves the original URL using the shortened URL.
- **Update Destination URL**: Updates the destination URL associated with a shortened URL.
- **Extend Expiration**: Extends the expiration date of a shortened URL.

## Technologies Used
- **Java 11**
- **Spring Boot 2.5**
- **PostgreSQL**
- **Spring Data JPA**
- **Spring Cache**
- **SLF4J**
- **Apache Commons Lang3**

## Prerequisites
- Java 11 or higher
- Maven 3.6.3 or higher
- PostgreSQL 12 or higher

## Getting Started

### Clone the Repository
```bash
git clone https://github.com/yourusername/url-shortener-service.git
cd url-shortener-service
