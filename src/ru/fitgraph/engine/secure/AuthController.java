package ru.fitgraph.engine.secure;

import ru.fitgraph.database.users.User;
import ru.fitgraph.database.users.UserController;
import ru.fitgraph.engine.vkapi.VkAuth;
import ru.fitgraph.engine.vkapi.VkUsers;
import ru.fitgraph.engine.vkapi.elements.VkAccessResponse;
import ru.fitgraph.engine.vkapi.elements.VkUserInfo;
import ru.fitgraph.engine.vkapi.exceptions.VkSideError;

/**
 * Created by melges on 22.12.14.
 */
public class AuthController {

    public static boolean isSessionCorrect(Long vkId, String sessionSecret) {
        return UserController.getUserByVkAndSession(vkId, sessionSecret) != null;
    }

    public static Long auth(String code, String redirectUrl, String sessionSecret) throws VkSideError {
        VkAccessResponse response = VkAuth.auth(code, redirectUrl);
        User user = UserController.getUserByVkId(response.getUserId());
        if(user == null) {
            VkUserInfo vkUserInfo = VkUsers.get(response.getUserId(), response.getAccessToken());
            user = new User(vkUserInfo.getFullName(), response.getEmail(), response.getUserId(), sessionSecret,
                    response.getAccessToken(), response.getExpiresIn());
        } else {
            user.addSession(sessionSecret, response.getAccessToken(), response.getExpiresIn());
        }

        UserController.persist(user);

        return response.getUserId();
    }
}
