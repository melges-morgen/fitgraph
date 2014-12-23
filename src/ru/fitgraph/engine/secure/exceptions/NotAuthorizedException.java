package ru.fitgraph.engine.secure.exceptions;

import javax.ws.rs.WebApplicationException;

/**
 * Created by melges on 18.12.14.
 */
public class NotAuthorizedException extends WebApplicationException {
    public NotAuthorizedException() {
    }

    public NotAuthorizedException(String message) {
        super(message);
    }
}
