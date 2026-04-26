package lk.ijse.serenitymentalhealththerepycentersystem.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "therapy_programs")
public class TherapyProgram {
    @Id
    private String programId;

    @Column(unique = true, nullable = false)
    private String programName;

    @Column(nullable = false)
    private String duration;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal fee;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;
}
