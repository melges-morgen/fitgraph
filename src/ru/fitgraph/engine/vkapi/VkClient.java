package ru.fitgraph.engine.vkapi;

import org.apache.log4j.Logger;
import org.glassfish.jersey.jackson.JacksonFeature;
import ru.fitgraph.engine.vkapi.elements.VkErrorResponse;
import ru.fitgraph.engine.vkapi.exceptions.VkSideError;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Map;

/**
 * Created by melges on 13.01.15.
 */
public class VkClient {
    private final static Logger logger = Logger.getLogger(VkClient.class);

    public static <T> T call(String targetUri, Map<String, Object> parameters, Class<T> entityClass) throws VkSideError {
        Client client = ClientBuilder.newBuilder().register(JacksonFeature.class).build();

        Response response;
        try {
            WebTarget target = client.target(targetUri);
            for(Map.Entry<String, Object> entry : parameters.entrySet())
                target = target.queryParam(entry.getKey(), entry.getValue());
            target = target.queryParam("v", "5.27");

            response = target.request(MediaType.APPLICATION_JSON).get();
        } catch (RuntimeException ignore) {
            logger.warn(String.format("Can't processing with vk servers: %s", ignore.getLocalizedMessage()));
            throw new VkSideError(ignore.getLocalizedMessage());
        }

        if(response.getStatus() != Response.Status.OK.getStatusCode()) {
            try {
                VkErrorResponse error = response.readEntity(VkErrorResponse.class);
                logger.warn(String.format("Response from vk not equals 200, it is: %d. Error description from vk: %s",
                        response.getStatus(), error.getErrorDescription()));

                throw new VkSideError(error.getErrorDescription(), response.getStatus());
            } catch (RuntimeException ignored) {
                logger.warn(String.format("Vk return error without description with code: %d. Exception: %s",
                        response.getStatus(), ignored.getLocalizedMessage()));
                ignored.printStackTrace();
                throw new VkSideError("Vk return error without description", response.getStatus());
            }
        }

        try {
            return response.readEntity(entityClass);
        } catch (RuntimeException ignore) {
            logger.warn(String.format("Vk return http ok code, but data in response is incorrect. Exception: %s",
                    ignore.getLocalizedMessage()));
            throw new VkSideError("Vk return http ok code, but data in response is incorrect", response.getStatus());
        }
    }
}
