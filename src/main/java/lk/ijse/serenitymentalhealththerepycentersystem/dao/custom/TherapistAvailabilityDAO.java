package lk.ijse.serenitymentalhealththerepycentersystem.dao.custom;

import lk.ijse.serenitymentalhealththerepycentersystem.dao.CrudDAO;
import lk.ijse.serenitymentalhealththerepycentersystem.entity.TherapistAvailability;
import org.hibernate.Session;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TherapistAvailabilityDAO extends CrudDAO<TherapistAvailability> {

    Optional<String> getLastPK();
    List<TherapistAvailability> findByTherapistName(String name);
    List<TherapistAvailability> getAvailableSlots(String therapistId, LocalDate date) throws Exception;
    boolean updateStatus(String id, String status) throws Exception;
    public List<TherapistAvailability> findByTherapistAndDate(String therapistId, LocalDate date);
    boolean update(TherapistAvailability entity, Session session);

}
