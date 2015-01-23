package ru.fitgraph.rest.resources;

import ru.fitgraph.database.weight.WeightPoint;
import ru.fitgraph.database.users.User;
import ru.fitgraph.database.users.UserController;
import ru.fitgraph.database.weight.WeightPointController;
import ru.fitgraph.rest.elements.DateParameter;

import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

/**
 * Resource which is used for access and work with weight.
 * For all weight methods date must be in format dd.MM.yyyy-hh:MM:ss {@link java.text.SimpleDateFormat}
 *
 * If client must be authorized for calling method but it is not, unauthorized response code will be
 * returned to client, and body will contain error object with description.
 *
 * @author Morgen Matvey
 */
@Path("/weight")
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class WeightResource {
    /**
     * Field storing the session id of the user if the user has no session it is null.
     *
     * Can be null only in permitted for all methods.
     */
    @CookieParam(value = "JSESSIONID")
    protected  String sessionId;

    /**
     * Field storing the vk id of the user if the user not authorized it is null.
     *
     * Can be null only in permitted for all methods.
     */
    @CookieParam(value = "vkId")
    protected Long vkId;

    /**
     * Return all weight points of user. User detected by context vkId and session.
     * @return all weight points of the user.
     */
    @GET
    public List<WeightPoint> getAllPoints() {
        User user = UserController.getUserByVkAndSession(vkId, sessionId);
        return new ArrayList<WeightPoint>(user.getWeightPoints());
    }

    /**
     * Searches weight points by specified period.
     * @param startDate minimum date which point must have.
     * @param endDate maximum date which point must have.
     * @return users points by specified period.
     */
    @GET
    @Path("/getPoints")
    public List<WeightPoint> getPoints(@NotNull @QueryParam("startDate") DateParameter startDate,
                                       @NotNull @QueryParam("endDate") DateParameter endDate) {
        return WeightPointController.getVkUserWeightPointsBetween(vkId, startDate.getDate(), endDate.getDate());
    }

    /**
     * Add new point or rewrite if point on the same date already exist.
     * @param date date of point in format.
     * @param weight weight of point.
     */
    @PUT
    @Path("/addPoint")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public void addPoint(@NotNull @FormParam("date") DateParameter date,
                         @NotNull @FormParam("weight") Double weight) {
        User user = UserController.getUserByVkAndSession(vkId, sessionId);
        user.addWeightPoint(date.getDate(), weight);
        UserController.saveOrUpdate(user);
    }
}
