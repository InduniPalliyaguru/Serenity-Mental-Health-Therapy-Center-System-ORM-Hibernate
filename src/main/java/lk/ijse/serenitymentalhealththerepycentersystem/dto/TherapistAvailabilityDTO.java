package lk.ijse.serenitymentalhealththerepycentersystem.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TherapistAvailabilityDTO {

    private String availabilityId;
    private String therapistId;
    private String therapistName; // UI එකේ පෙන්වීමට පහසුවට
    private LocalDate availableDate;
    private LocalTime startTime;
    private LocalTime endTime;
//    private List<String> availableSlots;
    private boolean isAvailable;

}
