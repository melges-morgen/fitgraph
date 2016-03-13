package ru.fitgraph.database.entities;

import ru.fitgraph.database.entities.User;

import javax.persistence.*;
import java.util.Date;

/**
 * Class for storing user sessions.
 *
 * @author Morgen Matvey
 */
@Entity
@Table(name = "users_sessions", indexes = {
})
@NamedQueries({@NamedQuery(name = "UserSession.findBySessionSecret", query = "select session from UserSession session " +
        "where session.sessionSecret = :secret")})
public class UserSession {
    /**
     * Internal session id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "session_id")
    private Long sessionId;

    /**
     * Filed contain the user for which this session is created.
     */
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, unique = false, updatable = false)
    private User owner;

    /**
     * Field contain client side session id (session secret)
     */
    @Column(name = "session_secret", nullable = false, unique = true, updatable = false)
    private String sessionSecret;

    /**
     * Date until session is valid.
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "expires_in", nullable = false)
    private Date expiresIn;

    /**
     * Create empty session without owner
     */
    public UserSession() {
        sessionSecret = null;
        expiresIn = null;
    }

    /**
     * Create user session and associate with specified user.
     * @param owner user with which session will be associated.
     * @param sessionSecret client side session id (session secret)
     * @param expiresIn time of live in milliseconds.
     */
    public UserSession(User owner, String sessionSecret, Long expiresIn) {
        if(expiresIn <= 0)
            expiresIn = 2629743L * 1000L;

        this.owner = owner;
        this.sessionSecret = sessionSecret;
        this.setExpiresIn(expiresIn);
        owner.addSession(this);
    }

    public Long getSessionId() {
        return sessionId;
    }

    public User getOwner() {
        return owner;
    }

    public String getSessionSecret() {
        return sessionSecret;
    }

    public Date getExpiresIn() {
        return expiresIn;
    }

    /**
     * Set the date when session will expires.
     * @param expiresIn Time from now in milliseconds, when the session expires.
     */
    public void setExpiresIn(Long expiresIn) {
        if(expiresIn <= 0)
            expiresIn = 2629743L * 1000L;
        this.expiresIn = new Date(System.currentTimeMillis() + expiresIn);
    }

    /**
     * Set the date when session will expire.
     * @param expiresIn date until the session is valid.
     */
    public void setExpiresIn(Date expiresIn) {
        this.expiresIn = expiresIn;
    }
}
