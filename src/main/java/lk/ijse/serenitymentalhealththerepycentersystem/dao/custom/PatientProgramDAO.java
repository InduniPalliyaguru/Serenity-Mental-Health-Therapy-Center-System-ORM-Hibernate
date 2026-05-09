package lk.ijse.serenitymentalhealththerepycentersystem.dao.custom;

import lk.ijse.serenitymentalhealththerepycentersystem.dao.CrudDAO;
import lk.ijse.serenitymentalhealththerepycentersystem.entity.PatientProgram;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface PatientProgramDAO extends CrudDAO<PatientProgram> {

    public boolean delete(String patientId, String programId);
    public List<PatientProgram> searchByName(String name);
    public List<PatientProgram> findByPatientId(String id);
    public List<PatientProgram> findByProgramId(String id);
    public Optional<PatientProgram> findById(String patientId, String programId);
    public boolean updateTherapyProgramFee(String patientId, String programId, BigDecimal newFee);

}
