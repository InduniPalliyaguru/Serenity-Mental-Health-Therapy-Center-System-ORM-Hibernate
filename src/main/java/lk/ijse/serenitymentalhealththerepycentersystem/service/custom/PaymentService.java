package lk.ijse.serenitymentalhealththerepycentersystem.service.custom;

import lk.ijse.serenitymentalhealththerepycentersystem.dto.PaymentDTO;
import lk.ijse.serenitymentalhealththerepycentersystem.service.SuperService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;

public interface PaymentService extends SuperService {

    boolean savePayment(PaymentDTO dto);

    boolean updatePayment(PaymentDTO dto);

    boolean deletePayment(String paymentId);

    ArrayList<PaymentDTO> getAllPayments();

    ArrayList<PaymentDTO> searchByPatientName(String name);

    ArrayList<PaymentDTO> searchByDate(LocalDate date);

    String getNextPaymentPK();

    PaymentDTO constructPaymentDto(String paymentId, String patientId, String programId, String sessionId, BigDecimal amount, LocalDate date);

    void generateInvoice(String paymentId) throws Exception;

}
