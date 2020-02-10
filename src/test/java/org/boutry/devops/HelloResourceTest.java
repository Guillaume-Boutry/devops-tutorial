package org.boutry.devops;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class HelloResourceTest {

    @Test
    public void testHelloEndpoint() {
        given()
          .when().get("/hello")
          .then()
             .statusCode(200)
             .body(is("hello"));
    }

    @Test
    public void testHelloName() {
        given()
                .when().get("/hello/Dumbo")
                .then()
                .statusCode(200)
                .body(is("<h1>hello Dumbo</h1>"));
    }
}