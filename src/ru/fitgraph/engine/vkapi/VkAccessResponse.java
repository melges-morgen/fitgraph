package ru.fitgraph.engine.vkapi;

/**
 * Created by melges on 16.12.14.
 */
public class VkAccessResponse {
    private String accessToken;
    private long userId;
    private long expiresIn;

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

    public long getExpiresIn() {
        return expiresIn;
    }
}
