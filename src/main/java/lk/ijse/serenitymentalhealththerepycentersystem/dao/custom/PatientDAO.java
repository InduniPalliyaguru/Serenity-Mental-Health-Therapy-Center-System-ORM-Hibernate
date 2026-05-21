package lk.ijse.serenitymentalhealththerepycentersystem.dao.custom;

import lk.ijse.serenitymentalhealththerepycentersystem.dao.CrudDAO;
import lk.ijse.serenitymentalhealththerepycentersystem.entity.Patient;

import java.util.List;
import java.util.Optional;

public interface PatientDAO extends CrudDAO<Patient> {

    Optional<String> getLastPK() throws Exception;

    List<Patient> findByPatientName(String name);

    List<Patient> searchBySessionStatus(String status);

    List<Patient> getPatientsEnrolledInAllPrograms();

}
