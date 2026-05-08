package lk.ijse.serenitymentalhealththerepycentersystem.dto.tm;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AvailabilitySlotTM {
    private String availabilityId;
    private LocalDate date;
    private LocalTime startTime;
    private String status;
}
