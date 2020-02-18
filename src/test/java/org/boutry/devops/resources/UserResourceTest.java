package org.boutry.devops.resources;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.boutry.devops.entities.UserEntity;
import org.boutry.devops.models.User;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

//@Testcontainers
@QuarkusTest
class UserResourceTest {

  /*  @Container
    static MariaDBContainer db = new MariaDBContainer();*/

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
        User user = new User("Patrick", "Kalashi", "patrick.kalashi@gmail.com");
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
        User user = new User("Jean", "Tardini", "jean.tardini@gmail.com");
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
        User user = new User("Jean", "Particiani", "jean.particiani@gmail.com");
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

    @Test
    public void testDuplicateUser() {
        User user = new User("Jean", "Kalashi", "jean.kalash@gmail.com");
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
                .post("/user")
                .then()
                .statusCode(409);
    }


    @Test
    public void testBlankField() {
        User user = new User("Jean", "", "jean.kalash@gmail.com");
        given()
                .when()
                .contentType(ContentType.JSON)
                .body(user)
                .post("/user")
                .then()
                .statusCode(422);
    }

    @Test
    public void testNullField() {
        User user = new User(null, "Balkany", "jean.kalash@gmail.com");
        given()
                .when()
                .contentType(ContentType.JSON)
                .body(user)
                .post("/user")
                .then()
                .statusCode(422);
    }
}