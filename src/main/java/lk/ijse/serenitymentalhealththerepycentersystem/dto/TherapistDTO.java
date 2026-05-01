package lk.ijse.serenitymentalhealththerepycentersystem.dto;

import lk.ijse.serenitymentalhealththerepycentersystem.dto.tm.TherapistProgramTM;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TherapistDTO {
    private String therapistId;
    private String name;
    private String email;
    private String phone;
    private String specialization;

    private List<TherapistProgramTM> assignedPrograms;

}

