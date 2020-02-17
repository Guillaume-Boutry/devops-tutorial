package org.boutry.devops.exception;

import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ConstraintViolationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {

    @Override
    public Response toResponse(ConstraintViolationException e) {
        String violation = e.getMessage();
        return Response.status(409).entity(String.format("{\"error\": \"Constraint violations\", \"constraints\": [\"%s\"]}", violation)).build();
    }
}
