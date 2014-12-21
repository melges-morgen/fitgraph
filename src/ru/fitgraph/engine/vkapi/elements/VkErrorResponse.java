package ru.fitgraph.engine.vkapi.elements;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by melges on 17.12.14.
 */
@XmlRootElement
public class VkErrorResponse {
    @XmlElement(name = "error")
    private String error;

    @XmlElement(name = "error_description")
    private String errorDescription;

    public VkErrorResponse() {
    }

    public VkErrorResponse(String error, String errorDescription) {
        this.error = error;
        this.errorDescription = errorDescription;
    }

    public String getError() {
        return error;
    }

    public String getErrorDescription() {
        return errorDescription;
    }
}
