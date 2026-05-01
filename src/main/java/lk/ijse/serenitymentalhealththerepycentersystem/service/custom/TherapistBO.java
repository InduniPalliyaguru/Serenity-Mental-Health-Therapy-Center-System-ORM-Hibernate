package lk.ijse.serenitymentalhealththerepycentersystem.service.custom;

import lk.ijse.serenitymentalhealththerepycentersystem.dto.TherapistDTO;
import lk.ijse.serenitymentalhealththerepycentersystem.dto.tm.TherapistProgramTM;
import lk.ijse.serenitymentalhealththerepycentersystem.service.SuperBO;

import java.util.ArrayList;
import java.util.List;

public interface TherapistBO extends SuperBO {

//     boolean saveTherapist(TherapistDTO dto);
     boolean updateTherapist(TherapistDTO dto);
     boolean deleteTherapist(String pk);
     ArrayList<TherapistDTO> getAllTherapists();
     ArrayList<TherapistDTO> findByTherapistName(String name);
    TherapistDTO findByTherapistId(String id);
     String getNextTherapistPK();
    boolean saveTherapistWithPrograms(TherapistDTO therapistDTO) throws Exception;
}


