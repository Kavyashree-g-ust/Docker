package contract.pos;

import au.com.dius.pact.consumer.MockServer;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.V4Pact;
import au.com.dius.pact.core.model.annotations.Pact;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Map;

import static io.restassured.RestAssured.given;

@ExtendWith(PactConsumerTestExt.class)
@PactTestFor(providerName = "user-provider")
public class UserConsumerPactTest {

    @Pact(provider = "user-provider", consumer = "user-consumer")
    V4Pact userPact(PactDslWithProvider builder) {

        return builder
                .given("User 1 exists")
                .uponReceiving("Get user by id")
                .path("/users/1")
                .method("GET")
                .willRespondWith()
                .status(200)
                .headers(Map.of("Content-Type", "application/json"))
                .body(new PactDslJsonBody()
                        .integerType("id", 1)
                        .stringType("name", "Kavya")
                        .stringType("role", "Developer"))
                .toPact(V4Pact.class);
    }

    @Test
    @PactTestFor(pactMethod = "userPact")
    void verifyConsumer(MockServer mockServer) {

        given()
                .baseUri(mockServer.getUrl())
                .when()
                .get("/users/1")
                .then()
                .statusCode(200);
    }

    @Pact(provider = "user-provider", consumer = "user-consumer")
    V4Pact user2Pact(PactDslWithProvider builder) {
        return builder
                .given("User 2 exists")
                .uponReceiving("Get user 2 by id")
                .path("/users/2")
                .method("GET")
                .willRespondWith()
                .status(200)
                .body(new PactDslJsonBody()
                        .integerType("id", 2)
                        .stringType("name", "Rahul")
                        .stringType("role", "Tester"))
                .toPact(V4Pact.class);
    }

    @Test
    @PactTestFor(pactMethod = "user2Pact")
    void verifyConsumerUser2(MockServer mockServer) {

        given()
                .baseUri(mockServer.getUrl())
                .when()
                .get("/users/2")
                .then()
                .statusCode(200);
    }

    @Pact(provider = "user-provider", consumer = "user-consumer")
    V4Pact createUserPact(PactDslWithProvider builder) {

        return builder
                .given("User can be created")
                .uponReceiving("Create a new user")
                .path("/users")
                .method("POST")
                .headers(Map.of("Content-Type", "application/json"))
                .body(new PactDslJsonBody()
                        .integerType("id", 3)
                        .stringType("name", "Ravi")
                        .stringType("role", "Developer"))
                .willRespondWith()
                .status(201)
                .headers(Map.of("Content-Type", "application/json"))
                .body(new PactDslJsonBody()
                        .integerType("id", 3)
                        .stringType("name", "Ravi")
                        .stringType("role", "Developer"))
                .toPact(V4Pact.class);
    }

    @Test
    @PactTestFor(pactMethod = "createUserPact")
    void verifyCreateUser(MockServer mockServer) {

        given()
                .baseUri(mockServer.getUrl())
                .contentType("application/json")
                .body("""
                        {
                          "id":3,
                          "name":"Ravi",
                          "role":"Developer"
                        }
                        """)
                .when()
                .post("/users")
                .then()
                .statusCode(201);
    }

    @Pact(provider = "user-provider", consumer = "user-consumer")
    V4Pact userNotFoundPact(PactDslWithProvider builder) {

        return builder
                .given("User 99 does not exist")
                .uponReceiving("Get invalid user")
                .path("/users/99")
                .method("GET")
                .willRespondWith()
                .status(404)
                .toPact(V4Pact.class);
    }
    @Test
    @PactTestFor(pactMethod = "userNotFoundPact")
    void verifyUserNotFound(MockServer mockServer) {

        given()
                .baseUri(mockServer.getUrl())
                .when()
                .get("/users/99")
                .then()
                .statusCode(404);
    }
}