package ru.fitgraph.rest.resources;

import ru.fitgraph.database.users.UserController;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Created by melges on 16.12.14.
 */
@Path("/auth")
public class Auth {
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public String returnHello() {
        UserController c = new UserController();
        return "Hello world";
    }
}
