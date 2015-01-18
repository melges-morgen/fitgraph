package ru.fitgraph.database.users;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import ru.fitgraph.database.WeightPoint;
import ru.fitgraph.database.marshals.BirthDateJsonSerializer;

import javax.persistence.*;
import javax.xml.bind.annotation.*;
import java.util.*;

/**
 * Created by melges on 15.12.14.
 */

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
@Entity
@Table(name = "users", indexes = {
})
@NamedQueries({
        @NamedQuery(name = "User.getUserByName", query = "select user from User user where user.username = :username"),
        @NamedQuery(name = "User.getUserByVkId", query = "select user from User user where user.vkUserId = :vkId"),
        @NamedQuery(name = "User.getUserByEmail", query = "select user from User user where user.email = :email"),
        @NamedQuery(name = "User.getUserBySessionSecret", query = "select session.owner " +
                "from User user, UserSession session " +
                "where session.sessionSecret = :sessionSecret " +
                "and session.expiresIn > CURRENT_TIMESTAMP"),
        @NamedQuery(name = "User.getUserByVkIdAndSessionSecret", query = "select user " +
                "from User user, UserSession sessions where " +
                "user.vkUserId = :vkId and sessions.sessionSecret = :secret " +
                "and sessions.expiresIn > CURRENT_TIMESTAMP"),
        @NamedQuery(name = "User.getUserByVkId", query = "select user from User user where user.vkUserId = :vkId")
})
public class User {
    @XmlType(name = "sex")
    @XmlEnum
    public enum Sex {
        @XmlEnumValue("0") Undefined,
        @XmlEnumValue("1") Female,
        @XmlEnumValue("2") Male,
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId = null;

    @XmlElement
    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @Column(name = "vk_user_id", unique = true, nullable = false)
    private Long vkUserId;

    @Column(name = "access_token")
    private String accessToken;

    @XmlElement
    @Column(name = "user_email", unique = true, nullable = false)
    private String email;

    @XmlElement
    @Column(name = "sex")
    private Sex sex;

    @XmlElement
    @JsonSerialize(using = BirthDateJsonSerializer.class)
    @Column(name = "birth_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date birthDate;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    @MapKeyColumn(name = "session_secret", insertable = false, updatable = false)
    private Map<String, UserSession> sessions = new HashMap<String, UserSession>();

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    @MapKeyColumn(name = "p_id", insertable = false, updatable = false)
    Map<Long, WeightPoint> weightPoint = new HashMap<Long, WeightPoint>();

    public User() {
    }

    public User(String username) {
        this.username = username;
    }

    public User(String username, String email) {
        this.username = username;
        this.email = email;
    }

    public User(String username, String email, Sex sex, Date birthDate,
                Long vkUserId, String sessionSecret, String accessToken, Long expiresIn) {
        this.username = username;
        this.email = email;
        this.sex = sex;
        this.birthDate = birthDate;
        this.vkUserId = vkUserId;
        this.accessToken = accessToken;

        UserSession firstSession = new UserSession(this, sessionSecret, expiresIn);
        this.sessions.put(firstSession.getSessionSecret(), firstSession);
    }

    /**
     * Save new user session opened for user.
     * @param sessionSecret session id or other secret string used for identity user.
     * @param accessToken access token received from vk.
     * @param expiresIn time that the session is valid in milliseconds.
     */
    public void addSession(String sessionSecret, String accessToken, Long expiresIn) {
        this.accessToken = accessToken;
        if(sessions.containsKey(sessionSecret)) { // Already opened session, update it
            UserSession session = sessions.get(sessionSecret);
            session.setExpiresIn(expiresIn);
        } else { // New session, create it
            UserSession firstSession = new UserSession(this, sessionSecret, expiresIn);
            this.sessions.put(firstSession.getSessionSecret(), firstSession);
        }
    }

    public Long getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public Long getVkUserId() {
        return vkUserId;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getEmail() {
        return email;
    }

    public Map<String, UserSession> getSessions() {
        return sessions;
    }

    public Sex getSex() {
        return sex;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public Map<Long, WeightPoint> getWeightPoint() {
        return weightPoint;
    }
}
