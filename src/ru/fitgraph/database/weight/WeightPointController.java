package ru.fitgraph.database.weight;

import ru.fitgraph.database.users.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import java.util.Date;
import java.util.List;

/**
 * Created by melges on 19.01.15.
 */
public class WeightPointController {
    /**
     * Entity manager factory associated with our data source.
     */
    private static EntityManagerFactory emf =
            Persistence.createEntityManagerFactory("FitGraphDataSource");

    /**
     * Find weight points registered in specified period and owned by specified user.
     * @param owner user, which points must have as owner.
     * @param startDate start moment of period.
     * @param endDate end moment of period.
     * @return list of found points.
     */
    public static List<WeightPoint> getUserWeightPointsBetween(User owner, Date startDate, Date endDate) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<WeightPoint> query = em.createNamedQuery("WeightPoint.getUserPointBetween", WeightPoint.class);
            query.setParameter("owner", owner);
            query.setParameter("startDate", startDate);
            query.setParameter("endDate", endDate);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * Find weight points registered in specified period and owned by specified user which specified by vk id.
     * @param vkId users vk id, which points must have as owner.
     * @param startDate start moment of period.
     * @param endDate end moment of period.
     * @return list of found points.
     */
    public static List<WeightPoint> getVkUserWeightPointsBetween(Long vkId, Date startDate, Date endDate) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<WeightPoint> query = em.createNamedQuery("WeightPoint.getVkUserPointBetween", WeightPoint.class);
            query.setParameter("ownerVkId", vkId);
            query.setParameter("startDate", startDate);
            query.setParameter("endDate", endDate);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * Save point to database, or update if point with same id exist.
     * @param point object which should be stored.
     */
    public static void saveOrUpdate(WeightPoint point) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            if(point.getId() == null)
                em.persist(point);
            else
                em.merge(point);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }
}
