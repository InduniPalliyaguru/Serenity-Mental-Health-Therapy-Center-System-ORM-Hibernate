package lk.ijse.serenitymentalhealththerepycentersystem.service.custom;

import lk.ijse.serenitymentalhealththerepycentersystem.dto.TherapySessionDTO;
import lk.ijse.serenitymentalhealththerepycentersystem.entity.TherapySession;
import lk.ijse.serenitymentalhealththerepycentersystem.service.SuperService;

import java.util.List;

public interface TherapySessionService extends SuperService {

    TherapySessionDTO searchSession(String id) throws Exception;
    boolean saveSession(TherapySessionDTO dto) throws Exception;
    boolean updateSession(TherapySessionDTO dto, String oldAvailId) throws Exception;
    boolean deleteSession(String sessionId, String availabilityId) throws Exception;
    List<TherapySessionDTO> getAllSessions() throws Exception;
    List<TherapySessionDTO> searchByPatientName(String name) throws Exception;
    String generateNextSessionId() throws Exception;

}
