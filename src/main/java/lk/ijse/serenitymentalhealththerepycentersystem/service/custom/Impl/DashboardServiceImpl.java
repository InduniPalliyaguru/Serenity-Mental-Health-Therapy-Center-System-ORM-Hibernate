package lk.ijse.serenitymentalhealththerepycentersystem.service.custom.Impl;

import lk.ijse.serenitymentalhealththerepycentersystem.dao.DAOFactory;
import lk.ijse.serenitymentalhealththerepycentersystem.dao.custom.QueryDAO;
import lk.ijse.serenitymentalhealththerepycentersystem.dto.DashboardDTO;
import lk.ijse.serenitymentalhealththerepycentersystem.service.custom.DashboardService;

import java.util.ArrayList;
import java.util.List;

public class DashboardServiceImpl implements DashboardService {

    private final QueryDAO queryDAO = (QueryDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOType.QUERY);

    @Override
    public DashboardDTO getDashboardData() {
        long totalPatients = queryDAO.getTotalPatientsCount();
        long dailySessions = queryDAO.getDailySessionsCount();
        long activeTherapists = queryDAO.getActiveTherapistsCount();
        double monthlyRevenue = queryDAO.getMonthlyRevenue();

        List<Object[]> rows = queryDAO.getRecentActivities();
        List<String> activities = new ArrayList<>();
        for (Object[] row : rows) {
            activities.add("Payment received: LKR " + row[2] + " from " + row[1]);
        }
        return new DashboardDTO(totalPatients, dailySessions, activeTherapists, monthlyRevenue, activities);
    }
}
