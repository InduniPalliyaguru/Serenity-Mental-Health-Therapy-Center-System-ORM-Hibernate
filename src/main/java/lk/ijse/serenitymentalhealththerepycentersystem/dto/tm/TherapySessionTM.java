package lk.ijse.serenitymentalhealththerepycentersystem.dto.tm;


import lombok.*;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TherapySessionTM {
    private String sessionId;
    private String patientId;
    private String therapyProgramId;
    private String therapistId;
    private String availabilityId;
    private LocalDate sessionDate;
    private LocalTime sessionTime;
    private Duration duration;
    private String status;

    public TherapySessionTM(String sessionId, String patientId, String therapistId, String therapyProgramId, LocalDate sessionDate, String status) {
        this.sessionId = sessionId;
        this.patientId = patientId;
        this.therapistId = therapistId;
        this.therapyProgramId = therapyProgramId;
        this.sessionDate = sessionDate;
        this.status = status;

        // අනෙකුත් fields default ලෙස තැබීම
//        this.availabilityId = null;
//        this.sessionTime = null;
//        this.duration = 0;
    }
}