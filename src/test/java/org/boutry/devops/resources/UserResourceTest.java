package org.boutry.devops.resources;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.boutry.devops.models.User;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
class UserResourceTest {
    @Test
    @Disabled("Only example")
    public void testUsersEndpoint() {
        User[] users = given()
                .when().get("/user")
                .then()
                .statusCode(200)
                .extract()
                .as(User[].class);
        assertEquals(2, users.length);
    }

    @Test
    public void testPatrick() {
        User user = given()
                .when()
                .pathParam("id", 1)
                .get("/user/{id}")
                .then()
                .statusCode(200)
                .extract()
                .as(User.class);
        assertEquals("Patrick", user.firstname);

    }

    @Test
    public void testAddUser() {
        User user = new User(57, "Jean", "Tardini");
        User returnedUser = given()
                .when()
                .contentType(ContentType.JSON)
                .body(user)
                .post("/user")
                .then()
                .statusCode(200)
                .extract()
                .as(User.class);
        assertEquals(user, returnedUser);
    }

    @Test
    public void testDeleteUser() {
        User user = new User(58, "Jean", "Kalashi");
        given()
                .when()
                .contentType(ContentType.JSON)
                .body(user)
                .post("/user")
                .then()
                .statusCode(200);
        given()
                .when()
                .contentType(ContentType.JSON)
                .body(user)
                .delete("/user")
                .then()
                .statusCode(204);
    }
}