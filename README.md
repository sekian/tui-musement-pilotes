# TUI DX Backend Technical Test v2

## Description
Spring based application that allows to manage the orders of the "pilotes" through a REST API.

## Operations
The following operations are implemented:
- Creation of a client user.
- Creation of a pilotes order.
- Update of a pilotes order.
- Login with user credentials.
- Search orders by customer data.

# Usage of the API
### Documentation
By default, the Swagger API documentation is available at `http://localhost:8080/swagger-ui/index.html`

By default, the H2 console is available at `http://localhost:8080/h2-console/`

### Considerations
When you create a user you will get a client identifier.

To create an order you will need to provide your client identifier.

When you create an order you will get an order identifier.

To update an order you will need to provide the order identifier.

The search operation is secured through JWT Bearer Token.

Make sure to  create a user and then login to get your token.

When using the API through Swagger, provide the token through the "Authorize" button.

When sending a request directly to the server provide the token in the header `Authorization: Bearer {token}`

### Example command with cURL
cURL POST request to create and order for client with identifier 1.

`curl "localhost:8080/api/order" -H 'Content-Type: application/json' -X POST -d '{"client":{"clientId":1}, "deliveryAddress":{"street":"Gran Via","postcode":"08034","city":"Barcelona","country":"Spain"}, "pilotes":15}'`

## Project information
The project has been tested with JDK 8 and JDK 11. 

The project is a Spring Boot application and uses Apache Maven.

The project provides Swagger API documentation.

The project has been developed with the IDE "IntelliJ IDEA Community".

The project uses H2 in-memory database.

The project uses Hibernate dialect to specificy the database relationships.

The project has tests at all levels with a coverage above 80%.

The project has a Dockerfile.

The proejct uses Lombok.

# Possible improvements
Encrypt the password and hash it with salt so that its not stored in the server in plain text.

Enforce the email to be a unique identifier and verify that the user owns the email (through a verification email).

Complete the API services to include all the missing REST operations.

Implement pagination for the search endpoint to avoid unnecessary strain on the server.

Implement a better approach to programmatically select which fields are visible on the responses.

Make the JWT Bearer token valid only for certain user roles.

Add Javadocs for the relevant functions.

If the creation of the order was authentificated (or through generated tokens) then the client would not need to specify his details for each order creation. 

It would also remove at server level the possibility to create an order on behalf of another client.

## Commands
### Docker local development: 
```
cd backend-technical-test-v2-master
mvn clean package
docker build --tag=pilotes-server:latest .
docker run -p8887:8888 pilotes-server:latest
```
### Maven
```
mvn clean test
mvn spring-boot:run
```
