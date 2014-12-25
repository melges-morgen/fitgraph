package ru.fitgraph.engine.secure;

import ru.fitgraph.database.users.UserController;
import ru.fitgraph.engine.vkapi.VkAuth;
import ru.fitgraph.engine.vkapi.elements.VkAccessResponse;
import ru.fitgraph.engine.vkapi.exceptions.VkSideError;

/**
 * Created by melges on 22.12.14.
 */
public class AuthController {

    public static boolean isSessionCorrect(Long vkId, String sessionSecret) {
        return UserController.getUserByVkAndSession(vkId, sessionSecret) != null;
    }

    public static Long auth(String code, String redirectUrl) throws VkSideError {
        VkAccessResponse response = VkAuth.auth(code, redirectUrl);
        //User

        return 0L;
    }
}
