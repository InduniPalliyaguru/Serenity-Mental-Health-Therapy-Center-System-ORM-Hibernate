package lk.ijse.serenitymentalhealththerepycentersystem.service.custom;

import lk.ijse.serenitymentalhealththerepycentersystem.dto.TherapistAvailabilityDTO;
import lk.ijse.serenitymentalhealththerepycentersystem.service.SuperService;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface TherapistAvailabilityService extends SuperService {
    boolean saveAvailability(TherapistAvailabilityDTO dto) throws Exception;

    boolean updateAvailability(TherapistAvailabilityDTO dto) throws Exception;

    boolean deleteAvailability(String id) throws Exception;

    List<TherapistAvailabilityDTO> getAllAvailability() throws Exception;

    String getNextAvailabilityId() throws Exception;

    List<TherapistAvailabilityDTO> findAvailabilityByTherapistName(String name) throws Exception;

    List<TherapistAvailabilityDTO> getAvailableSlots(String therapistId, LocalDate date) throws Exception;

    boolean bookTimeSlot(String therapistId, LocalDate date, LocalTime startTime, Duration sessionDuration);

    boolean restoreTimeSlot(String therapistId, LocalDate date, LocalTime startTime, Duration sessionDuration);

    List<TherapistAvailabilityDTO> findByTherapistAndDate(String therapistId, LocalDate date);
}
