package ru.fitgraph.engine.vkapi;

import org.apache.log4j.Logger;
import ru.fitgraph.engine.vkapi.elements.VkUserInfo;
import ru.fitgraph.engine.vkapi.elements.VkUsersGetResponse;
import ru.fitgraph.engine.vkapi.exceptions.VkSideError;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by melges on 13.01.15.
 */
public class VkUsers {
    private final static String VK_API_URI = "https://api.vk.com/method/";

    private final static Logger logger = Logger.getLogger(VkAuth.class);

    public static VkUserInfo get(Long vkId, String accessToken) throws VkSideError {
        final String methodName = "users.get";
        final Map<String, Object> parametersMap = new HashMap<String, Object>();
        parametersMap.put("user_ids", vkId);
        parametersMap.put("fields", "photo_max,bdate,sex");
        parametersMap.put("access_token", accessToken);

        VkUsersGetResponse response = VkClient.call(VK_API_URI + methodName, parametersMap, VkUsersGetResponse.class);

        return response.getUsers().get(0);
    }
}
