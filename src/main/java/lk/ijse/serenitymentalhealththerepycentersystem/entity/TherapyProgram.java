package lk.ijse.serenitymentalhealththerepycentersystem.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import jakarta.persistence.Cacheable;
import org.hibernate.annotations.Cache;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "therapy_programs")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class TherapyProgram {
    @Id
    private String programId;

    @Column(unique = true, nullable = false)
    private String programName;

    @Column(nullable = false)
    private String duration;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal fee;

    @Lob
    private String description;
}
