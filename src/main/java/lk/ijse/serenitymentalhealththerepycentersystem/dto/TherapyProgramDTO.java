package lk.ijse.serenitymentalhealththerepycentersystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class TherapyProgramDTO {
    private String programId;
    private String programName;
    private String duration;
    private BigDecimal fee;
    private String description;
}
