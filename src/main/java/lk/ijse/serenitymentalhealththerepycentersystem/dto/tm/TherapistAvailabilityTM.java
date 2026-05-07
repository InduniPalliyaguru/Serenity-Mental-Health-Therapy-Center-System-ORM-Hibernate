package lk.ijse.serenitymentalhealththerepycentersystem.dto.tm;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TherapistAvailabilityTM {

    private String availId;
    private String therapistId;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private String status;

}
