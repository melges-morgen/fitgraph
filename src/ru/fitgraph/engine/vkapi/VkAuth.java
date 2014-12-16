package ru.fitgraph.engine.vkapi;

import javax.net.ssl.HttpsURLConnection;
import javax.ws.rs.core.UriBuilder;
import java.io.IOException;
import java.net.URI;

/**
 * Created by melges on 16.12.14.
 */
public class VkAuth {
    private final static String VK_ACCESS_URI = "https://oauth.vk.com/access_token";
    private static long APP_ID = 4684096;
    private static String APP_SECRET = "";

    /**
     * Get user information for provided code from vk. Associate vk user with out user, create session and return
     * session secret string, which should be sended to client.
     * @param code special string from client provided by vk.
     * @return parsed to object vk response.
     */
    static VkAccessResponse auth(String code, String redirectUri) throws IOException {
        URI uri = UriBuilder.fromPath(VK_ACCESS_URI)
                .queryParam("client_id", APP_ID)
                .queryParam("client_secret", APP_SECRET)
                .queryParam("code", code)
                .queryParam("redirect_uri", redirectUri)
                .build();

        HttpsURLConnection connection = (HttpsURLConnection) uri.toURL().openConnection();

        int responseCode = connection.getResponseCode();
        if(responseCode != HttpsURLConnection.HTTP_OK)
            throw new IOException(String.format("Invalid response from vk server, should be 200: %d", responseCode));


        return new VkAccessResponse("", 0, 0);
    }
}
