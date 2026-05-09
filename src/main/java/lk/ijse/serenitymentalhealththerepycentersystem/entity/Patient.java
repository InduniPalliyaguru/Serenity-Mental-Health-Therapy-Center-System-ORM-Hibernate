package lk.ijse.serenitymentalhealththerepycentersystem.entity;

import jakarta.persistence.*;
import lk.ijse.serenitymentalhealththerepycentersystem.entity.PatientProgram;
import lk.ijse.serenitymentalhealththerepycentersystem.entity.SuperEntity;
import lombok.*;

import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Table(name = "patient")
public class Patient implements SuperEntity {

    @Id
    private String patient_id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String phone;

    @Lob
    private String address;

    @Lob
    private String medical_history;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL)
    private List<TherapySession> therapySessions;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL)
    private List<Payment> payments;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL)
    private List<PatientProgram> patientPrograms;
}
