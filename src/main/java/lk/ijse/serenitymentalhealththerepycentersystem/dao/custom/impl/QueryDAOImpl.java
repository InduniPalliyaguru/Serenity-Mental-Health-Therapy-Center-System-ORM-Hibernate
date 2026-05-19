package lk.ijse.serenitymentalhealththerepycentersystem.dao.custom.impl;

import lk.ijse.serenitymentalhealththerepycentersystem.config.FactoryConfiguration;
import lk.ijse.serenitymentalhealththerepycentersystem.dao.custom.QueryDAO;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.time.LocalDate;
import java.util.List;

public class QueryDAOImpl implements QueryDAO {

    @Override
    public List<Object[]> getTherapistPerformanceData() {
        Session session = FactoryConfiguration.getInstance().getSession();
        String hql = "SELECT t.name, COUNT(s.session_id) FROM TherapySession s JOIN s.therapist t GROUP BY t.name";
        List<Object[]> list = session.createQuery(hql, Object[].class).list();
        session.close();
        return list;
    }

    @Override
    public List<Object[]> getProgramPopularityData() {
        Session session = FactoryConfiguration.getInstance().getSession();
        String hql = "SELECT p.programName, COUNT(s.session_id) FROM TherapySession s JOIN s.therapy_program p GROUP BY p.programName";
        List<Object[]> list = session.createQuery(hql, Object[].class).list();
        session.close();
        return list;
    }

    @Override
    public List<Object[]> getPatientHistory(String patientId) {
        Session session = FactoryConfiguration.getInstance().getSession();
        String hql = "SELECT s.session_date, p.programName, t.name, s.status FROM TherapySession s JOIN s.therapy_program p JOIN s.therapist t WHERE s.patient.patient_id = :id ORDER BY s.session_date DESC";
        Query<Object[]> query = session.createQuery(hql, Object[].class);
        query.setParameter("id", patientId);
        List<Object[]> list = query.list();
        session.close();
        return list;
    }

    @Override
    public List<Object[]> getAllPaymentsInfo() {
        Session session = FactoryConfiguration.getInstance().getSession();
        // pt.name වෙනුවට pt (සම්පූර්ණ Patient object එකම) ගන්නවා
        String hql = "SELECT py.payment_id, pt, py.amount, py.payment_date FROM Payment py JOIN py.patient pt ORDER BY py.payment_date DESC";
        List<Object[]> list = session.createQuery(hql, Object[].class).list();
        session.close();
        return list;
    }

    @Override
    public long getTotalPatientsCount() {
        Session session = FactoryConfiguration.getInstance().getSession();
        String hql = "SELECT COUNT(p.patient_id) FROM Patient p";
        long count = session.createQuery(hql, Long.class).uniqueResult();
        session.close();
        return count;
    }

    @Override
    public long getDailySessionsCount() {
        Session session = FactoryConfiguration.getInstance().getSession();
        String hql = "SELECT COUNT(s.session_id) FROM TherapySession s WHERE s.session_date = :today";
        long count = session.createQuery(hql, Long.class)
                .setParameter("today", LocalDate.now())
                .uniqueResult();
        session.close();
        return count;
    }

    @Override
    public long getActiveTherapistsCount() {
        Session session = FactoryConfiguration.getInstance().getSession();
        String hql = "SELECT COUNT(t.therapist_id) FROM Therapist t";
        long count = session.createQuery(hql, Long.class).uniqueResult();
        session.close();
        return count;
    }

    @Override
    public double getMonthlyRevenue() {
        Session session = FactoryConfiguration.getInstance().getSession();
        LocalDate firstDayOfMonth = LocalDate.now().withDayOfMonth(1);
        String hql = "SELECT SUM(p.amount) FROM Payment p WHERE p.payment_date >= :startDate";
        java.math.BigDecimal sum = session.createQuery(hql, java.math.BigDecimal.class)
                .setParameter("startDate", firstDayOfMonth)
                .uniqueResult();
        session.close();
        return sum != null ? sum.doubleValue() : 0.0;
    }

    @Override
    public List<Object[]> getRecentActivities() {
        Session session = FactoryConfiguration.getInstance().getSession();
        String hql = "SELECT p.payment_date, pt.name, p.amount FROM Payment p JOIN p.patient pt ORDER BY p.payment_id DESC";
        List<Object[]> list = session.createQuery(hql, Object[].class)
                .setMaxResults(3)
                .list();
        session.close();
        return list;
    }
}
