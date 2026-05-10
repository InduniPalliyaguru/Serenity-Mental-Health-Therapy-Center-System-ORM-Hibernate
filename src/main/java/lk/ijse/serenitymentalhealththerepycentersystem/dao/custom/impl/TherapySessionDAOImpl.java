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
    public Optional<TherapySession> findBySessionId(String sessionId) {
        Session session = FactoryConfiguration.getInstance().getSession();
        TherapySession sessionEntity = null;
        try {
            sessionEntity = session.get(TherapySession.class, sessionId);
        } finally {
            session.close();
        }
        return Optional.ofNullable(sessionEntity);
    }

    @Override
    public List<TherapySession> findByPatientId(String patientId) {
        Session session = FactoryConfiguration.getInstance().getSession();
        List<TherapySession> sessions = session.createQuery(
                        "FROM TherapySession ts WHERE ts.patient.patient_id = :id", TherapySession.class)
                .setParameter("id", patientId)
                .list();
        session.close();
        return sessions;
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
        return false;
    }

    @Override
    public boolean save(TherapySession entity, Session session) {
        try {
            session.persist(entity);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean update(TherapySession entity) {
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
            TherapySession therapySession = session.get(TherapySession.class, id);
            if (therapySession != null) {
                session.remove(therapySession);
                transaction.commit();
                return true;
            }
            return false;
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
            return false;
        } finally {
            session.close();
        }
    }

    @Override
    public TherapySession search(String id) {
        return null;
    }

    @Override
    public List<TherapySession> getAll() {
        Session session = FactoryConfiguration.getInstance().getSession();
        List<TherapySession> sessions = session.createQuery("FROM TherapySession", TherapySession.class).list();
        session.close();
        return sessions;
    }

}
