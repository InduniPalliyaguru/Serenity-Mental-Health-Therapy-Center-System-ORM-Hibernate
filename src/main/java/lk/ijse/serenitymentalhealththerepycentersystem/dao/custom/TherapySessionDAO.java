package lk.ijse.serenitymentalhealththerepycentersystem.dao.custom;

import lk.ijse.serenitymentalhealththerepycentersystem.dao.CrudDAO;
import lk.ijse.serenitymentalhealththerepycentersystem.entity.TherapySession;
import org.hibernate.Session;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TherapySessionDAO extends CrudDAO<TherapySession> {

    Optional<TherapySession> findBySessionId(String sessionId);

    List<TherapySession> findByPatientId(String patientId);

    Optional<String> getLastPK();

    boolean save(TherapySession entity, Session session);

    List<TherapySession> findActiveSessionsByTherapist(Session session, String therapistId, LocalDate date);

    List<TherapySession> findActiveSessionsByPatient(Session session, String patientId, LocalDate date);

}
