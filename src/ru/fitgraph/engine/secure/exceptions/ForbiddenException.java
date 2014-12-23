package ru.fitgraph.engine.secure.exceptions;

import javax.ws.rs.WebApplicationException;

/**
 * Created by melges on 19.12.14.
 */
public class ForbiddenException extends WebApplicationException {
    public ForbiddenException() {
    }

    public ForbiddenException(String message) {
        super(message);
    }
}
