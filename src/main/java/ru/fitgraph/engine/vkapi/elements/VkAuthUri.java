package ru.fitgraph.engine.vkapi.elements;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Object used for serialization of generated auth uri to vk servers.
 */
@XmlRootElement
public class VkAuthUri {
    @XmlElement(name = "auth_uri")
    private String authUri;

    public VkAuthUri() {
    }

    public VkAuthUri(String authUri) {
        this.authUri = authUri;
    }

    public String getAuthUri() {
        return authUri;
    }
}
