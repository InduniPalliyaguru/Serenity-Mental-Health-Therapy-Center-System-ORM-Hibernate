package lk.ijse.serenitymentalhealththerepycentersystem.dao.custom.impl;

import lk.ijse.serenitymentalhealththerepycentersystem.config.FactoryConfiguration;
import lk.ijse.serenitymentalhealththerepycentersystem.dao.custom.PatientDAO;
import lk.ijse.serenitymentalhealththerepycentersystem.entity.Patient;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Optional;

public class PatientDAOImpl implements PatientDAO {
    @Override
    public boolean save(Patient entity) {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.persist(entity);
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
    public boolean update(Patient entity) {
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
            Patient patient = session.find(Patient.class, id);
            if (patient!= null) {
                session.remove(patient);
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
    public Patient search(String id) {
        Session session = FactoryConfiguration.getInstance().getSession();
        try {
            return session.get(Patient.class, id);
        } finally {
            session.close();
        }
    }

    @Override
    public List<Patient> getAll() {
        Session session = FactoryConfiguration.getInstance().getSession();
        List<Patient> patient = session.createQuery("FROM Patient", Patient.class).list();
        session.close();
        return patient;
    }

    @Override
    public Optional<String> getLastPK() {
        Session session = FactoryConfiguration.getInstance().getSession();
        String lastPk = session.createQuery("SELECT p.patient_id FROM Patient p ORDER BY p.patient_id DESC", String.class)
                .setMaxResults(1)
                .uniqueResult();
        session.close();

        return Optional.ofNullable(lastPk);
    }

    @Override
    public List<Patient> findByPatientName(String name) {
        Session session = FactoryConfiguration.getInstance().getSession();
        List<Patient> patients = session.createQuery("FROM Patient p WHERE p.name LIKE :name", Patient.class)
                .setParameter("name", "%" + name + "%")
                .list();
        session.close();

        return patients;
    }

    @Override
    public List<Patient> searchBySessionStatus(String status) {
        return List.of();
    }

//    @Override
//    public List<Patient> searchBySessionStatus(String status) {
//        Session session = FactoryConfiguration.getInstance().getSession();
//        try {
//            String hql = "SELECT DISTINCT p FROM Patient p JOIN p.therapySessions s WHERE s.status = :status";
//            return session.createQuery(hql, Patient.class)
//                    .setParameter("status", status)
//                    .list();
//        } finally {
//            session.close();
//        }
//    }

}
