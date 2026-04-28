package lk.ijse.serenitymentalhealththerepycentersystem.dao.custom.impl;

import lk.ijse.serenitymentalhealththerepycentersystem.config.FactoryConfiguration;
import lk.ijse.serenitymentalhealththerepycentersystem.dao.custom.TherapistDAO;
import lk.ijse.serenitymentalhealththerepycentersystem.entity.Therapist;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Optional;

public class TherapistDAOImpl implements TherapistDAO {
    @Override
    public boolean save(Therapist entity) {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.persist(entity);
            transaction.commit();
            return true;
        }catch (Exception e) {
            transaction.rollback();
            return false;
        }finally {
            session.close();
        }
    }

    @Override
    public boolean update(Therapist entity) {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.merge(entity);
            transaction.commit();
            return true;
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
            return false;
        } finally {
            session.close();
        }
    }

    @Override
    public boolean delete(String id) {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();
        try {
            Therapist therapist = session.find(Therapist.class, id);
            if (therapist!= null) {
                session.remove(therapist);
                transaction.commit();
                return true;
            }
            return false;
        }catch (Exception e) {
            transaction.rollback();
            return false;
        }finally {
            session.close();
        }
    }

    @Override
    public Therapist search(String id) {
        Session session = FactoryConfiguration.getInstance().getSession();
        try {
            return session.get(Therapist.class, id);
        } finally {
            session.close();
        }
    }

    @Override
    public List<Therapist> getAll() {
        Session session = FactoryConfiguration.getInstance().getSession();
        List<Therapist> therapists = session.createQuery("FROM Therapist", Therapist.class).list();
        session.close();
        return therapists;
    }

    @Override
    public List<Therapist> findByTherapistName(String name) {
        Session session = FactoryConfiguration.getInstance().getSession();
        List<Therapist> therapists = session.createQuery("FROM Therapist th WHERE th.name LIKE :name", Therapist.class)
                .setParameter("name", "%" + name + "%")
                .list();
        session.close();

        return therapists;
    }

    @Override
    public Optional<String> getLastPK() {
        Session session = FactoryConfiguration.getInstance().getSession();
        String lastPk = session.createQuery("SELECT th.therapist_id FROM Therapist th ORDER BY th.therapist_id DESC", String.class)
                .setMaxResults(1)
                .uniqueResult();
        session.close();

        return Optional.ofNullable(lastPk);
    }

}
