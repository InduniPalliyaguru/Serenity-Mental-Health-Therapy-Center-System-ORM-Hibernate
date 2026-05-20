package lk.ijse.serenitymentalhealththerepycentersystem.dto.tm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class HistoryTM {

    private String date;
    private String programName;
    private String therapistName;
    private String historyStatus;
    private String notes;

}