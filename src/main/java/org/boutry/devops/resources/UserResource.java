package org.boutry.devops.resources;

import org.boutry.devops.entities.UserEntity;
import org.boutry.devops.models.User;

import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Collection;

@Path("/user")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {

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
    public UserEntity createUser(User user) {
        UserEntity userEntity = UserEntity.fromUser(user);
        userEntity.persist();
        return userEntity;
    }

    @DELETE
    @Transactional
    public void deleteUser(UserEntity user) {
        UserEntity.delete(user);
    }

}
