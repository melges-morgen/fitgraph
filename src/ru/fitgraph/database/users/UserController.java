package ru.fitgraph.database.users;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

/**
 * Created by melges on 16.12.14.
 */
public class UserController {
    private EntityManager em = Persistence.createEntityManagerFactory("FitGraphDataSource").createEntityManager();
}
