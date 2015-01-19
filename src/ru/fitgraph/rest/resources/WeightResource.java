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
 * Created by melges on 17.01.15.
 */
@Path("/weight")
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class WeightResource {
    @CookieParam(value = "JSESSIONID")
    private String sessionId;

    @CookieParam(value = "vkId")
    private Long vkId;

    @GET
    public List<WeightPoint> getAllPoints() {
        User user = UserController.getUserByVkAndSession(vkId, sessionId);
        return new ArrayList<WeightPoint>(user.getWeightPoint().values());
    }

    @GET
    @Path("/getpoints")
    public List<WeightPoint> getPoints(@NotNull @QueryParam("startDate") DateParameter startDate,
                                       @NotNull @QueryParam("endDate") DateParameter endDate) {
        return WeightPointController.getVkUserWeightPointsBetween(vkId, startDate.getDate(), endDate.getDate());
    }
}
