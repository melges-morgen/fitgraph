package ru.fitgraph.database.users;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by melges on 16.12.14.
 */
@Entity
@Table(name = "users_sessions", indexes = {
})
public class UserSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "session_id")
    private Long sessionId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true, updatable = false)
    private User owner;

    @Column(name = "session_secret", nullable = false, unique = true, updatable = false)
    private String sessionSecret;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "expires_in", nullable = false)
    private Date expiresIn;

    public UserSession() {
        sessionSecret = null;
        expiresIn = null;
    }

    public UserSession(User owner, String sessionSecret, Long expiresIn) {
        this.owner = owner;
        this.sessionSecret = sessionSecret;
        this.expiresIn = new Date(System.currentTimeMillis() + expiresIn);
    }
}
