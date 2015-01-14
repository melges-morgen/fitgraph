package ru.fitgraph.engine.vkapi.elements;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.*;

/**
 * Created by melges on 13.01.15.
 */
@XmlRootElement
public class VkUserInfo {
    @XmlType(name = "sex")
    @XmlEnum
    public enum Sex {
        @XmlEnumValue("0") Undefined,
        @XmlEnumValue("1") Female,
        @XmlEnumValue("2") Male,
    }

    @NotNull
    @XmlElement(name = "id", required = true)
    private Long id;

    @NotNull
    @XmlElement(name = "first_name")
    private String firstName;

    @NotNull
    @XmlElement(name = "last_name")
    private String lastName;

    @NotNull
    @XmlElement(name = "sex")
    private Sex sex;

    @XmlElement(name = "bdate")
    private String birthDate;

    @NotNull
    @XmlElement(name = "photo_max", required = true)
    private String photoUri;

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Sex getSex() {
        return sex;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public String getPhotoUri() {
        return photoUri;
    }

    public String getFullName() {
        return lastName + firstName;
    }
}
