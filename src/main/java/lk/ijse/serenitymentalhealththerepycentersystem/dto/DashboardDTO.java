package lk.ijse.serenitymentalhealththerepycentersystem.dto;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class DashboardDTO {
    private long totalPatients;
    private long dailySessions;
    private long activeTherapists;
    private double monthlyRevenue;
    private List<String> recentActivities;
}
