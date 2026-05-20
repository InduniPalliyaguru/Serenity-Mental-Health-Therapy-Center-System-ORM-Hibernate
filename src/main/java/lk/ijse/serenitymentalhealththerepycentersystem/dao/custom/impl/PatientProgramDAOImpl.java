package lk.ijse.serenitymentalhealththerepycentersystem.dao.custom.impl;

import lk.ijse.serenitymentalhealththerepycentersystem.config.FactoryConfiguration;
import lk.ijse.serenitymentalhealththerepycentersystem.dao.custom.PatientProgramDAO;
import lk.ijse.serenitymentalhealththerepycentersystem.entity.PatientProgram;
import lk.ijse.serenitymentalhealththerepycentersystem.entity.PatientProgramID;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class PatientProgramDAOImpl implements PatientProgramDAO {

    @Override
    public boolean delete(String patientId, String programId) {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();

        try {
            PatientProgramID patientProgramId = new PatientProgramID(patientId, programId);
            PatientProgram patientProgram = session.find(PatientProgram.class, patientProgramId);

            if (patientProgram != null) {
                session.remove(patientProgram);
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
    public List<PatientProgram> findByPatientId(String id) {
        Session session = FactoryConfiguration.getInstance().getSession();
        List<PatientProgram> patientPrograms = session.createQuery("FROM PatientProgram pp WHERE pp.patient.id = :patient_id", PatientProgram.class)
                .setParameter("patient_id", id)
                .list();

        session.close();
        return patientPrograms;
    }

    @Override
    public List<PatientProgram> findByProgramId(String id) {
        Session session = FactoryConfiguration.getInstance().getSession();
        List<PatientProgram> patientPrograms = session.createQuery("FROM PatientProgram pp WHERE pp.therapy_program.id = :program_id", PatientProgram.class)
                .setParameter("program_id", id)
                .list();

        session.close();
        return patientPrograms;
    }

    @Override
    public Optional<PatientProgram> findById(String patientId, String programId) {
        Session session = FactoryConfiguration.getInstance().getSession();
        try {
            PatientProgramID id = new PatientProgramID(patientId, programId);
            PatientProgram patientProgram = session.find(PatientProgram.class, id);
            return Optional.ofNullable(patientProgram);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return Optional.empty();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public boolean updateTherapyProgramFee(String patientId, String programId, BigDecimal newFee) {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();
        try {
            PatientProgramID id = new PatientProgramID(patientId, programId);
            PatientProgram patientProgram = session.find(PatientProgram.class, id);

            if (patientProgram != null) {
                patientProgram.setProgram_fee(newFee);
                session.merge(patientProgram);
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
    public boolean save(PatientProgram entity) {
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
    public boolean update(PatientProgram entity) {
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
    public PatientProgram search(String id) {
        return null;
    }

    @Override
    public List<PatientProgram> getAll() {
        Session session = FactoryConfiguration.getInstance().getSession();
        List<PatientProgram> patientProgram = session.createQuery("FROM PatientProgram", PatientProgram.class).list();
        session.close();
        return patientProgram;
    }

}
