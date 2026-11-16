# PII Redaction Gateway

A Java service that sits between users and an AI model: it detects personal data like card numbers, SINs, emails, and names in incoming text, replaces it with placeholder tokens before the text reaches the model, and puts the real values back into the response.

## Tech stack
- Java 21
- Maven

## Build
```bash
mvn clean compile
```