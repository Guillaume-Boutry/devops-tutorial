package org.boutry.devops.services;

import org.boutry.devops.entities.CatEntity;
import org.boutry.devops.entities.UserEntity;
import org.boutry.devops.exception.ViolationException;
import org.boutry.devops.resources.UserResource;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.Collection;
import java.util.List;
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
            throw new NotFoundException("User not found");
        }
        return userOptional.get();
    }

    @Transactional
    public UserEntity createUser(UserEntity newUser) throws ViolationException {
        Set<ConstraintViolation<UserEntity>> violations = validator.validate(newUser);
        if (!violations.isEmpty()) {
            throw new ViolationException(violations.toArray(new ConstraintViolation[]{}));
        }
        UserEntity user = new UserEntity();
        user.email = newUser.email;
        user.lastname = newUser.lastname;
        user.firstname = newUser.firstname;
        user.persist();

        return user;
    }

    @Transactional
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

    @Transactional
    public void modifyUser(UserEntity userEntity) {
        Optional<UserEntity> userOptional = UserEntity.findByIdOptional(userEntity.id);
        if (userOptional.isEmpty()) {
            throw new NotFoundException("User not found");
        }
        UserEntity user = userOptional.get();
        user.firstname = userEntity.firstname;
        user.lastname = userEntity.lastname;
        user.email = userEntity.email;
    }

    @Transactional
    public void deleteUser(UserEntity userEntity) {
        Optional<UserEntity> userOptional = UserEntity.findByIdOptional(userEntity.id);
        if (userOptional.isEmpty()) {
            throw new NotFoundException("User not found");
        }
        userOptional.get().delete();
    }

    public List<CatEntity> getUserCats(UserEntity userEntity) {
        return CatEntity.list("owner_id", userEntity.id);
    }
}
