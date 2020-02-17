package org.boutry.devops.resources;

import org.boutry.devops.models.User;
import org.boutry.devops.services.UserService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Collection;
import java.util.Optional;

@Path("/user")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {

    @Inject
    UserService service;

    @GET
    public Collection<User> getUsers() {
        return service.getUsers();
    }


    @GET
    @Path("/{id}")
    public User getUser(@PathParam("id") long id) {
        return Optional.ofNullable(service.getUser(id)).orElseThrow(() -> new NotFoundException(String.format("Unable to find user id %s", id)));
    }

    @POST
    public User createUser(User user) {
        service.addUser(user);
        return service.getUser(user.id);
    }

    @DELETE
    public void deleteUser(User user) {
        service.deleteUser(user);
    }

}
