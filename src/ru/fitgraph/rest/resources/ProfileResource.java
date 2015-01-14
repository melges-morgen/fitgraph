package ru.fitgraph.rest.resources;

import org.hibernate.validator.constraints.NotEmpty;
import ru.fitgraph.database.users.User;
import ru.fitgraph.engine.secure.AuthController;
import ru.fitgraph.engine.vkapi.exceptions.VkSideError;

import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import java.net.MalformedURLException;
import java.net.URISyntaxException;

/**
 * Created by melges on 16.12.14.
 */
@Path("/profile")
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class ProfileResource {
    @GET
    @PermitAll
    @Path("/auth")
    public Response auth(
            @NotEmpty @QueryParam("code") String code,
            @Context HttpServletRequest request,
            @CookieParam(value = "JSESSIONID") String sessionId) throws MalformedURLException, VkSideError, URISyntaxException {
        String uri = request.getRequestURL().toString();

        return Response.ok()
                .cookie(new NewCookie("vkId", AuthController.auth(code, uri, sessionId).toString()))
                .build();
    }

    @GET
    public User getProfile(@Context HttpServletRequest request) {
        return new User("Melges");
    }

}
