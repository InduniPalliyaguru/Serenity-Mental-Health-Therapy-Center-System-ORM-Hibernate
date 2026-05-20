package lk.ijse.serenitymentalhealththerepycentersystem.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CacheConcurrencyStrategy;
//import jakarta.persistence.Cacheable;
import org.hibernate.annotations.Cache;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "therapist")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Therapist implements SuperEntity {
    @Id
    private String therapist_id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private String specialization;

    @OneToMany(mappedBy = "therapist", cascade = CascadeType.ALL)
    private List<TherapySession> therapySessions;

    @OneToMany(mappedBy = "therapist", cascade = CascadeType.ALL)
    private List<TherapistAvailability> availabilities;

    @OneToMany(mappedBy = "therapist", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<TherapistProgram> therapistPrograms;
}
