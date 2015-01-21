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
 * Resource which is used for auth and work with profile.
 *
 * If client must be authorized for calling method but it is not, unauthorized response code will be
 * returned to client, and body will contain error object with description.
 *
 * @author Morgen Matvey
 */
@Path("/profile")
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class ProfileResource {
    /**
     * Field storing the session id of the user if the user has no session it is null.
     *
     * Can be null only in permitted for all methods.
     */
    @CookieParam(value = "JSESSIONID")
    private String sessionId;

    /**
     * Field storing the vk id of the user if the user not authorized it is null.
     *
     * Can be null only in permitted for all methods.
     */
    @CookieParam(value = "vkId")
    private Long vkId;

    /**
     * Return uri referring to which the client will receive the code necessary
     * to authenticate and from which they will be redirected back to our
     * resource for authentication. Method wouldn't redirect
     *
     * Method allowed for all.
     * @param uriInfo information about resource, uri injected by context.
     * @return redirect uri as string.
     */
    @GET
    @PermitAll
    @Path("/getVkRequestUri")
    public String getVkRequestUri(@Context UriInfo uriInfo) {
        return VkAuth.getClientAuthUri(uriInfo.getBaseUriBuilder()
                .path(ProfileResource.class) // Add class path
                .path(ProfileResource.class, "auth").build());
    }

    /**
     * Auth user by code and set the client cookie.
     *
     * Yo should call {@link #getVkRequestUri(javax.ws.rs.core.UriInfo)}
     * for get the url which client must call, to get the code.
     * @param code code that vk returned to the client.
     * @param request information about request, injected by context.
     * @return response with new cookie, or error object with description.
     * @throws MalformedURLException
     * @throws VkSideError
     * @throws URISyntaxException
     */
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

    /**
     * Return user profile object. Only for authorized users.
     * @return profile object.
     */
    @GET
    public User getProfile() {
        return UserController.getUserByVkAndSession(vkId, sessionId);
    }
}
