# Purchase Transaction API

# Table of Contents
- Overview
- Features
- Getting Started
- Usage
- Endpoints
- Technologies Used

The Purchase Transaction API is a simple Spring Boot application that allows you to accept and store(in cache, because
the application it's not linked to any database source) purchase transactions with a description, transaction date, and
a purchase amount in United States dollars (USD). 
Each transaction is assigned a unique identifier upon storage. Additionally, the application provides a way to retrieve
stored purchase transactions converted to currencies supported by the Treasury Reporting Rates of Exchange API
based on the exchange rate active for the date of the purchase.

# Features
- Accept and store purchase transactions with the following details:
- Description
- Transaction date
- Purchase amount in USD
- Assign a unique identifier to each stored transaction
- Retrieve stored purchase transactions converted to different currencies
- Provide details for each retrieved purchase transaction:
   - Identifier
   - Description
   - Transaction date
   - Original USD purchase amount
   - Exchange rate used
   - Converted amount in the specified currency or country

# Getting Started
Follow these steps to get the Purchase Transaction API up and running on your local environment:

1. Clone the repository:

````
git clone https://github.com/izamatos/transaction-purchase-api.git
````
2. Navigate to the project directory:

````
cd purchase-transaction-api
````
3. Build the application using Maven:
````
mvn clean install
````
4. Run the application:

````
mvn spring-boot:run
````
The API should now be accessible at http://localhost:8080.

# Usage
You can interact with the Purchase Transaction API using HTTP requests. Below are the available endpoints and how to use them.
The API endpoints are listed and documented on the Swagger: http://localhost:8080/swagger-ui/index.html#/

# Endpoints
# 1.Create a Purchase Transaction
**endpoint: POST /transactions**
**Request Body:**

```json
{
"description": "Sample purchase",
"transactionDate": "2023-09-22",
"usdPurchaseAmount": 100.00
}
````
**Response (Success):**

```json
{
"transactionUuid": "unique-transaction-uuid",
"description": "description",
"transactionDate": "2023-09-22",
"usdPurchaseAmount": 100.00
}
````
# 2. Retrieve a Purchase Transaction
**Endpoint: GET /transactions/{transactionUuid}**

**Response (Success):**

```json
{
"transactionUuid": "unique-transaction-uuid",
"description": "description",
"transactionDate": "2023-09-22",
"usdPurchaseAmount": 100.00,
"exchangeRateUsed": 1.23,
"convertedAmount": 123.00,
"countryCurrencyDescription": "Brazil-Real",
"exchangeRateRecordDate": "2023-09-22"
}
````

# Technologies Used
Spring Boot
Spring Cache (Simple Cache)
Maven
Java 17
RESTful API