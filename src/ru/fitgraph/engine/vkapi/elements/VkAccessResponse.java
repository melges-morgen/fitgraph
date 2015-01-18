package ru.fitgraph.engine.vkapi.elements;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by melges on 16.12.14.
 */
@XmlRootElement
public class VkAccessResponse {
    @XmlElement(name = "access_token")
    private String accessToken;

    @XmlElement(name = "user_id")
    private long userId;

    @XmlElement(name = "expires_in")
    private long expiresIn;

    @XmlElement(name = "email", required = false)
    private String email;

    public VkAccessResponse() {
    }

    public VkAccessResponse(String accessToken, long userId, long expiresIn) {
        this.accessToken = accessToken;
        this.userId = userId;
        this.expiresIn = expiresIn;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public long getUserId() {
        return userId;
    }

    /**
     * Time that access token is valid in milliseconds
     * @return time in milliseconds
     */
    public long getExpiresIn() {
        return expiresIn * 1000;
    }

    public String getEmail() {
        return email;
    }
}
