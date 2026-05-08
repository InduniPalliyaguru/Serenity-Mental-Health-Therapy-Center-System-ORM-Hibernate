package lk.ijse.serenitymentalhealththerepycentersystem.dto.tm;

//import lombok.*;
//
//import java.time.LocalDate;
//import java.time.LocalTime;
//
//@Getter
//@Setter
//@AllArgsConstructor
//@NoArgsConstructor
//@ToString
//public class TimeSlotRowTM {
//    private String availabilityId;
//    private LocalDate date;
//    private LocalTime startTime;
//    private String status;
//}

import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString

public class TimeSlotRowTM {
    private String timeSlot;
    private String date1Status;
    private String date2Status;
    private String date3Status;
    private String date4Status;
    private String date5Status;


}
