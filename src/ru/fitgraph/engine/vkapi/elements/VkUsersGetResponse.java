package ru.fitgraph.engine.vkapi.elements;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created by melges on 13.01.15.
 */
@XmlRootElement
public class VkUsersGetResponse {
    @XmlElement(name = "response")
    List<VkUserInfo> users;

    public VkUsersGetResponse() {
        users = null;
    }

    public List<VkUserInfo> getUsers() {
        return users;
    }
}
