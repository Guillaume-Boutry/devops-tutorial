package org.boutry.devops.resources;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.boutry.devops.entities.UserEntity;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
class UserResourceTest {

    @Test
    public void testGetUserEntity() {
        UserEntity user = new UserEntity("Patrick", "Kalashi", "patrick.kalashi@gmail.com");
        String location = given()
                .when()
                .contentType(ContentType.JSON)
                .body(user)
                .post("/api/user")
                .then()
                .statusCode(201)
                .extract()
                .header("Location");
        UserEntity userEntity = given()
                .when()
                .get(location)
                .then()
                .statusCode(200)
                .extract()
                .as(UserEntity.class);
        assertEquals("Patrick", userEntity.firstname);

    }

    @Test
    public void testAddUserEntity() {
        UserEntity user = new UserEntity("Jean", "Tardini", "jean.tardini@gmail.com");
        given()
                .when()
                .contentType(ContentType.JSON)
                .body(user)
                .post("/api/user")
                .then()
                .statusCode(201);
    }

    @Test
    public void testDeleteUserEntity() {
        UserEntity user = new UserEntity("Jean", "Particiani", "jean.particiani@gmail.com");
        String location = given()
                .when()
                .contentType(ContentType.JSON)
                .body(user)
                .post("/api/user")
                .then()
                .statusCode(201)
                .extract()
                .header("Location");
        user = given()
                .when()
                .get(location)
                .then()
                .extract()
                .as(UserEntity.class);
        given()
                .when()
                .contentType(ContentType.JSON)
                .body(user)
                .delete("/api/user")
                .then()
                .statusCode(204);
    }

    @Test
    public void testModifyUserEntity() {
        UserEntity user = new UserEntity("Jeanne", "Calmant", "je.ca@gmail.com");
        String location = given()
                .when()
                .contentType(ContentType.JSON)
                .body(user)
                .post("/api/user")
                .then()
                .statusCode(201)
                .extract()
                .header("Location");
        user = given()
                .when()
                .contentType(ContentType.JSON)
                .get(location)
                .then()
                .statusCode(200)
                .extract()
                .as(UserEntity.class);
        user.email = "jaja@gmail.com";
        given()
                .when()
                .contentType(ContentType.JSON)
                .body(user)
                .put("/api/user")
                .then()
                .statusCode(204);
        UserEntity returned = given()
                .when()
                .contentType(ContentType.JSON)
                .get(location)
                .then()
                .statusCode(200)
                .extract()
                .as(UserEntity.class);
        assertEquals(user.email, returned.email);
    }

    @Test
    public void testDuplicateUserEntity() {
        UserEntity user = new UserEntity("Jean", "Kalashi", "jean.kalash@gmail.com");
        given()
                .when()
                .contentType(ContentType.JSON)
                .body(user)
                .post("/api/user")
                .then()
                .statusCode(201);
        given()
                .when()
                .contentType(ContentType.JSON)
                .body(user)
                .post("/api/user")
                .then()
                .statusCode(409);
    }

    @Test
    public void testBlankField() {
        UserEntity user = new UserEntity("Jean", "", "jean.kalash@gmail.com");
        given()
                .when()
                .contentType(ContentType.JSON)
                .body(user)
                .post("/api/user")
                .then()
                .statusCode(422);
    }

    @Test
    public void testNullField() {
        UserEntity user = new UserEntity(null, "Balkany", "jean.kalash@gmail.com");
        given()
                .when()
                .contentType(ContentType.JSON)
                .body(user)
                .post("/api/user")
                .then()
                .statusCode(422);
    }
}