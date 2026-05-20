package lk.ijse.serenitymentalhealththerepycentersystem.service.custom;

import lk.ijse.serenitymentalhealththerepycentersystem.dto.PatientDTO;
import lk.ijse.serenitymentalhealththerepycentersystem.service.SuperService;

import java.util.ArrayList;
import java.util.List;

public interface PatientService extends SuperService {

    boolean savePatient(PatientDTO dto);

    boolean updatePatient(PatientDTO dto);

    boolean deletePatient(String id);

    ArrayList<PatientDTO> getAllPatients();

    ArrayList<PatientDTO> findByPatientName(String name);

    PatientDTO findPatientByID(String id);

    String getNextPatientPK() throws Exception;

    List<PatientDTO> getPatientsBySession(String status) throws Exception;
}
