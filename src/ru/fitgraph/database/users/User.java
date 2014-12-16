package ru.fitgraph.database.users;

import javax.persistence.*;
import java.util.List;

/**
 * Created by melges on 15.12.14.
 */
@Entity
@Table(name = "users", indexes = {
})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @Column(name = "vk_user_id", unique = true, nullable = false)
    private Long vkUserId;

    @Column(name = "access_token")
    private String accessToken;

    @Column(name = "user_email", unique = true, nullable = false)
    private String email;

    @OneToMany(mappedBy = "owner")
    private List<UserSession> sessions;

    public User() {
        sessions = null;
    }

    public User(String username) {
        this.username = username;
    }
}
