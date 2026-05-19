package lk.ijse.serenitymentalhealththerepycentersystem.dao.custom.impl;

import lk.ijse.serenitymentalhealththerepycentersystem.config.FactoryConfiguration;
import lk.ijse.serenitymentalhealththerepycentersystem.dao.custom.QueryDAO;
import org.hibernate.Session;
import org.hibernate.query.Query;

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
}
