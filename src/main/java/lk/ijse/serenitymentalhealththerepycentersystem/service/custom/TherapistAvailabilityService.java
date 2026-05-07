package lk.ijse.serenitymentalhealththerepycentersystem.service.custom;

import lk.ijse.serenitymentalhealththerepycentersystem.dto.TherapistAvailabilityDTO;
import lk.ijse.serenitymentalhealththerepycentersystem.service.SuperService;

import java.util.List;

public interface TherapistAvailabilityService extends SuperService {
    boolean saveAvailability(TherapistAvailabilityDTO dto) throws Exception;
    boolean updateAvailability(TherapistAvailabilityDTO dto) throws Exception;
    boolean deleteAvailability(String id) throws Exception;
    List<TherapistAvailabilityDTO> getAllAvailability() throws Exception;
    String getNextAvailabilityId() throws Exception;
    List<TherapistAvailabilityDTO> findAvailabilityByTherapistName(String name) throws Exception;
}
