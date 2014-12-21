package ru.fitgraph.engine.vkapi.exceptions;

import java.rmi.RemoteException;

/**
 * Created by melges on 17.12.14.
 */
public class VkSideError extends RemoteException {
    private int httpCode = 0;

    public VkSideError() {
    }

    public VkSideError(String s) {
        super(s);
    }

    public VkSideError(String s, int httpCode) {
        super(s);
        this.httpCode = httpCode;
    }

    public int getHttpCode() {
        return httpCode;
    }
}
