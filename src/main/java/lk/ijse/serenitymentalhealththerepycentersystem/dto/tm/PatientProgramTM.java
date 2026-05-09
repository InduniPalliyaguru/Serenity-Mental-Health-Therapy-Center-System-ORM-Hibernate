package lk.ijse.serenitymentalhealththerepycentersystem.dto.tm;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PatientProgramTM {
    private String patientId;
    private String patientName;
    private String programId;
    private String programName;
    private LocalDate registrationDate;
    private String paymentId;
    private BigDecimal programFee;
    private BigDecimal leftToPay;

}
