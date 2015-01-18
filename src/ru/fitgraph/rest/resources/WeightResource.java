package ru.fitgraph.rest.resources;

import ru.fitgraph.database.WeightPoint;
import ru.fitgraph.database.users.User;
import ru.fitgraph.database.users.UserController;

import javax.ws.rs.CookieParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
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
    public List<WeightPoint> getPoints() {
        return null;
    }
}
