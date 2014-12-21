package ru.fitgraph.rest.resources;

import org.hibernate.validator.constraints.NotEmpty;
import ru.fitgraph.database.users.User;
import ru.fitgraph.engine.vkapi.VkAuth;
import ru.fitgraph.engine.vkapi.elements.VkAccessResponse;
import ru.fitgraph.engine.vkapi.exceptions.VkSideError;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by melges on 16.12.14.
 */
@Path("/profile")
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class ProfileResource {
    @GET
    @Path("/auth")
    public VkAccessResponse auth(
            @NotEmpty @QueryParam("code") String code,
            @Context HttpServletRequest request) throws MalformedURLException, VkSideError, URISyntaxException {
        URI uri = new URI(request.getRequestURI());
        return VkAuth.auth(code, String.format("%s://%s%s", uri.getScheme(), uri.getAuthority(), uri.getPath()));
    }

    @GET
    public User getProfile(@Context HttpServletRequest request) {
        return new User("Melges");
    }

}
