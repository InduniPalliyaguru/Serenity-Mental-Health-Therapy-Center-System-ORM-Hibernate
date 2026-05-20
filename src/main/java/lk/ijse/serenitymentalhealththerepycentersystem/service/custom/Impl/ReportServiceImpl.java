package lk.ijse.serenitymentalhealththerepycentersystem.service.custom.Impl;

import lk.ijse.serenitymentalhealththerepycentersystem.dao.DAOFactory;
import lk.ijse.serenitymentalhealththerepycentersystem.dao.custom.QueryDAO;
import lk.ijse.serenitymentalhealththerepycentersystem.dto.HistoryDTO;
import lk.ijse.serenitymentalhealththerepycentersystem.dto.PaymentDTO;
import lk.ijse.serenitymentalhealththerepycentersystem.entity.Patient;
import lk.ijse.serenitymentalhealththerepycentersystem.service.custom.ReportService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReportServiceImpl implements ReportService {

    private final QueryDAO queryDAO = (QueryDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOType.QUERY);

    @Override
    public Map<String, Integer> getTherapistPerformance() {
        List<Object[]> data = queryDAO.getTherapistPerformanceData();
        Map<String, Integer> map = new HashMap<>();
        for (Object[] row : data) {
            map.put((String) row[0], ((Long) row[1]).intValue());
        }
        return map;
    }

    @Override
    public Map<String, Integer> getProgramPopularity() {
        List<Object[]> data = queryDAO.getProgramPopularityData();
        Map<String, Integer> map = new HashMap<>();
        for (Object[] row : data) {
            map.put((String) row[0], ((Long) row[1]).intValue());
        }
        return map;
    }

    @Override
    public List<HistoryDTO> getPatientHistory(String patientId) {
        List<Object[]> data = queryDAO.getPatientHistory(patientId);
        List<HistoryDTO> dtoList = new ArrayList<>();
        for (Object[] row : data) {
            dtoList.add(new HistoryDTO(row[0].toString(), (String) row[1], (String) row[2], (String) row[3], "N/A"));
        }
        return dtoList;
    }

    @Override
    public List<PaymentDTO> getAllFinancialData() {
        List<Object[]> data = queryDAO.getAllPaymentsInfo();
        List<PaymentDTO> dtoList = new ArrayList<>();

        for (Object[] row : data) {
            String paymentId = (String) row[0];

            Patient patient = (Patient) row[1];

            java.math.BigDecimal amount = (java.math.BigDecimal) row[2];
            java.time.LocalDate date = (java.time.LocalDate) row[3];

            dtoList.add(new PaymentDTO(paymentId, patient, null, null, amount, date));
        }
        return dtoList;
    }

    @Override
    public void generateFinancialReport(LocalDate fromDate, LocalDate toDate) {
        queryDAO.generateFinancialReport(fromDate, toDate);
    }

}
