package org.boutry.devops.resources;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.boutry.devops.entities.UserEntity;
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
        UserEntity[] users = given()
                .when().get("/user")
                .then()
                .statusCode(200)
                .extract()
                .as(UserEntity[].class);
        assertEquals(2, users.length);
    }

    @Test
    public void testGetUser() {
        User user = new User("Patrick", "Kalashi");
        UserEntity returned = given()
                .when()
                .contentType(ContentType.JSON)
                .body(user)
                .post("/user")
                .then()
                .statusCode(200)
                .extract()
                .as(UserEntity.class);
        UserEntity userEntity = given()
                .when()
                .pathParam("id", returned.id)
                .get("/user/{id}")
                .then()
                .statusCode(200)
                .extract()
                .as(UserEntity.class);
        assertEquals("Patrick", userEntity.firstname);

    }

    @Test
    public void testAddUser() {
        User user = new User("Jean", "Tardini");
        UserEntity returnedUser = given()
                .when()
                .contentType(ContentType.JSON)
                .body(user)
                .post("/user")
                .then()
                .statusCode(200)
                .extract()
                .as(UserEntity.class);
        assertEquals(user.getFirstname(), returnedUser.firstname);
        assertEquals(user.getLastname(), returnedUser.lastname);
    }

    @Test
    public void testDeleteUser() {
        User user = new User("Jean", "Kalashi");
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