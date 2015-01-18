package ru.fitgraph.engine.vkapi.elements;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import ru.fitgraph.database.users.User;
import ru.fitgraph.engine.vkapi.marshals.VkDateDeserializer;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

/**
 * Created by melges on 13.01.15.
 */
@XmlRootElement
public class VkUserInfo {
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
    private User.Sex sex;

    @XmlElement(name = "bdate")
    @JsonDeserialize(using = VkDateDeserializer.class)
    private Date birthDate;

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

    public User.Sex getSex() {
        return sex;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public String getPhotoUri() {
        return photoUri;
    }

    public String getFullName() {
        return String.format("%s %s", lastName, firstName);
    }
}
