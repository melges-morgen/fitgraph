package ru.fitgraph.rest.exceptions;

import ru.fitgraph.engine.secure.NotAuthorizedException;
import ru.fitgraph.rest.elements.ErrorResponse;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Created by melges on 18.12.14.
 */
@Provider
public class NotAuthorizedExceptionMapper implements ExceptionMapper<NotAuthorizedException> {
    @Override
    public Response toResponse(NotAuthorizedException exception) {
        ErrorResponse response =
                new ErrorResponse(exception.getClass().getSimpleName(), exception.getLocalizedMessage());
        return Response.status(Response.Status.UNAUTHORIZED).entity(response).build();
    }
}
