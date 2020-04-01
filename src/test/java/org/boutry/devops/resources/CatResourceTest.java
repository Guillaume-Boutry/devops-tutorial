package org.boutry.devops.resources;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.boutry.devops.entities.CatEntity;
import org.boutry.devops.entities.UserEntity;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
public class CatResourceTest {


    @Test
    public void testGetCatEntity() {
        UserEntity user = new UserEntity("Patrick", "Jamboni", "patrick.jamboni@gmail.com");
        String location = given()
                .when()
                .contentType(ContentType.JSON)
                .body(user)
                .post("/api/user")
                .then()
                .statusCode(201)
                .extract()
                .header("Location");
        UserEntity owner = given()
                .when()
                .get(location)
                .then()
                .statusCode(200)
                .extract()
                .as(UserEntity.class);
        CatEntity cat = new CatEntity();
        cat.name = "Freyja";
        cat.owner = owner;
        location = given()
                .when()
                .contentType(ContentType.JSON)
                .body(cat)
                .post("/api/cat")
                .then()
                .statusCode(201)
                .extract()
                .header("Location");
        CatEntity returnedCat = given()
                .when()
                .get(location)
                .then()
                .statusCode(200)
                .extract()
                .as(CatEntity.class);
        assertEquals(cat.name, returnedCat.name);
        assertEquals(cat.owner.email, returnedCat.owner.email);
    }
}
