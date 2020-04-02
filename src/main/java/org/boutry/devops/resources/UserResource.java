package org.boutry.devops.resources;

import org.boutry.devops.entities.CatEntity;
import org.boutry.devops.entities.UserEntity;
import org.boutry.devops.exception.ViolationException;
import org.boutry.devops.services.UserService;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.Collection;
import java.util.List;

@Path("/user")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {


    @Inject
    UserService service;

    @GET
    public Collection<UserEntity> getUsers() {
        return service.getAllUsers();
    }

    @GET
    @Path("/{id}")
    public UserEntity getUser(@PathParam("id") long id) {
        return service.getUser(id);
    }

    @POST
    @Transactional
    public Response createUser(@Context UriInfo uriInfo, UserEntity newUser) throws ViolationException {
        return service.createUser(uriInfo, newUser);
    }

    @PUT
    @Transactional
    public void updateUser(UserEntity userEntity) throws ViolationException {
        service.modifyUser(userEntity);
    }

    @DELETE
    @Transactional
    public void deleteUser(UserEntity userEntity) {
        service.deleteUser(userEntity);
    }

    @GET
    @Path("/{id}/cats")
    public List<CatEntity> getUserCats(@PathParam("id") long id) {
        UserEntity userEntity = service.getUser(id);
        return service.getUserCats(userEntity);
    }
}
