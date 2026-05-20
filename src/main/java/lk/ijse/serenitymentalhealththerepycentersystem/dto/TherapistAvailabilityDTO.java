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
    private String therapistName;
    private LocalDate availableDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private List<String> availableSlots;
    private boolean isAvailable;

    public TherapistAvailabilityDTO(String availabilityId, String therapistId, String therapistName, LocalDate availableDate, LocalTime startTime, LocalTime endTime, boolean isAvailable) {
        this.availabilityId = availabilityId;
        this.therapistId = therapistId;
        this.therapistName = therapistName;
        this.availableDate = availableDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.isAvailable = isAvailable;
    }
}
