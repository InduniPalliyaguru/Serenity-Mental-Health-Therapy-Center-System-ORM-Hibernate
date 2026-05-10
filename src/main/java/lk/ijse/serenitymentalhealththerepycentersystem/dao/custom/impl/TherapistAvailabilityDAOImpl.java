package lk.ijse.serenitymentalhealththerepycentersystem.dao.custom.impl;

import lk.ijse.serenitymentalhealththerepycentersystem.config.FactoryConfiguration;
import lk.ijse.serenitymentalhealththerepycentersystem.dao.custom.TherapistAvailabilityDAO;
import lk.ijse.serenitymentalhealththerepycentersystem.entity.TherapistAvailability;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class TherapistAvailabilityDAOImpl implements TherapistAvailabilityDAO {

    @Override
    public boolean save(TherapistAvailability entity) {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction tx = session.beginTransaction();
        try {
            session.persist(entity);
            tx.commit();
            return true;
        } catch (Exception e) {
            tx.rollback();
            return false;
        } finally {
            session.close();
        }
    }

    @Override
    public boolean update(TherapistAvailability entity) {
        return false;
    }

    @Override
    public boolean update(TherapistAvailability entity, Session session) {
        try {
            session.merge(entity);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(String id) {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction tx = session.beginTransaction();
        try {
            TherapistAvailability availability = session.get(TherapistAvailability.class, id);
            if (availability != null) {
                session.remove(availability);
                tx.commit();
                return true;
            }
            return false;
        } catch (Exception e) {
            tx.rollback();
            return false;
        } finally {
            session.close();
        }
    }

    @Override
    public TherapistAvailability search(String id) {
        Session session = FactoryConfiguration.getInstance().getSession();
        try {
            return session.get(TherapistAvailability.class, id);
        } finally {
            session.close();
        }
    }

    @Override
    public List<TherapistAvailability> getAll() {
        Session session = FactoryConfiguration.getInstance().getSession();
        try {
            return session.createQuery("FROM TherapistAvailability", TherapistAvailability.class).list();
        } finally {
            session.close();
        }
    }

    @Override
    public Optional<String> getLastPK() {
        Session session = FactoryConfiguration.getInstance().getSession();
        try {
            String lastId = session.createQuery("SELECT a.availability_id FROM TherapistAvailability a ORDER BY a.availability_id DESC", String.class)
                    .setMaxResults(1).uniqueResult();
            return Optional.ofNullable(lastId);
        } finally {
            session.close();
        }
    }

    @Override
    public List<TherapistAvailability> findByTherapistName(String name) {
        Session session = FactoryConfiguration.getInstance().getSession();
        try {
            String hql = "SELECT a FROM TherapistAvailability a JOIN a.therapist t WHERE t.name LIKE :name";
            return session.createQuery(hql, TherapistAvailability.class)
                    .setParameter("name", "%" + name + "%")
                    .list();
        } finally {
            session.close();
        }
    }

    @Override
    public List<TherapistAvailability> getAvailableSlots(String therapistId, LocalDate date) throws Exception {
        Session session = FactoryConfiguration.getInstance().getSession();
        try {
            String hql = "FROM TherapistAvailability t WHERE t.therapist.therapist_id = :tId " +
                    "AND t.available_date = :selDate AND t.is_available = true ";

            return session.createQuery(hql, TherapistAvailability.class)
                    .setParameter("tId", therapistId)
                    .setParameter("selDate", date)
                    .list();
        } finally {
            session.close();
        }
    }

    @Override
    public boolean updateStatus(String id, String status) throws Exception {
        return false;
    }

    @Override
    public List<TherapistAvailability> findByTherapistAndDate(String therapistId, LocalDate date) {
        Session session = FactoryConfiguration.getInstance().getSession();
        List<TherapistAvailability> availabilities = session.createQuery(
                        "FROM TherapistAvailability ta WHERE ta.therapist.therapist_id = :id AND ta.available_date = :date AND ta.is_available = true",
                        TherapistAvailability.class)
                .setParameter("id", therapistId)
                .setParameter("date", date)
                .getResultList();
        session.close();
        return availabilities;
    }

}
