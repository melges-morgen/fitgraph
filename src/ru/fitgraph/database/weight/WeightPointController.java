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
    private static EntityManagerFactory emf =
            Persistence.createEntityManagerFactory("FitGraphDataSource");

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
