package lk.ijse.serenitymentalhealththerepycentersystem.service.custom;

import lk.ijse.serenitymentalhealththerepycentersystem.dto.TherapySessionDTO;
import lk.ijse.serenitymentalhealththerepycentersystem.service.SuperService;

import java.util.List;

public interface TherapySessionService extends SuperService {

    boolean saveSession(TherapySessionDTO dto) throws Exception;

    boolean updateSession(TherapySessionDTO dto) throws Exception;

    boolean deleteSession(String sessionId) throws Exception;

    List<TherapySessionDTO> getAllSessions() throws Exception;

    List<TherapySessionDTO> findByPatientId(String patientId);

    String getNextSessionPK();

}
