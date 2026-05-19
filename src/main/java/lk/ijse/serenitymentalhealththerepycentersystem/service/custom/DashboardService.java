package lk.ijse.serenitymentalhealththerepycentersystem.service.custom;

import lk.ijse.serenitymentalhealththerepycentersystem.dto.DashboardDTO;
import lk.ijse.serenitymentalhealththerepycentersystem.service.SuperService;

public interface DashboardService extends SuperService {
    
    DashboardDTO getDashboardData();
}
