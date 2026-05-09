package lk.ijse.serenitymentalhealththerepycentersystem.dao.custom.impl;

import lk.ijse.serenitymentalhealththerepycentersystem.config.FactoryConfiguration;
import lk.ijse.serenitymentalhealththerepycentersystem.dao.custom.TherapyProgramDAO;
import lk.ijse.serenitymentalhealththerepycentersystem.entity.TherapyProgram;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Optional;

public class TherapyProgramDAOImpl implements TherapyProgramDAO {

    @Override
    public boolean save(TherapyProgram entity) {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.persist(entity);
            transaction.commit();
            return true;
        } catch (Exception e) {
            transaction.rollback();
            return false;
        } finally {
            session.close();
        }
    }

    @Override
    public boolean update(TherapyProgram entity) {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.merge(entity);
            transaction.commit();
            return true;
        } catch (Exception e) {
            transaction.rollback();
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
            TherapyProgram program = session.get(TherapyProgram.class, id);
            if (program != null) {
                session.remove(program);
                transaction.commit();
                return true;
            }
            return false;
        } catch (Exception e) {
            transaction.rollback();
            return false;
        } finally {
            session.close();
        }
    }

    @Override
    public TherapyProgram search(String id) {
        Session session = FactoryConfiguration.getInstance().getSession();
        try {
            return session.get(TherapyProgram.class, id);
        } finally {
            session.close();
        }
    }

    @Override
    public List<TherapyProgram> getAll() {
        Session session = FactoryConfiguration.getInstance().getSession();
        try {
            return session.createQuery("FROM TherapyProgram", TherapyProgram.class).list();
        } finally {
            session.close();
        }
    }

    @Override
    public Optional<String> getLastPK() {
        Session session = FactoryConfiguration.getInstance().getSession();
        String lastPk = session.createQuery("SELECT tp.programId FROM TherapyProgram tp ORDER BY tp.programId DESC", String.class)
                .setMaxResults(1)
                .uniqueResult();
        session.close();

        return Optional.ofNullable(lastPk);
    }

    @Override
    public TherapyProgram findByName(String name) throws Exception {
        Session session = FactoryConfiguration.getInstance().getSession();
        try {
            String hql = "FROM TherapyProgram p WHERE p.programName = :pName";
            return session.createQuery(hql, TherapyProgram.class)
                    .setParameter("pName", name)
                    .uniqueResult();
        } finally {
            session.close();
        }
    }

    @Override
    public List<TherapyProgram> findByTherapyProgramName(String name) {
        Session session = FactoryConfiguration.getInstance().getSession();
        List<TherapyProgram> programs = session.createQuery("FROM TherapyProgram tp WHERE tp.programName LIKE :name", TherapyProgram.class)
                .setParameter("name", "%" + name + "%")
                .list();
        session.close();

        return programs;
    }
}
