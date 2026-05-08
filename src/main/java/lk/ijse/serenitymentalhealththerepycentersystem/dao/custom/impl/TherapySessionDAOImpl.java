package lk.ijse.serenitymentalhealththerepycentersystem.dao.custom.impl;

import lk.ijse.serenitymentalhealththerepycentersystem.config.FactoryConfiguration;
import lk.ijse.serenitymentalhealththerepycentersystem.dao.custom.TherapySessionDAO;
import lk.ijse.serenitymentalhealththerepycentersystem.entity.TherapySession;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Optional;

public class TherapySessionDAOImpl implements TherapySessionDAO {
    @Override
    public List<TherapySession> searchByPatientName(String name) throws Exception {
        Session session = FactoryConfiguration.getInstance().getSession();
        try {
            String hql = "SELECT s FROM TherapySession s JOIN s.patient p WHERE p.name LIKE :name";
            return session.createQuery(hql, TherapySession.class)
                    .setParameter("name", "%" + name + "%")
                    .list();
        } finally {
            session.close();
        }
    }

    @Override
    public Optional<String> getLastPK() {
        Session session = FactoryConfiguration.getInstance().getSession();
        String lastPk = session.createQuery("SELECT s.session_id FROM TherapySession s ORDER BY s.session_id DESC", String.class)
                .setMaxResults(1)
                .uniqueResult();
        session.close();
        return Optional.ofNullable(lastPk);
    }

    @Override
    public boolean save(TherapySession entity) {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.save(entity);
            transaction.commit();
            return true;
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    @Override
    public boolean update(TherapySession entity) {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.update(entity);
            transaction.commit();
            return true;
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    @Override
    public boolean delete(String id) {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();
        try {
            TherapySession sessionEntity = session.get(TherapySession.class, id);
            if (sessionEntity != null) {
                session.delete(sessionEntity);
                transaction.commit();
                return true;
            }
            return false;
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    @Override
    public TherapySession search(String id) {
        Session session = FactoryConfiguration.getInstance().getSession();
        try {
            return session.get(TherapySession.class, id);
        } finally {
            session.close();
        }
    }

    @Override
    public List<TherapySession> getAll() {
        Session session = FactoryConfiguration.getInstance().getSession();
        try {
            return session.createQuery("FROM TherapySession", TherapySession.class).list();
        } finally {
            session.close();
        }
    }
}
