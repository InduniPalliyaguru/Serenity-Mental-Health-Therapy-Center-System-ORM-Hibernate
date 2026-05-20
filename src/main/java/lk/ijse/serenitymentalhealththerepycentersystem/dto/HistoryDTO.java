package lk.ijse.serenitymentalhealththerepycentersystem.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class HistoryDTO {

    private String date;
    private String programName;
    private String therapistName;
    private String historyStatus;
    private String notes;

}
