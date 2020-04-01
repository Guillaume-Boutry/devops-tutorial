package org.boutry.devops.services;

import org.boutry.devops.entities.CatEntity;
import org.boutry.devops.entities.UserEntity;
import org.boutry.devops.exception.ViolationException;
import org.boutry.devops.resources.CatResource;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;

@ApplicationScoped
public class CatService {
    @Inject
    Validator validator;

    public Collection<CatEntity> getAllCats() {
        return CatEntity.listAll();
    }

    public CatEntity getCat(long id) {
        Optional<CatEntity> catEntityOptional = CatEntity.findByIdOptional(id);
        if (catEntityOptional.isEmpty()) {
            throw new NotFoundException("User not found");
        }
        return catEntityOptional.get();
    }

    public Response createCat(UriInfo uriInfo, CatEntity newCat) throws ViolationException {
        Set<ConstraintViolation<CatEntity>> violations = validator.validate(newCat);
        if (!violations.isEmpty()) {
            throw new ViolationException(violations.toArray(new ConstraintViolation[]{}));
        }
        CatEntity cat = new CatEntity();
        Optional<UserEntity> ownerOpional = UserEntity.findByIdOptional(newCat.owner.id);
        if (ownerOpional.isEmpty()) {
            throw new BadRequestException("User doesn't exist");
        }
        cat.name = newCat.name;
        cat.owner = ownerOpional.get();
        cat.persist();

        URI uri = uriInfo.getAbsolutePathBuilder()
                .path(CatResource.class, "getCat")
                .build(cat.id);
        return Response.created(uri).build();
    }

    public void modifyCat(CatEntity catEntity) {
        Optional<CatEntity> catEntityOptional = UserEntity.findByIdOptional(catEntity.id);
        if (catEntityOptional.isEmpty()) {
            throw new NotFoundException("Cat not found");
        }
        Optional<UserEntity> ownerOpional = UserEntity.findByIdOptional(catEntity.owner.id);
        if (ownerOpional.isEmpty()) {
            throw new BadRequestException("User doesn't exist");
        }
        CatEntity catEntity1 = catEntityOptional.get();
        catEntity1.name = catEntity.name;
        catEntity1.owner = ownerOpional.get();

    }

    public void deleteCat(CatEntity catEntity) {
        Optional<CatEntity> catEntityOptional = UserEntity.findByIdOptional(catEntity.id);
        if (catEntityOptional.isEmpty()) {
            throw new NotFoundException("User not found");
        }
        catEntityOptional.get().delete();
    }

}
