package ru.fitgraph.rest.resources;

import org.hibernate.validator.constraints.NotEmpty;
import ru.fitgraph.engine.secure.AuthService;
import ru.fitgraph.engine.vkapi.VkAuth;
import ru.fitgraph.engine.vkapi.elements.VkAuthUri;
import ru.fitgraph.engine.vkapi.exceptions.VkSideError;

import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.MalformedURLException;
import java.net.URISyntaxException;

/**
 * Created by madu0815 on 14.03.2016.
 */
public class AuthResource {

    private String sessionId;

    public AuthResource(UriInfo uriInfo, String sessionId) {
        this.sessionId = sessionId;
    }

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
    public VkAuthUri getVkRequestUri(@Context UriInfo uriInfo) {
        return VkAuth.getClientAuthUri(uriInfo.getBaseUriBuilder()
                .path(ProfileResource.class) // Add class path
                .path(ProfileResource.class, "auth").build());
    }

    /**
     * Auth user by code and set the client cookie. For comparability with browser application method return html page
     * with javascript which should close the window.
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
        if(sessionId == null)
            sessionId = AuthService.generateSessionSecret();

        NewCookie vkIdCookie = new NewCookie(AuthService.VK_ID_COOKIE_NAME, AuthService.auth(code, uri, sessionId).toString(), "/", null,
                null, 2629744, false); // Valid for a month
        NewCookie sessionIdCookie = new NewCookie(AuthService.SESSION_COOKIE_NAME, sessionId, "/", null,
                null, 2629744, false); // Valid for a month

        return Response.ok()
                .cookie(vkIdCookie)
                .cookie(sessionIdCookie)
                .build();

    }
}
