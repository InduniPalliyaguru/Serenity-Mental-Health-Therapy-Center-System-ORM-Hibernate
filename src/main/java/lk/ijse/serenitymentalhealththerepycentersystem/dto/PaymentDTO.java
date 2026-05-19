package lk.ijse.serenitymentalhealththerepycentersystem.dto;

import lk.ijse.serenitymentalhealththerepycentersystem.entity.Patient;
import lk.ijse.serenitymentalhealththerepycentersystem.entity.TherapyProgram;
import lk.ijse.serenitymentalhealththerepycentersystem.entity.TherapySession;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString

public class PaymentDTO {
    private String paymentId;
    private Patient patient;
    private TherapyProgram therapyProgram;
    private TherapySession therapySession;
    private BigDecimal amount;
    private LocalDate paymentDate;

}
