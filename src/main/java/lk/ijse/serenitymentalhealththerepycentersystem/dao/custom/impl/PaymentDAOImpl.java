package lk.ijse.serenitymentalhealththerepycentersystem.dao.custom.impl;

import lk.ijse.serenitymentalhealththerepycentersystem.config.FactoryConfiguration;
import lk.ijse.serenitymentalhealththerepycentersystem.dao.custom.PaymentDAO;
import lk.ijse.serenitymentalhealththerepycentersystem.entity.Payment;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class PaymentDAOImpl implements PaymentDAO {
    @Override
    public List<Payment> findByPatientName(String name) {
        Session session = FactoryConfiguration.getInstance().getSession();
        List<Payment> payments = session.createQuery(
                        "FROM Payment p WHERE p.patient.name LIKE :name", Payment.class)
                .setParameter("name", "%" + name + "%")
                .list();
        session.close();
        return payments;
    }

    @Override
    public Optional<Payment> findById(String pk) {
        Session session = FactoryConfiguration.getInstance().getSession();
        Payment payment = session.find(Payment.class, pk);
        session.close();
        return Optional.ofNullable(payment);
    }

    @Override
    public List<Payment> findByDate(LocalDate date) {
        Session session = FactoryConfiguration.getInstance().getSession();
        List<Payment> payments = session.createQuery(
                        "FROM Payment p WHERE DATE(p.payment_date) = :date", Payment.class)
                .setParameter("date", date)
                .list();
        session.close();
        return payments;
    }

    @Override
    public boolean save(Payment entity) {
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
    public boolean update(Payment entity) {
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
            Payment payment = session.find(Payment.class, id);
            if (payment != null) {
                session.remove(payment);
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
    public Payment search(String id) {
        return null;
    }

    @Override
    public List<Payment> getAll() {
        Session session = FactoryConfiguration.getInstance().getSession();
        List<Payment> payments = session.createQuery("FROM Payment", Payment.class).list();
        session.close();
        return payments;
    }

    @Override
    public Optional<String> getLastPK() {
        Session session = FactoryConfiguration.getInstance().getSession();
        String lastPk = session.createQuery("SELECT p.id FROM Payment p ORDER BY p.id DESC", String.class)
                .setMaxResults(1)
                .uniqueResult();
        session.close();
        return Optional.ofNullable(lastPk);
    }
}
