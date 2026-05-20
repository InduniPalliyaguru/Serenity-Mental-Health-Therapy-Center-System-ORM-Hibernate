package lk.ijse.serenitymentalhealththerepycentersystem.dao.custom.impl;

import lk.ijse.serenitymentalhealththerepycentersystem.config.FactoryConfiguration;
import lk.ijse.serenitymentalhealththerepycentersystem.dao.custom.TherapistProgramDAO;
import lk.ijse.serenitymentalhealththerepycentersystem.entity.TherapistProgram;
import lk.ijse.serenitymentalhealththerepycentersystem.entity.TherapistProgramId;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Optional;

public class TherapistProgramDAOImpl implements TherapistProgramDAO {

    @Override
    public boolean delete(String therapistId, String programId) {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();
        try {
            TherapistProgramId id = new TherapistProgramId(therapistId, programId);
            TherapistProgram therapistProgram = session.find(TherapistProgram.class, id);
            if (therapistProgram != null) {
                session.remove(therapistProgram);
                transaction.commit();
                return true;
            }
            return false;
        } catch (Exception e) {
            transaction.rollback();
            System.out.println(e.getMessage());
            return false;
        } finally {
            session.close();
        }
    }

    @Override
    public List<TherapistProgram> findByProgramName(String name) {
        Session session = FactoryConfiguration.getInstance().getSession();
        List<TherapistProgram> programs = session.createQuery(
                        "FROM TherapistProgram tp WHERE tp.therapy_program.programName LIKE :name", TherapistProgram.class)
                .setParameter("name", "%" + name + "%")
                .list();
        session.close();
        return programs;
    }

    @Override
    public List<TherapistProgram> findByTherapist(String id) {
        Session session = FactoryConfiguration.getInstance().getSession();
        List<TherapistProgram> programs = session.createQuery(
                        "FROM TherapistProgram tp WHERE tp.therapist.therapist_id LIKE :therapist_id", TherapistProgram.class)
                .setParameter("therapist_id", "%" + id + "%")
                .list();
        session.close();
        return programs;
    }

    @Override
    public Optional<TherapistProgram> findById(String therapistId, String programId) {
        Session session = FactoryConfiguration.getInstance().getSession();
        TherapistProgram program = null;

        try {
            TherapistProgramId id = new TherapistProgramId(therapistId, programId);
            program = session.get(TherapistProgram.class, id);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            session.close();
        }

        return Optional.ofNullable(program);
    }

    @Override
    public List<TherapistProgram> findByTherapistId(String therapistId) {
        Session session = FactoryConfiguration.getInstance().getSession();
        List<TherapistProgram> results = null;

        try {
            results = session.createQuery(
                            "FROM TherapistProgram tp WHERE tp.therapist.therapist_id = :therapistId", TherapistProgram.class)
                    .setParameter("therapistId", therapistId)
                    .getResultList();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            session.close();
        }

        return results;
    }

    @Override
    public List<TherapistProgram> findByProgramId(String programId) {
        Session session = FactoryConfiguration.getInstance().getSession();
        List<TherapistProgram> results = null;

        try {
            results = session.createQuery(
                            "FROM TherapistProgram tp WHERE tp.therapy_program.programId = :programId", TherapistProgram.class)
                    .setParameter("programId", programId)
                    .getResultList();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            session.close();
        }
        return results;
    }

    @Override
    public boolean save(TherapistProgram entity) {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.persist(entity);
            transaction.commit();
            return true;
        } catch (Exception e) {
            transaction.rollback();
            System.out.println(e.getMessage());
            return false;
        } finally {
            session.close();
        }
    }

    @Override
    public boolean update(TherapistProgram entity) {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.merge(entity);
            transaction.commit();
            return true;
        } catch (Exception e) {
            transaction.rollback();
            System.out.println(e.getMessage());
            return false;
        } finally {
            session.close();
        }
    }

    @Override
    public boolean delete(String id) {
        return false;
    }

    @Override
    public TherapistProgram search(String id) {
        return null;
    }

    @Override
    public List<TherapistProgram> getAll() {
        Session session = FactoryConfiguration.getInstance().getSession();
        List<TherapistProgram> programs = session.createQuery("FROM TherapistProgram", TherapistProgram.class).list();
        session.close();
        return programs;
    }

}
