package lk.ijse.serenitymentalhealththerepycentersystem.dao.custom;

import lk.ijse.serenitymentalhealththerepycentersystem.dao.SuperDAO;

import java.util.List;

public interface QueryDAO extends SuperDAO {

    List<Object[]> getTherapistPerformanceData();

    List<Object[]> getProgramPopularityData();

    List<Object[]> getPatientHistory(String patientId);

    List<Object[]> getAllPaymentsInfo();

    long getTotalPatientsCount();

    long getDailySessionsCount();

    long getActiveTherapistsCount();

    double getMonthlyRevenue();

    List<Object[]> getRecentActivities();
}
