package lk.ijse.serenitymentalhealththerepycentersystem.dao.custom;

import lk.ijse.serenitymentalhealththerepycentersystem.dao.CrudDAO;
import lk.ijse.serenitymentalhealththerepycentersystem.entity.TherapyProgram;

import java.util.Optional;

public interface TherapyProgramDAO extends CrudDAO<TherapyProgram> {

    Optional<String> getLastPK();

}
