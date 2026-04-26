package lk.ijse.serenitymentalhealththerepycentersystem.service.custom;

import lk.ijse.serenitymentalhealththerepycentersystem.dto.TherapyProgramDTO;
import lk.ijse.serenitymentalhealththerepycentersystem.service.SuperBO;

import java.util.List;

public interface TherapyProgramBO extends SuperBO {

    boolean saveProgram(TherapyProgramDTO dto) throws Exception;
    boolean updateProgram(TherapyProgramDTO dto) throws Exception;
    boolean deleteProgram(String id) throws Exception;
    List<TherapyProgramDTO> getAllPrograms() throws Exception;
    TherapyProgramDTO searchProgram(String id) throws Exception;
    String getNextTherapyProgramPK() throws Exception;

}
