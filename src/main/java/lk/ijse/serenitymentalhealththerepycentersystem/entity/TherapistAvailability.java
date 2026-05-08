package lk.ijse.serenitymentalhealththerepycentersystem.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "therapist_availability")
public class TherapistAvailability implements SuperEntity {

    @Id
    @Column(name = "availability_id")
    private String availability_id; // උදා: AVA001, AVA002

    @ManyToOne
    @JoinColumn(name = "therapist_id", nullable = false)
    private Therapist therapist;

    @Column(name = "available_date", nullable = false)
    private LocalDate available_date;

    @Column(name = "start_time", nullable = false)
    private LocalTime start_time;

    @Column(name = "end_time", nullable = false)
    private LocalTime end_time;

    @Column(name = "is_available", nullable = false)
    private boolean is_available = true; // පාලනය කරන්නේ මෙතැනින්
}