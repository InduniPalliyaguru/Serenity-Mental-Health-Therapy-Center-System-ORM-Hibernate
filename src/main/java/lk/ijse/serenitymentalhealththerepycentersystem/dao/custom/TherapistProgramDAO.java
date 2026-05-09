package lk.ijse.serenitymentalhealththerepycentersystem.dao.custom;

import lk.ijse.serenitymentalhealththerepycentersystem.dao.CrudDAO;
import lk.ijse.serenitymentalhealththerepycentersystem.entity.TherapistProgram;

import java.util.List;
import java.util.Optional;

public interface TherapistProgramDAO extends CrudDAO<TherapistProgram> {

    boolean delete(String therapistId, String programId);
    List<TherapistProgram> findByProgramName(String name);
    List<TherapistProgram> findByTherapist(String name);
    Optional<TherapistProgram> findById(String therapistId, String programId);
    List<TherapistProgram> findByTherapistId(String therapistId);
    List<TherapistProgram> findByProgramId(String programId);


}
