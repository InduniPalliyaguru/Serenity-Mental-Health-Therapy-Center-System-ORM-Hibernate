package lk.ijse.serenitymentalhealththerepycentersystem.dto.tm;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PatientTM {

    private String patientId;
    private String name;
    private String email;
    private String contact;
    private String address;
    private String medicalHistory;

}
