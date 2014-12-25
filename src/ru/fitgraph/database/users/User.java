package ru.fitgraph.database.users;

import javax.persistence.*;
import javax.xml.bind.annotation.*;
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
                "and sessions.expiresIn > CURRENT_TIMESTAMP")
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

    @OneToMany(mappedBy = "owner")
    private List<UserSession> sessions;

    public User() {
        sessions = null;
    }

    public User(String username) {
        this.username = username;
        sessions = null;
    }

    public User(String username, String email) {
        this.username = username;
        this.email = email;
        sessions = null;
    }

    public User(String username, String email, Long vkUserId, String sessionSecret, Long expiresIn) {
        this.username = username;
        this.email = email;
        this.vkUserId = vkUserId;
        UserSession firstSession = new UserSession(sessionSecret, expiresIn);
        this.sessions.add(firstSession);
    }
}
