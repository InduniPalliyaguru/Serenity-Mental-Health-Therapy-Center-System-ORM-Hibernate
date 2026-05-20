package lk.ijse.serenitymentalhealththerepycentersystem.dto.tm;

import lombok.*;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class TherapyProgramTM {

    private String programId;
    private String programName;
    private String duration;
    private BigDecimal fee;
    private String description;

}
