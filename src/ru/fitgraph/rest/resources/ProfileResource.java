package ru.fitgraph.rest.resources;

import org.hibernate.validator.constraints.NotEmpty;
import ru.fitgraph.database.users.User;
import ru.fitgraph.database.users.UserController;
import ru.fitgraph.engine.secure.AuthController;
import ru.fitgraph.engine.vkapi.VkAuth;
import ru.fitgraph.engine.vkapi.exceptions.VkSideError;

import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.MalformedURLException;
import java.net.URISyntaxException;

/**
 * Created by melges on 16.12.14.
 */
@Path("/profile")
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class ProfileResource {
    @CookieParam(value = "JSESSIONID")
    private String sessionId;

    @CookieParam(value = "vkId")
    private Long vkId;

    @GET
    @PermitAll
    @Path("/getvkrequesturi")
    public String getVkRequestUri(@Context UriInfo uriInfo) {
        return VkAuth.getClientAuthUri(uriInfo.getBaseUriBuilder()
                .path(ProfileResource.class) // Add class path
                .path(ProfileResource.class, "auth").build());
    }

    @GET
    @PermitAll
    @Path("/auth")
    public Response auth(
            @NotEmpty @QueryParam("code") String code,
            @Context HttpServletRequest request) throws MalformedURLException, VkSideError, URISyntaxException {
        String uri = request.getRequestURL().toString();

        NewCookie vkIdCookie = new NewCookie("vkId", AuthController.auth(code, uri, sessionId).toString(), "/", null,
                null, 2629744, false); // Valid for a month
        return Response.ok()
                .cookie(vkIdCookie)
                .build();
    }

    @GET
    public User getProfile() {
        return UserController.getUserByVkAndSession(vkId, sessionId);
    }
}
