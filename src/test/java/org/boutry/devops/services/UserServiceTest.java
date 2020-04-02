package org.boutry.devops.services;

import io.quarkus.test.junit.QuarkusTest;
import org.boutry.devops.entities.UserEntity;
import org.boutry.devops.exception.ViolationException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.NotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class UserServiceTest {

    @Inject
    UserService userService;

    @AfterEach
    @Transactional
    void tearDown() {
        UserEntity.deleteAll();
    }


    @Test
    void getAllUsers() {
        List<UserEntity> userEntityList = List.of(new UserEntity("Patrick", "Balkany", "patrick.balkany@gmail.com"), new UserEntity("Jeanne", "Calmant", "jeanne.calmant@gmail.com"));
        List<UserEntity> createdEntityList = userEntityList.stream().map(userEntity -> {
            try {
                return userService.createUser(userEntity);
            } catch (ViolationException e) {
                fail(e);
            }
            return null; //shouldn't happen
        }).collect(Collectors.toUnmodifiableList());
        if (userEntityList.size() != createdEntityList.size()) {
            fail("Size are different");
        }
        List<UserEntity> gottenUserEntityList = new ArrayList<>(userService.getAllUsers());
        if (createdEntityList.size() != gottenUserEntityList.size()) {
            fail("Size are different");
        }
        for (int i = 0; i < userEntityList.size(); i++) {
            assertTrue(equalUser(userEntityList.get(i), createdEntityList.get(i)), String.format("%s should be identical to %s except for ids", userEntityList.get(i), createdEntityList.get(i)));
            assertTrue(gottenUserEntityList.containsAll(createdEntityList), "gottenUserEntityList should be a superset of createdEntityList");
        }
    }

    @Test
    void getUser() {
        UserEntity userEntity = new UserEntity("Patrick", "Balkany", "patrick.balkany@gmail.com");
        UserEntity userCreated;
        try {
            userCreated = userService.createUser(userEntity);
        } catch (ViolationException e) {
            fail(e);
            return;
        }
        UserEntity userGot = userService.getUser(userCreated.id);
        assertTrue(equalUser(userEntity, userGot), "User should be equal");
    }

    @Test
    void createUser() {
        UserEntity userEntity = new UserEntity("Patrick", "Balkany", "patrick.balkany@gmail.com");
        UserEntity userCreated;
        try {
            userCreated = userService.createUser(userEntity);
        } catch (ViolationException e) {
            fail(e);
            return;
        }

        assertTrue(equalUser(userEntity, userCreated), "User should be equal");
    }

    @Test
    void modifyUser() {
        UserEntity userEntity = new UserEntity("Patrick", "Balkany", "patrick.balkany@gmail.com");
        UserEntity userCreated;
        try {
            userCreated = userService.createUser(userEntity);
        } catch (ViolationException e) {
            fail(e);
            return;
        }
        userCreated.firstname = "Jean";
        userService.modifyUser(userCreated);
        userCreated = userService.getUser(userCreated.id);
        assertEquals("Jean", userCreated.firstname, "Firstname should be Jean");
    }

    @Test
    void deleteUser() {
        UserEntity userEntity = new UserEntity("Patrick", "Balkany", "patrick.balkany@gmail.com");
        final UserEntity userCreated;
        try {
            userCreated = userService.createUser(userEntity);
        } catch (ViolationException e) {
            fail(e);
            return;
        }
        userService.deleteUser(userCreated);
        assertThrows(NotFoundException.class, () -> {
            userService.getUser(userCreated.id);
        });
    }

    @Test
    @Disabled("Not implemented yet")
    void getUserCats() {
    }

    private boolean equalUser(UserEntity user1, UserEntity user2) {
        return user1.email.equalsIgnoreCase(user2.email) && user1.firstname.equals(user2.firstname) && user1.lastname.equals(user2.lastname);
    }
}
