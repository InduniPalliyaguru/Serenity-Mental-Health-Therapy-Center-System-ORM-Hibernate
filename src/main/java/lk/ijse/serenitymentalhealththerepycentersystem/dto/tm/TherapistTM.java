package lk.ijse.serenitymentalhealththerepycentersystem.dto.tm;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString

public class TherapistTM {
    private String therapistId;
    private String name;
    private String email;
    private String phone;
    private String specialization;
}

