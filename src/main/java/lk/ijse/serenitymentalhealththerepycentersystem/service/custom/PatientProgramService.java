package lk.ijse.serenitymentalhealththerepycentersystem.service.custom;

import lk.ijse.serenitymentalhealththerepycentersystem.dto.PatientDTO;
import lk.ijse.serenitymentalhealththerepycentersystem.dto.PatientProgramDTO;
import lk.ijse.serenitymentalhealththerepycentersystem.dto.TherapyProgramDTO;
import lk.ijse.serenitymentalhealththerepycentersystem.service.SuperService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public interface PatientProgramService extends SuperService {

     boolean savePatientProgram(PatientProgramDTO dto);
     boolean updatePatientProgram(PatientProgramDTO dto);
     boolean deletePatientProgram(String patientName, String programName) throws Exception;
     ArrayList<PatientProgramDTO> getAllPatientPrograms();
     PatientDTO findByPatientName(String patientName);
     TherapyProgramDTO findByProgramName(String programName);
     ArrayList<PatientProgramDTO> search(String name, boolean isPatient) throws Exception;
     String getNextPatientProgramPK();

     List<PatientProgramDTO> getProgramsByPatientId(String patientId);
     List<PatientProgramDTO> getPatientsByProgramId(String programId);

     boolean updateTherapyProgramFeeOfPatient(String patientId, String programId, BigDecimal newFee);
     PatientProgramDTO searchPatientProgramFromBothIds(String patientId, String programId);


}
