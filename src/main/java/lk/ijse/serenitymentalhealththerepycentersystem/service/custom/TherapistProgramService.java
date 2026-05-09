package lk.ijse.serenitymentalhealththerepycentersystem.service.custom;

import lk.ijse.serenitymentalhealththerepycentersystem.dto.TherapistProgramDTO;
import lk.ijse.serenitymentalhealththerepycentersystem.service.SuperService;

import java.util.List;

public interface TherapistProgramService extends SuperService {

    boolean saveTherapistProgram(String therapistId, String programId);

    boolean updateTherapistProgram(String therapistId, String programId);

    boolean deleteTherapistProgram(String therapistId, String programId);

    TherapistProgramDTO findById(String therapistId, String programId);

    List<TherapistProgramDTO> getAllTherapistPrograms();

    List<TherapistProgramDTO> findByProgramName(String name);

    List<TherapistProgramDTO> getTherapistProgramsByTherapist(String id);

    List<TherapistProgramDTO> getTherapistProgramsByTherapistId(String therapistId);

    List<TherapistProgramDTO> getTherapistProgramsByProgramId(String programId);


}
