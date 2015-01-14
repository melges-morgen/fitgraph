package ru.fitgraph.engine.vkapi;

import org.apache.log4j.Logger;
import ru.fitgraph.engine.vkapi.elements.VkAccessResponse;
import ru.fitgraph.engine.vkapi.exceptions.VkSideError;

import java.util.HashMap;
import java.util.Map;

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
        final Map<String, Object> parametersMap = new HashMap<String, Object>();
        parametersMap.put("client_id", APP_ID);
        parametersMap.put("client_secret", APP_SECRET);
        parametersMap.put("code", code);
        parametersMap.put("redirect_uri", redirectUri);

        return VkClient.call(VK_ACCESS_URI, parametersMap, VkAccessResponse.class);
    }
}
