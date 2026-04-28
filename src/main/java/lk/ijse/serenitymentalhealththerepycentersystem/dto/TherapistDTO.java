package lk.ijse.serenitymentalhealththerepycentersystem.dto;

import lombok.*;

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

}