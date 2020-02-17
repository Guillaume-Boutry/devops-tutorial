package org.boutry.devops.exception;

import javax.validation.ConstraintViolation;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.List;
import java.util.stream.Collectors;

@Provider
public class ViolationExceptionMapper implements ExceptionMapper<ViolationException> {

    @Override
    public Response toResponse(ViolationException e) {
        ConstraintViolation<?>[] violations = e.getViolations();
        String constraints = List.of(violations).stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining("\", \""));
        return Response.status(422).entity(String.format("{\"error\": \"Constraint violations\", \"constraints\": [\"%s\"]}", constraints)).build();
    }
}
