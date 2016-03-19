package ru.fitgraph.rest.resources;

import org.springframework.stereotype.Component;
import ru.fitgraph.database.entities.User;
import ru.fitgraph.database.repositories.UserRepository;
import ru.fitgraph.engine.secure.AuthService;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

/**
 * Resource which is used for auth and work with profile.
 *
 * If client must be authorized for calling method but it is not, unauthorized response code will be
 * returned to client, and body will contain error object with description.
 *
 * @author Morgen Matvey
 */
@Component
@Path("/profile")
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class ProfileResource {
    /**
     * Field storing the session id of the user if the user has no session it is null.
     *
     * Can be null only in permitted for all methods.
     */
    @CookieParam(value = AuthService.SESSION_COOKIE_NAME)
    private String sessionId;

    /**
     * Field storing the vk id of the user if the user not authorized it is null.
     *
     * Can be null only in permitted for all methods.
     */
    @CookieParam(value = "vkId")
    private Long vkId;

    /**
     * Return user profile object. Only for authorized users.
     * @return profile object.
     */
    @GET
    @Path("/me")
    public User getMyProfile() {
        return UserRepository.getUserByVkAndSession(vkId, sessionId);
    }

    /** Finds a weight points resource for working with weight points of given user
     *
     * @param id id of user to get points for
     * @return weightResource
     */
    @Path("/{id}/weight")
    public WeightResource getWeightResource(@PathParam("id") long id) {
        return new WeightResource(id);
    }

    /**
     * Finds a schedule resource for working with drugs, weight points and so on
     * @param id id of user to finds schedule for
     * @return scheduleResource
     */
    @Path("/{id}/schelude")
    public ScheduleResource getScheduleResource(@PathParam("id") long id) {
        return new ScheduleResource(id);
    }

    @Path("/auth")
    public AuthResource getAuthResource(@Context UriInfo uriInfo, String sessionId) {
        return new AuthResource(uriInfo, sessionId);
    }
}
