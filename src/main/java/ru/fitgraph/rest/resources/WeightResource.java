package ru.fitgraph.rest.resources;

import org.springframework.stereotype.Component;
import ru.fitgraph.database.entities.WeightPoint;
import ru.fitgraph.database.entities.User;
import ru.fitgraph.database.repositories.UserRepository;
import ru.fitgraph.database.repositories.WeightPointRepository;
import ru.fitgraph.engine.secure.AuthService;
import ru.fitgraph.rest.elements.ChangeWeightPointRequest;
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
@Component
@Path("/weight")
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class WeightResource {
    /**
     * Field storing the session id of the user if the user has no session it is null.
     *
     * Can be null only in permitted for all methods.
     */
    @CookieParam(value = AuthService.SESSION_COOKIE_NAME)
    protected  String sessionId;

    /**
     * Field storing the vk id of the user if the user not authorized it is null.
     *
     * Can be null only in permitted for all methods.
     */
    @CookieParam(value = "vkId")
    protected Long vkId;

    public WeightResource(long id) {

    }

    /**
     * Return all weight points of user. User detected by context vkId and session.
     * @return all weight points of the user.
     */
    @GET
    public List<WeightPoint> getAllPoints() {
        User user = UserRepository.getUserByVkAndSession(vkId, sessionId);
        return new ArrayList<WeightPoint>(user.getWeightPoints());
    }

    /**
     * Searches weight points by specified period.
     * @param startDate minimum date which point must have.
     * @param endDate maximum date which point must have.
     * @return users points by specified period.
     */
    @GET
    @Path("/points")
    public List<WeightPoint> getPoints(@NotNull @QueryParam("startDate") DateParameter startDate,
                                       @NotNull @QueryParam("endDate") DateParameter endDate) {
        return WeightPointRepository.getVkUserWeightPointsBetween(vkId, startDate.getDate(), endDate.getDate());
    }

    /**
     * Add new point or rewrite if point on the same date already exist.
     * @param date date of point in format.
     * @param weight weight of point.
     */
    @PUT
    @Path("/points")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public void addPointUrlEncoded(@NotNull @FormParam("date") DateParameter date,
                                   @NotNull @FormParam("weight") Double weight) {
        User user = UserRepository.getUserByVkAndSession(vkId, sessionId);
        user.addWeightPoint(date.getDate(), weight);
        UserRepository.saveOrUpdate(user);
    }

    /**
     * Add new point or rewrite if point on the same date already exist.
     * @param point json encoded weight point.
     */
    @PUT
    @Path("/points")
    public void addPoint(WeightPoint point) {
        User user = UserRepository.getUserByVkAndSession(vkId, sessionId);
        user.addWeightPoint(point);
        UserRepository.saveOrUpdate(user);
    }

    @POST
    @Path("/points")
    public void changePoint(@NotNull ChangeWeightPointRequest request) {
        WeightPoint pointToChange = WeightPointRepository.getPointByParameters(vkId, request.getOldPoint().getDate());
        if(pointToChange == null)
            throw new NotFoundException("Specified point not founded");

        pointToChange.inheritParameters(request.getNewPoint());
        WeightPointRepository.saveOrUpdate(pointToChange);
    }

    /**
     * Delete specified point. Point would found by date and user vk id (from context).
     * @param pointDateParam date of point which should be deleted.
     */
    @DELETE
    @Path("/points")
    public void deletePoint(@NotNull @QueryParam("date") DateParameter pointDateParam) {
        WeightPoint pointToDelete = WeightPointRepository.getPointByParameters(vkId, pointDateParam.getDate());
        if(pointToDelete == null)
            throw new NotFoundException(String.format("Weight point at %s for %d does not exist.",
                    pointDateParam.getDate().toString(), vkId));

        pointToDelete.setDeleted(true);
        WeightPointRepository.saveOrUpdate(pointToDelete);
    }
}
