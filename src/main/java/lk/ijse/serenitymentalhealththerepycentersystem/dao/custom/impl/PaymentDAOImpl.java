package lk.ijse.serenitymentalhealththerepycentersystem.dao.custom.impl;

import lk.ijse.serenitymentalhealththerepycentersystem.config.FactoryConfiguration;
import lk.ijse.serenitymentalhealththerepycentersystem.dao.custom.PaymentDAO;
import lk.ijse.serenitymentalhealththerepycentersystem.entity.Payment;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.JasperViewer;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    public boolean save(Payment entity) {
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
    public boolean update(Payment entity) {
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
            System.out.println(e.getMessage());
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

    @Override
    public void printInvoiceReport(String paymentId) {

        Session session = FactoryConfiguration.getInstance().getSession();

        try {
            session.doWork(new org.hibernate.jdbc.Work() {
                @Override
                public void execute(Connection connection) throws SQLException {
                    try {
                        InputStream reportStream = getClass().getResourceAsStream("/reports/paymentInvoice.jasper");

                        if (reportStream == null) {
                            throw new RuntimeException("Compiled report file (paymentInvoice.jasper) not found in resources/reports/ folder!");
                        }

                        Map<String, Object> parameters = new HashMap<>();
                        parameters.put("p_payment_id", paymentId);

                        JasperPrint jasperPrint = JasperFillManager.fillReport(reportStream, parameters, connection);

                        JasperViewer.viewReport(jasperPrint, false);

                    } catch (JRException e) {
                        System.out.println(e.getMessage());
                    }
                }
            });
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }
}
