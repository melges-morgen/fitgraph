package ru.fitgraph.database.users;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import ru.fitgraph.database.weight.WeightPoint;
import ru.fitgraph.database.marshals.BirthDateJsonSerializer;

import javax.persistence.*;
import javax.xml.bind.annotation.*;
import java.util.*;

/**
 * Entity class for storing users.
 *
 * @author Morgen Matvey
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
    /**
     * Enum type which represent available state of sex field.
     */
    @XmlType(name = "sex")
    @XmlEnum
    public enum Sex {
        @XmlEnumValue("0") Undefined,
        @XmlEnumValue("1") Female,
        @XmlEnumValue("2") Male,
    }

    /**
     * Unique id field
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId = null;

    /**
     * Full name of user
     */
    @XmlElement
    @Column(name = "username", unique = true, nullable = false)
    private String username;

    /**
     * Users vk id of
     */
    @Column(name = "vk_user_id", unique = true, nullable = false)
    private Long vkUserId;

    /**
     * Vk access token, associated with user
     */
    @Column(name = "access_token")
    private String accessToken;

    /**
     * Users email
     */
    @XmlElement
    @Column(name = "user_email", unique = true, nullable = false)
    private String email;

    /**
     * Is user male, female or other?
     */
    @XmlElement
    @Column(name = "sex")
    private Sex sex;

    /**
     * Birth date, stored day, month and year always midnight in servers time zone as unix timestamp.
     * Serialized to json as dd.MM.yyyy {@link java.util.SimpleTimeZone}
     */
    @XmlElement
    @JsonSerialize(using = BirthDateJsonSerializer.class)
    @Column(name = "birth_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date birthDate;

    /**
     * Virtual field, list which contain all success auth sessions opened by user.
     * Field is virtual, link to user is made by column in {@link ru.fitgraph.database.users.UserSession} class.
     */
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    @MapKeyColumn(name = "session_secret", insertable = false, updatable = false)
    private Map<String, UserSession> sessions = new HashMap<String, UserSession>();

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    @MapKeyColumn(name = "p_id", insertable = false, updatable = false)
    List<WeightPoint> weightPoint = new ArrayList<WeightPoint>();

    /**
     * Default constructor
     */
    public User() {
    }

    /**
     * Create user with specified username
     * @param username users full name.
     */
    public User(String username) {
        this.username = username;
    }

    /**
     * Create user with specified username and email.
     * @param username users full name.
     * @param email users email.
     */
    public User(String username, String email) {
        this.username = username;
        this.email = email;
    }

    /**
     * Create user with full profile, and add first session for him. This constructor should be used
     * when we user register himself.
     * @param username users full name.
     * @param email users email.
     * @param sex users sex.
     * @param birthDate users date of birth.
     * @param vkUserId user id in vk service.
     * @param sessionSecret session id which user provide.
     * @param accessToken access token returned from vk service.
     * @param expiresIn time of vk sessions life in milliseconds.
     */
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
     * Save new user session opened for user. Allow auth user with provided data.
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

    /**
     * Create new weight point and associate it with user.
     * @param date date when weight was seen.
     * @param weight weight in kilograms.
     */
    public void addWeightPoint(Date date, Double weight) {
        WeightPoint newWeightPoint = new WeightPoint(date, weight, this);
        weightPoint.add(newWeightPoint);
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

    public List<WeightPoint> getWeightPoints() {
        return weightPoint;
    }
}
