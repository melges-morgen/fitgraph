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

    /** Delegates work with weight to weight resource using DI
     *
     * @return WeightResource class
     */
    @Path("/{id}/weight")
    public Class<ScheduleResource> getWeightResource() {
        return ScheduleResource.class;
    }

    /**
     * Delegates work with drugs, weight points and so on to schedule resource using DI
     *
     * @return ScheduleResource class
     */
    @Path("/{id}/schelude")
    public Class<ScheduleResource> getScheduleResource() {
        return ScheduleResource.class;
    }

    /**
     * Delegates authorization to special resource using DI
     *
     * @return AuthResource class
     */
    @Path("/auth")
    public Class<AuthResource> getAuthResource() {
        return AuthResource.class;
    }
}
