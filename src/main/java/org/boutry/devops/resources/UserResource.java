package org.boutry.devops.resources;

import org.boutry.devops.entities.UserEntity;
import org.boutry.devops.exception.ViolationException;
import org.boutry.devops.models.User;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Collection;
import java.util.Set;

@Path("/user")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {

    @Inject
    EntityManager entityManager;

    @Inject
    Validator validator;

    @GET
    public Collection<UserEntity> getUsers() {
        return UserEntity.listAll();
    }

    @GET
    @Path("/{id}")
    public UserEntity getUser(@PathParam("id") long id) {
        return UserEntity.findByIdOptional(id).orElseThrow(() -> new NotFoundException(String.format("Unable to find user id %s", id)));
    }

    @POST
    @Transactional
    public UserEntity createUser(User user) throws ViolationException {
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        if (!violations.isEmpty()) {
            throw new ViolationException(violations.toArray(new ConstraintViolation[]{}));
        }
        UserEntity userEntity = UserEntity.fromUser(user);
        userEntity.persist();
        return userEntity;
    }

    @PUT
    @Transactional
    public UserEntity updateUser(UserEntity userEntity) throws ViolationException {
        return entityManager.merge(userEntity);
    }

    @DELETE
    @Transactional
    public void deleteUser(UserEntity user) {
        validator.validate(user);
        UserEntity.delete(user);
    }

}
