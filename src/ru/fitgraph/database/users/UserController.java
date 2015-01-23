package ru.fitgraph.database.users;

import javax.persistence.*;
import java.util.List;

/**
 * Class for work with user objects.
 */
public class UserController {
    /**
     * Entity manager factory associated with our data source
     */
    private static EntityManagerFactory emf =
            Persistence.createEntityManagerFactory("FitGraphDataSource");

    /**
     * Find user with specified vk id and session id. Useful for auth methods.
     * @param vkId vk id which user should have.
     * @param sessionSecret client session id (sessionSecret) which user should have.
     * @return user with specified data or null.
     *
     * @throws javax.persistence.NonUniqueResultException in case when we find more than one users with specified
     * criteria.
     */
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

    /**
     * Find user with specified vk id. Useful for auth methods.
     * @param vkId vk id which user should have.
     * @return user with specified data or null.
     *
     * @throws javax.persistence.NonUniqueResultException in case when we find more than one users with specified
     * criteria.
     */
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

    /**
     * Return user session with specified session secret.
     * @param secret sessionSecret which found session must have.
     * @return session with specified secret or null.
     *
     * @throws javax.persistence.NonUniqueResultException in case when we find more than one users with specified
     * criteria.
     */
    public UserSession getSessionBySecret(String secret) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<UserSession> findSessionQuery =
                    em.createNamedQuery("UserSession.findBySessionSecret", UserSession.class);
            findSessionQuery.setParameter("secret", secret);
            List<UserSession> resultList = findSessionQuery.getResultList();
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

    /**
     * Save user to database and save all linked objects if needed. If user with specified data already exist throw
     * error.
     * @param user user which must be saved.
     */
    public static void save(User user) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(user);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    /**
     * Similar to save method, but if user exist update them.
     * @param user user which must be saved or merged.
     */
    public static void saveOrUpdate(User user) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            if(user.getUserId() == null)
                em.persist(user);
            else
                em.merge(user);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }
}
