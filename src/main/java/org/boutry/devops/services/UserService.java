package org.boutry.devops.services;

import org.boutry.devops.entities.UserEntity;
import org.boutry.devops.exception.ViolationException;
import org.boutry.devops.resources.UserResource;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;

@ApplicationScoped
public class UserService {
    @Inject
    Validator validator;


    public Collection<UserEntity> getAllUsers() {
        return UserEntity.listAll();
    }

    public UserEntity getUser(long id) {
        Optional<UserEntity> userOptional = UserEntity.findByIdOptional(id);
        if (userOptional.isEmpty()) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return userOptional.get();
    }

    public Response createUser(UriInfo uriInfo, UserEntity newUser) throws ViolationException {
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

    public void modifyUser(UserEntity userEntity) {
        Optional<UserEntity> userOptional = UserEntity.findByIdOptional(userEntity.id);
        if (userOptional.isEmpty()) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        UserEntity user = userOptional.get();
        user.firstname = userEntity.firstname;
        user.lastname = userEntity.lastname;
        user.email = userEntity.email;
    }

    public void deleteUser(UserEntity userEntity) {
        Optional<UserEntity> userOptional = UserEntity.findByIdOptional(userEntity.id);
        if (userOptional.isEmpty()) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        userOptional.get().delete();
    }
}
