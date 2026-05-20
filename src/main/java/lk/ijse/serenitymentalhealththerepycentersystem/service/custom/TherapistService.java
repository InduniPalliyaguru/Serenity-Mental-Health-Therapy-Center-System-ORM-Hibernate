package lk.ijse.serenitymentalhealththerepycentersystem.service.custom;

import lk.ijse.serenitymentalhealththerepycentersystem.dto.TherapistDTO;
import lk.ijse.serenitymentalhealththerepycentersystem.service.SuperService;

import java.util.ArrayList;

public interface TherapistService extends SuperService {

    boolean updateTherapist(TherapistDTO dto);

    boolean deleteTherapist(String pk);

    ArrayList<TherapistDTO> getAllTherapists();

    ArrayList<TherapistDTO> findByTherapistName(String name);

    TherapistDTO findByTherapistId(String id);

    String getNextTherapistPK();

    boolean saveTherapistWithPrograms(TherapistDTO therapistDTO) throws Exception;
}


