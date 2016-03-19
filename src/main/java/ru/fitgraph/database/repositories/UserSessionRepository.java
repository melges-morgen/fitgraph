package ru.fitgraph.database.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.fitgraph.database.entities.UserSession;

/**
 * Created by melge on 13.03.2016.
 */
public interface UserSessionRepository extends JpaRepository<UserSession, Long> {
}
