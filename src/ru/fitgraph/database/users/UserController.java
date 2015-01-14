package ru.fitgraph.database.users;

import javax.persistence.*;
import java.util.List;

/**
 * Created by melges on 16.12.14.
 */
public class UserController {
    private static EntityManagerFactory emf =
            Persistence.createEntityManagerFactory("FitGraphDataSource");

    public static User getUserByVkAndSession(Long vkId, String sessionSecret) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<User> query = em.createNamedQuery("User.getUserByVkIdAndSessionSecret", User.class);
            query.setParameter("vkId", vkId);
            query.setParameter("secret", sessionSecret);
            List<User> resultList = query.getResultList();
            if (resultList.size() == 1)
                return resultList.get(0);
            if (resultList.size() == 0)
                return null;

            throw new NonUniqueResultException("We are try to get user by vkId and session, but get more than 1 result. " +
                    "This can occur when the database is incorrectly configured");
        } finally {
            em.close();
        }
    }

    public static User getUserByVkId(Long vkId) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<User> query = em.createNamedQuery("User.getUserByVkId", User.class);
            query.setParameter("vkId", vkId);
            List<User> resultList = query.getResultList();
            if (resultList.size() == 1)
                return resultList.get(0);
            if (resultList.size() == 0)
                return null;

            throw new NonUniqueResultException("We are try to get user by vkId, but get more than 1 result. " +
                    "This can occur when the database is incorrectly configured");
        } finally {
            em.close();
        }
    }

    public static void persist(User user) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(user);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }
}
