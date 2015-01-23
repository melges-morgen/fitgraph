package ru.fitgraph.engine.secure;

import ru.fitgraph.database.users.User;
import ru.fitgraph.database.users.UserController;
import ru.fitgraph.engine.vkapi.VkAuth;
import ru.fitgraph.engine.vkapi.VkUsers;
import ru.fitgraph.engine.vkapi.elements.VkAccessResponse;
import ru.fitgraph.engine.vkapi.elements.VkUserInfo;
import ru.fitgraph.engine.vkapi.exceptions.VkSideError;

/**
 * Class contain methods for work with user authentication.
 */
public class AuthController {

    /**
     * Method check data provided by client for correctness and session validity.
     * @param vkId vkId of user, provided by client.
     * @param sessionSecret session id provided by client (session secret).
     * @return true if clint give correct data, or false if client is liar or his session expired.
     */
    public static boolean isSessionCorrect(Long vkId, String sessionSecret) {
        return UserController.getUserByVkAndSession(vkId, sessionSecret) != null;
    }

    /**
     * Method authorize client by provided data using vk oauth protocol. If user client authenticate at first time,
     * new user will be created. In other cases we find who is authenticate and create new session.
     *
     * @param code code provided by client (client must request it from vk first).
     * @param redirectUrl redirect uri which client provide to vk client and which client call for invoke auth.
     * @param sessionSecret client session secret.
     * @return vk id returned from
     * @throws VkSideError if vk say about error, or we have a trouble when connecting to vk.
     */
    public static Long auth(String code, String redirectUrl, String sessionSecret) throws VkSideError {
        VkAccessResponse response = VkAuth.auth(code, redirectUrl);
        User user = UserController.getUserByVkId(response.getUserId());
        if(user == null) {
            VkUserInfo vkUserInfo = VkUsers.get(response.getUserId(), response.getAccessToken());
            user = new User(vkUserInfo.getFullName(), response.getEmail(), vkUserInfo.getSex(),
                    vkUserInfo.getBirthDate(),
                    response.getUserId(), sessionSecret,
                    response.getAccessToken(), response.getExpiresIn());
        } else {
            user.addSession(sessionSecret, response.getAccessToken(), response.getExpiresIn());
        }

        UserController.saveOrUpdate(user);

        return response.getUserId();
    }
}
