package ru.fitgraph.database.users;

import javax.persistence.EntityManager;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Created by melges on 16.12.14.
 */
public class UserController {
    private static EntityManager em =
            Persistence.createEntityManagerFactory("FitGraphDataSource").createEntityManager();

    public static User getUserByVkAndSession(Long vkId, String sessionSecret) {
        TypedQuery<User> query = em.createNamedQuery("User.getUserByVkIdAndSessionSecret", User.class);
        query.setParameter("vkId", vkId);
        query.setParameter("secret", sessionSecret);
        List<User> resultList = query.getResultList();
        if(resultList.size() == 1)
            return resultList.get(0);
        if(resultList.size() == 0)
            return null;

        throw new NonUniqueResultException("We are try to get user by vkId and session, but get more than 1 result. " +
                "This can occur when the database is incorrectly configured");
    }
}
