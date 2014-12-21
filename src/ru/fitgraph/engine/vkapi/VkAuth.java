package ru.fitgraph.engine.vkapi;

import org.apache.log4j.Logger;
import org.glassfish.jersey.jackson.JacksonFeature;
import ru.fitgraph.engine.vkapi.elements.VkAccessResponse;
import ru.fitgraph.engine.vkapi.elements.VkErrorResponse;
import ru.fitgraph.engine.vkapi.exceptions.VkSideError;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by melges on 16.12.14.
 */
public class VkAuth {
    private final static String VK_ACCESS_URI = "https://oauth.vk.com/access_token";
    private static long APP_ID = 4684983;
    private static String APP_SECRET = "RZt2T2f1wIi1p5rt191k";

    private final static Logger logger = Logger.getLogger(VkAuth.class);

    /**
     * Get user information for provided code from vk. Associate vk user with out user, create session and return
     * session secret string, which should be sended to client.
     * @param code special string from client provided by vk.
     * @return parsed to object vk response.
     */
    public static VkAccessResponse auth(String code, String redirectUri) throws VkSideError {
        Client client = ClientBuilder.newBuilder().register(JacksonFeature.class).build();

        Response response;
        try {
            response = client.target(VK_ACCESS_URI)
                    .queryParam("client_id", APP_ID)
                    .queryParam("client_secret", APP_SECRET)
                    .queryParam("code", code)
                    .queryParam("redirect_uri", redirectUri)
                    .request(MediaType.APPLICATION_JSON)
                    .get();
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
            return response.readEntity(VkAccessResponse.class);
        } catch (RuntimeException ignore) {
            logger.warn(String.format("Vk return http ok code, but data in response is incorrect. Exception: %s",
                    ignore.getLocalizedMessage()));
            throw new VkSideError("Vk return http ok code, but data in response is incorrect", response.getStatus());
        }
    }
}
