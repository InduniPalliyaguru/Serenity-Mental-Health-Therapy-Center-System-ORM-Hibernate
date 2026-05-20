package lk.ijse.serenitymentalhealththerepycentersystem.dao.custom;

import lk.ijse.serenitymentalhealththerepycentersystem.dao.CrudDAO;
import lk.ijse.serenitymentalhealththerepycentersystem.entity.PatientProgram;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface PatientProgramDAO extends CrudDAO<PatientProgram> {

    boolean delete(String patientId, String programId);

    List<PatientProgram> findByPatientId(String id);

    List<PatientProgram> findByProgramId(String id);

    Optional<PatientProgram> findById(String patientId, String programId);

    boolean updateTherapyProgramFee(String patientId, String programId, BigDecimal newFee);

}
