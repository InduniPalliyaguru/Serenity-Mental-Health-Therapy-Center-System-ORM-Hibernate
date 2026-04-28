package lk.ijse.serenitymentalhealththerepycentersystem.dao.custom;

import lk.ijse.serenitymentalhealththerepycentersystem.dao.CrudDAO;
import lk.ijse.serenitymentalhealththerepycentersystem.entity.Therapist;

import java.util.List;
import java.util.Optional;

public interface TherapistDAO extends CrudDAO<Therapist> {

    List<Therapist> findByTherapistName(String name);
    Optional<String> getLastPK();
}
