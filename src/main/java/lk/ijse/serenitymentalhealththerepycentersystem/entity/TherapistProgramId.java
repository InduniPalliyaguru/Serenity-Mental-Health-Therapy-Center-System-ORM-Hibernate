package lk.ijse.serenitymentalhealththerepycentersystem.entity;

import jakarta.persistence.Embeddable;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Embeddable
public class TherapistProgramId {
    private String therapistId;
    private String programId;
}
