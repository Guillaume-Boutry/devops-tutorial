package org.boutry.devops.resources;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.boutry.devops.entities.CatEntity;
import org.boutry.devops.entities.UserEntity;
import org.boutry.devops.exception.ViolationException;
import org.boutry.devops.services.CatService;
import org.boutry.devops.services.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.transaction.Transactional;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@QuarkusTest
public class CatResourceTest {

    @Inject
    UserService userService;

    @Inject
    CatService catService;

    @AfterEach
    @Transactional
    void tearDown() {
        CatEntity.deleteAll();
        UserEntity.deleteAll();
    }

    @Test
    public void testGetCatEntity() {
        UserEntity user = new UserEntity("Patrick", "Jamboni", "patrick.jamboni@gmail.com");
        try {
            user = userService.createUser(user);
        } catch (ViolationException e) {
            fail(e);
        }
        CatEntity cat = new CatEntity();
        cat.name = "Freyja";
        cat.owner = user;
        String location = given()
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

    @Test
    public void modifyCat() {
        UserEntity user = new UserEntity("Patrick", "Jamboni", "patrick.jamboni@gmail.com");
        CatEntity cat = new CatEntity();
        cat.name = "Freyja";
        try {
            user = userService.createUser(user);
            cat.owner = user;
            cat = catService.createCat(cat);
        } catch (ViolationException e) {
            fail(e);
        }

        cat.name = "Lili";
        System.out.println(cat);
        given()
                .when()
                .contentType(ContentType.JSON)
                .body(cat)
                .put("/api/cat")
                .then()
                .statusCode(204);

        CatEntity returnedCat = given()
                .when()
                .contentType(ContentType.JSON)
                .get(String.format("/api/cat/%d", cat.id))
                .then()
                .statusCode(200)
                .extract()
                .as(CatEntity.class);
        assertEquals(cat.name, returnedCat.name);
    }
}
