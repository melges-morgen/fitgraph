package ru.fitgraph.database.users;

import javax.persistence.*;
import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

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
                "user.vkUserId = :vkId and sessions.sessionSecret = :secret and sessions.owner = user " +
                "and sessions.expiresIn > CURRENT_TIMESTAMP"),
        @NamedQuery(name = "User.getUserByVkId", query = "select user from User user where user.vkUserId = :vkId")
})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

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

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    private List<UserSession> sessions;

    public User() {
        sessions = new ArrayList<UserSession>();
    }

    public User(String username) {
        this.username = username;
        sessions = new ArrayList<UserSession>();
    }

    public User(String username, String email) {
        this.username = username;
        this.email = email;
        sessions = new ArrayList<UserSession>();
    }

    public User(String username, String email, Long vkUserId, String sessionSecret, String accessToken,
                Long expiresIn) {
        this.username = username;
        this.email = email;
        this.vkUserId = vkUserId;
        this.accessToken = accessToken;
        this.sessions = new ArrayList<UserSession>();

        UserSession firstSession = new UserSession(this, sessionSecret, expiresIn);
        this.sessions.add(firstSession);
    }

    public void addSession(String sessionSecret, String accessToken, Long expiresIn) {
        this.accessToken = accessToken;

        UserSession firstSession = new UserSession(this, sessionSecret, expiresIn);
        this.sessions.add(firstSession);
    }
}
