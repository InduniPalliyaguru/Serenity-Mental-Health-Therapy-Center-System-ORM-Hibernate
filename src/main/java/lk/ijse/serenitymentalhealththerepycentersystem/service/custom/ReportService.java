package lk.ijse.serenitymentalhealththerepycentersystem.service.custom;

import lk.ijse.serenitymentalhealththerepycentersystem.dto.HistoryDTO;
import lk.ijse.serenitymentalhealththerepycentersystem.dto.PaymentDTO;
import lk.ijse.serenitymentalhealththerepycentersystem.service.SuperService;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface ReportService extends SuperService {

    Map<String, Integer> getTherapistPerformance();

    Map<String, Integer> getProgramPopularity();

    List<HistoryDTO> getPatientHistory(String patientId);

    List<PaymentDTO> getAllFinancialData();

    void generateFinancialReport(LocalDate fromDate, LocalDate toDate);
}
