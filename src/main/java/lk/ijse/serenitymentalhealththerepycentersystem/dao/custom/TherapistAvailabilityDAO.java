package lk.ijse.serenitymentalhealththerepycentersystem.dao.custom;

import lk.ijse.serenitymentalhealththerepycentersystem.dao.CrudDAO;
import lk.ijse.serenitymentalhealththerepycentersystem.entity.TherapistAvailability;

import java.util.List;
import java.util.Optional;

public interface TherapistAvailabilityDAO extends CrudDAO<TherapistAvailability> {

    Optional<String> getLastPK();
    List<TherapistAvailability> findByTherapistName(String name);

}
