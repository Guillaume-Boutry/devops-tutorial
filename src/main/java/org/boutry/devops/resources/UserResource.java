package org.boutry.devops.resources;

import org.boutry.devops.entities.UserEntity;
import org.boutry.devops.exception.ViolationException;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.Collection;
import java.util.Optional;
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
        Optional<UserEntity> userOptional = UserEntity.findByIdOptional(id);
        if (userOptional.isEmpty()) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return userOptional.get();
    }

    @POST
    @Transactional
    public Response createUser(@Context UriInfo uriInfo, UserEntity newUser) throws ViolationException {
        Set<ConstraintViolation<UserEntity>> violations = validator.validate(newUser);
        if (!violations.isEmpty()) {
            throw new ViolationException(violations.toArray(new ConstraintViolation[]{}));
        }
        UserEntity user = new UserEntity();
        user.email = newUser.email;
        user.lastname = newUser.lastname;
        user.firstname = newUser.firstname;
        user.persist();
        URI uri = uriInfo.getAbsolutePathBuilder()
                .path(UserResource.class, "getUser")
                .build(user.id);
        return Response.created(uri).build();
    }

    @PUT
    @Transactional
    public void updateUser(UserEntity userEntity) throws ViolationException {
        Optional<UserEntity> userOptional = UserEntity.findByIdOptional(userEntity.id);
        if (userOptional.isEmpty()) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        UserEntity user = userOptional.get();
        user.firstname = userEntity.firstname;
        user.lastname = userEntity.lastname;
        user.email = userEntity.email;
    }

    @DELETE
    @Transactional
    public void deleteUser(UserEntity user) {
        Optional<UserEntity> userOptional = UserEntity.findByIdOptional(user.id);
        if (userOptional.isEmpty()) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        userOptional.get().delete();
    }

}
