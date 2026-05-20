package lk.ijse.serenitymentalhealththerepycentersystem.dao.custom;

import lk.ijse.serenitymentalhealththerepycentersystem.dao.CrudDAO;
import lk.ijse.serenitymentalhealththerepycentersystem.entity.TherapyProgram;

import java.util.List;
import java.util.Optional;

public interface TherapyProgramDAO extends CrudDAO<TherapyProgram> {

    Optional<String> getLastPK();

    TherapyProgram findByName(String name) throws Exception;

    List<TherapyProgram> findByTherapyProgramName(String name);

}
