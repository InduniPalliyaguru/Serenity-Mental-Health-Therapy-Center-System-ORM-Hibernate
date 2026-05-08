package lk.ijse.serenitymentalhealththerepycentersystem.service.custom.Impl;

import lk.ijse.serenitymentalhealththerepycentersystem.config.FactoryConfiguration;
import lk.ijse.serenitymentalhealththerepycentersystem.dao.DAOFactory;
import lk.ijse.serenitymentalhealththerepycentersystem.dao.custom.*;
import lk.ijse.serenitymentalhealththerepycentersystem.dto.TherapySessionDTO;
import lk.ijse.serenitymentalhealththerepycentersystem.entity.*;
import lk.ijse.serenitymentalhealththerepycentersystem.service.ServiceFactory;
import lk.ijse.serenitymentalhealththerepycentersystem.service.custom.TherapistAvailabilityService;
import lk.ijse.serenitymentalhealththerepycentersystem.service.custom.TherapySessionService;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TherapySessionServiceImpl implements TherapySessionService {

    TherapySessionDAO therapySessionDAO = (TherapySessionDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOType.THERAPY_SESSION);
    TherapistDAO therapistDAO = (TherapistDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOType.THERAPIST);
    PatientDAO patientDAO = (PatientDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOType.PATIENT);
    TherapyProgramDAO therapyProgramDAO = (TherapyProgramDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOType.THERAPY_PROGRAM);

    TherapistAvailabilityService therapistAvailabilityBO = (TherapistAvailabilityService) ServiceFactory.getInstance().getService(ServiceFactory.ServiceType.THERAPIST_AVAILABILITY);


    @Override
    public TherapySessionDTO searchSession(String id) throws Exception {
        TherapySession s = sessionDAO.search(id);
        if (s != null) {
            return new TherapySessionDTO(
                    s.getSession_id(),
                    s.getPatient().getPatient_id(),
                    s.getTherapy_program().getProgramId(),
                    s.getTherapist().getTherapist_id(),
                    s.getTherapistAvailability() != null ? s.getTherapistAvailability().getAvailability_id() : null,
                    s.getSession_date(),
                    s.getStart_time(),
                    s.getDuration(),
                    s.getStatus()
            );
        }
        return null;
    }

    @Override
    public boolean saveSession(TherapySessionDTO dto) throws Exception {
//        Session session = FactoryConfiguration.getInstance().getSession();
//        Transaction tx = session.beginTransaction();
//
//        try {
//            TherapySession entity = new TherapySession();
//            entity.setSession_id(dto.getSessionId());
//            entity.setPatient(patientDAO.search(dto.getPatientId()));
//            entity.setTherapist(therapistDAO.search(dto.getTherapistId()));
//            entity.setTherapy_program(programDAO.search(dto.getTherapyProgramId()));
//
//            TherapistAvailability availability = availDAO.search(dto.getAvailabilityId());
//            entity.setTherapistAvailability(availability);
//
//            entity.setSession_date(dto.getSessionDate());
//            entity.setStart_time(dto.getSessionTime());
//            entity.setDuration(dto.getDuration());
//            entity.setStatus(dto.getStatus());
//
//            session.save(entity);
//
//            if (availability != null) {
//                availability.set_available(false);
//                session.update(availability);
//            }
//
//            tx.commit();
//            return true;
//        } catch (Exception e) {
//            if (tx != null) tx.rollback();
//            throw e;
//        } finally {
//            session.close();
//        }
//
//            Session session = FactoryConfiguration.getInstance().getSession();
//            Transaction tx = session.beginTransaction();
//
//            try {
//                // 1. Session එක Save කිරීම
//                TherapySession entity = new TherapySession();
//                entity.setSession_id(dto.getSessionId());
//            entity.setPatient(patientDAO.search(dto.getPatientId()));
//            entity.setTherapist(therapistDAO.search(dto.getTherapistId()));
//            entity.setTherapy_program(programDAO.search(dto.getTherapyProgramId()));
//
//            TherapistAvailability availability = availDAO.search(dto.getAvailabilityId());
//            entity.setTherapistAvailability(availability);
//
//            entity.setSession_date(dto.getSessionDate());
//            entity.setStart_time(dto.getSessionTime());
//            entity.setDuration(dto.getDuration());
//            entity.setStatus(dto.getStatus());
//                session.save(entity);
//
//                // 2. තෝරාගත් නිශ්චිත Time Slot එක පමණක් Update කිරීම
//                // මෙහිදී dto.getAvailabilityId() මගින් ලැබෙන්නේ රෝගියා Table එකෙන් Click කළ Row එකේ ID එකයි.
//                if (dto.getAvailabilityId() != null) {
//                    TherapistAvailability availability = session.get(TherapistAvailability.class, dto.getAvailabilityId());
//
//                    if (availability != null) {
//                        // එම පේළිය පමණක් 'Booked' ලෙස වෙනස් කරයි
//                        availability.set_available(false);
//                        session.update(availability);
//                    }
//                }
//
//                tx.commit();
//                return true;
//            } catch (Exception e) {
//                if (tx != null) tx.rollback();
//                throw e;
//            } finally {
//                session.close();
//            }

        boolean isCompleted = false;

        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();

        try {
            // Retrieve the entities from their respective DAOs
            Therapist therapistOpt = therapistDAO.search(dto.getTherapistId());
            Patient patientOpt = patientDAO.search(dto.getPatientId());
            TherapyProgram programOpt = therapyProgramDAO.search(dto.getTherapyProgramId());

            // Check if any of the required entities are not found
            if (therapistOpt == null || patientOpt == null || programOpt == null) {
                return false;
            }

            // Create the TherapySession entity
            TherapySession therapySession = new TherapySession();

            therapySession.setSession_id(dto.getSessionId());
            therapySession.setTherapist(therapistOpt);
            therapySession.setPatient(patientOpt);
            therapySession.setTherapy_program(programOpt);
            therapySession.setTherapistAvailability(null);
            therapySession.setSession_date(dto.getSessionDate());
            therapySession.setStart_time(dto.getSessionTime());
            therapySession.setDuration(dto.getDuration());
            therapySession.setStatus(dto.getStatus());

            // Convert the duration (in minutes) to a Duration object
            Duration sessionDuration = Duration.ofMinutes(dto.getDuration());

            // First save the therapy session
            boolean saved = therapySessionDAO.save(therapySession);
            if (!saved) {
                transaction.rollback();
                return false;
            }

            // Then attempt to book the time slot
            boolean success = therapistAvailabilityBO.bookTimeSlot(
                    dto.getTherapistId(),
                    dto.getSessionDate(),
                    dto.getSessionTime(),
                    sessionDuration
            );

            if (success) {
                isCompleted = true;
                transaction.commit();
            } else {
                // If booking fails, roll back the transaction
                transaction.rollback();
                isCompleted = false;
            }
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }

        return isCompleted;

    }

    @Override
    public boolean updateSession(TherapySessionDTO dto, String oldAvailId) throws Exception {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction tx = session.beginTransaction();

        try {

            TherapySession entity = new TherapySession();
            entity.setSession_id(dto.getSessionId());
            session.update(entity);

            if (!dto.getAvailabilityId().equals(oldAvailId)) {

                TherapistAvailability oldAvail = availDAO.search(oldAvailId);
                oldAvail.set_available(true);
                session.update(oldAvail);

                TherapistAvailability newAvail = availDAO.search(dto.getAvailabilityId());
                newAvail.set_available(false);
                session.update(newAvail);
            }

            tx.commit();
            return true;
        } catch (Exception e) {
            tx.rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    @Override
    public boolean deleteSession(String sessionId, String availabilityId) throws Exception {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction tx = session.beginTransaction();

        try {
            TherapySession ts = session.get(TherapySession.class, sessionId);
            if (ts != null) {
                session.delete(ts);

                TherapistAvailability availability = session.get(TherapistAvailability.class, availabilityId);
                if (availability != null) {
                    availability.set_available(true);
                    session.update(availability);
                }
            }
            tx.commit();
            return true;
        } catch (Exception e) {
            tx.rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    @Override
    public List<TherapySessionDTO> getAllSessions() throws Exception {
        List<TherapySession> list = sessionDAO.getAll();
        List<TherapySessionDTO> dtoList = new ArrayList<>();

        for (TherapySession s : list) {
            // Availability එක null ද කියලා පරීක්ෂා කරලා ID එක තීරණය කරනවා
            String availabilityId = (s.getTherapistAvailability() != null)
                    ? s.getTherapistAvailability().getAvailability_id()
                    : "N/A"; // Null නම් "N/A" ලෙස පෙන්වයි

            dtoList.add(new TherapySessionDTO(
                    s.getSession_id(),
                    s.getPatient().getPatient_id(),
                    s.getTherapy_program().getProgramId(),
                    s.getTherapist().getTherapist_id(),
                    availabilityId, // කෙලින්ම get නොකර උඩ හදාගත්ත variable එක මෙතනට දාන්න
                    s.getSession_date(),
                    s.getStart_time(),
                    s.getDuration(),
                    s.getStatus()
            ));
        }
        return dtoList;
    }

    @Override
    public List<TherapySessionDTO> searchByPatientName(String name) throws Exception {
//        List<TherapySession> entities = sessionDAO.searchByPatientName(name);
//        List<TherapySessionDTO> dtoList = new ArrayList<>();
//
//        for (TherapySession s : entities) {
//            dtoList.add(new TherapySessionDTO(
//                    s.getSession_id(),
//                    s.getPatient().getPatient_id(),
//                    s.getTherapy_program().getProgramId(),
//                    s.getTherapist().getTherapist_id(),
//                    s.getTherapistAvailability() != null ? s.getTherapistAvailability().getAvailability_id() : null,
//                    s.getSession_date(),
//                    s.getStart_time(),
//                    s.getDuration(),
//                    s.getStatus()
//            ));
//        }
//        return dtoList;
    }

    @Override
    public List<TherapySessionDTO> findByPatientId(String patientId) {
        return List.of();
    }

    @Override
    public String getNextSessionPK() {
        return "";
    }

}
