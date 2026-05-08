package lk.ijse.serenitymentalhealththerepycentersystem.service.custom.Impl;

import lk.ijse.serenitymentalhealththerepycentersystem.config.FactoryConfiguration;
import lk.ijse.serenitymentalhealththerepycentersystem.dao.DAOFactory;
import lk.ijse.serenitymentalhealththerepycentersystem.dao.custom.*;
import lk.ijse.serenitymentalhealththerepycentersystem.dto.TherapySessionDTO;
import lk.ijse.serenitymentalhealththerepycentersystem.entity.TherapistAvailability;
import lk.ijse.serenitymentalhealththerepycentersystem.entity.TherapySession;
import lk.ijse.serenitymentalhealththerepycentersystem.service.custom.TherapySessionService;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TherapySessionServiceImpl implements TherapySessionService {

    TherapySessionDAO sessionDAO = (TherapySessionDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOType.THERAPY_SESSION);
    PatientDAO patientDAO = (PatientDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOType.PATIENT);
    TherapistDAO therapistDAO = (TherapistDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOType.THERAPIST);
    TherapyProgramDAO programDAO = (TherapyProgramDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOType.THERAPY_PROGRAM);
    TherapistAvailabilityDAO availDAO = (TherapistAvailabilityDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOType.THERAPIST_AVAILABILITY);

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

            Session session = FactoryConfiguration.getInstance().getSession();
            Transaction tx = session.beginTransaction();

            try {
                // 1. Session එක Save කිරීම
                TherapySession entity = new TherapySession();
                entity.setSession_id(dto.getSessionId());
            entity.setPatient(patientDAO.search(dto.getPatientId()));
            entity.setTherapist(therapistDAO.search(dto.getTherapistId()));
            entity.setTherapy_program(programDAO.search(dto.getTherapyProgramId()));

//            TherapistAvailability availability = availDAO.search(dto.getAvailabilityId());
//            entity.setTherapistAvailability(availability);

            entity.setSession_date(dto.getSessionDate());
            entity.setStart_time(dto.getSessionTime());
            entity.setDuration(dto.getDuration());
            entity.setStatus(dto.getStatus());
                session.save(entity);

                // 2. තෝරාගත් නිශ්චිත Time Slot එක පමණක් Update කිරීම
                // මෙහිදී dto.getAvailabilityId() මගින් ලැබෙන්නේ රෝගියා Table එකෙන් Click කළ Row එකේ ID එකයි.
                if (dto.getAvailabilityId() != null) {
                    TherapistAvailability availability = session.get(TherapistAvailability.class, dto.getAvailabilityId());

                    if (availability != null) {
                        // එම පේළිය පමණක් 'Booked' ලෙස වෙනස් කරයි
                        availability.set_available(false);
                        session.update(availability);
                    }
                }

                tx.commit();
                return true;
            } catch (Exception e) {
                if (tx != null) tx.rollback();
                throw e;
            } finally {
                session.close();
            }

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
        List<TherapySession> entities = sessionDAO.searchByPatientName(name);
        List<TherapySessionDTO> dtoList = new ArrayList<>();

        for (TherapySession s : entities) {
            dtoList.add(new TherapySessionDTO(
                    s.getSession_id(),
                    s.getPatient().getPatient_id(),
                    s.getTherapy_program().getProgramId(),
                    s.getTherapist().getTherapist_id(),
                    s.getTherapistAvailability() != null ? s.getTherapistAvailability().getAvailability_id() : null,
                    s.getSession_date(),
                    s.getStart_time(),
                    s.getDuration(),
                    s.getStatus()
            ));
        }
        return dtoList;
    }

    @Override
    public String generateNextSessionId() throws Exception {
        Optional<String> lastPkOpt = sessionDAO.getLastPK();

        if (lastPkOpt.isPresent()) {
            String lastPk = lastPkOpt.get();
            String numericPart = lastPk.substring(2);
            int currentId = Integer.parseInt(numericPart);
            int nextId = currentId + 1;
            return String.format("S%03d", nextId);
        }

        return "S001";
    }
}
