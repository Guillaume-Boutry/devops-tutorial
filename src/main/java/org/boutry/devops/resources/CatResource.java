package org.boutry.devops.resources;

import org.boutry.devops.entities.CatEntity;
import org.boutry.devops.exception.ViolationException;
import org.boutry.devops.services.CatService;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.Collection;

@Path("/cat")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CatResource {
    @Inject
    CatService service;

    @GET
    public Collection<CatEntity> getCats() {
        return service.getAllCats();
    }

    @GET
    @Path("/{id}")
    public CatEntity getCat(@PathParam("id") long id) {
        return service.getCat(id);
    }

    @POST
    @Transactional
    public Response createCat(@Context UriInfo uriInfo, CatEntity newCat) throws ViolationException {
        System.out.println(newCat);
        return service.createCat(uriInfo, newCat);
    }

    @PUT
    @Transactional
    public void updateCat(CatEntity catEntity) throws ViolationException {
        service.modifyCat(catEntity);
    }

    @DELETE
    @Transactional
    public void deleteCat(CatEntity catEntity) {
        service.deleteCat(catEntity);
    }
}
