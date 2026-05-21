package lk.ijse.serenitymentalhealththerepycentersystem.service.custom.Impl;

import lk.ijse.serenitymentalhealththerepycentersystem.config.FactoryConfiguration;
import lk.ijse.serenitymentalhealththerepycentersystem.dao.DAOFactory;
import lk.ijse.serenitymentalhealththerepycentersystem.dao.custom.*;
import lk.ijse.serenitymentalhealththerepycentersystem.dto.TherapySessionDTO;
import lk.ijse.serenitymentalhealththerepycentersystem.entity.*;
import lk.ijse.serenitymentalhealththerepycentersystem.exception.ScheduleConflictException;
import lk.ijse.serenitymentalhealththerepycentersystem.service.ServiceFactory;
import lk.ijse.serenitymentalhealththerepycentersystem.service.custom.TherapistAvailabilityService;
import lk.ijse.serenitymentalhealththerepycentersystem.service.custom.TherapySessionService;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TherapySessionServiceImpl implements TherapySessionService {

    TherapySessionDAO therapySessionDAO = (TherapySessionDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOType.THERAPY_SESSION);
    TherapistDAO therapistDAO = (TherapistDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOType.THERAPIST);
    PatientDAO patientDAO = (PatientDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOType.PATIENT);
    TherapyProgramDAO therapyProgramDAO = (TherapyProgramDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOType.THERAPY_PROGRAM);
    TherapistAvailabilityDAO therapistAvailabilityDAO = (TherapistAvailabilityDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOType.THERAPIST_AVAILABILITY);
    TherapistAvailabilityService therapistAvailabilityBO = (TherapistAvailabilityService) ServiceFactory.getInstance().getService(ServiceFactory.ServiceType.THERAPIST_AVAILABILITY);

    @Override
    public boolean saveSession(TherapySessionDTO dto) {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();

        try {
            Therapist therapist = session.get(Therapist.class, dto.getTherapistId());
            Patient patient = session.get(Patient.class, dto.getPatientId());
            TherapyProgram program = session.get(TherapyProgram.class, dto.getTherapyProgramId());

            if (therapist == null || patient == null || program == null) return false;

            LocalTime newStart = dto.getSessionTime();
            LocalTime newEnd = newStart.plusMinutes(dto.getDuration());

            List<TherapySession> tSessions = therapySessionDAO.findActiveSessionsByTherapist(session, dto.getTherapistId(), dto.getSessionDate());

            for (TherapySession s : tSessions) {
                LocalTime existStart = s.getStart_time();
                LocalTime existEnd = existStart.plusMinutes(s.getDuration());

                if (newStart.isBefore(existEnd) && existStart.isBefore(newEnd)) {
                    throw new ScheduleConflictException("Scheduling Conflict: Therapist '" + therapist.getName() + "' is already booked for a session from " + existStart + " to " + existEnd + "!");
                }
            }

            List<TherapySession> pSessions = therapySessionDAO.findActiveSessionsByPatient(session, dto.getPatientId(), dto.getSessionDate());

            for (TherapySession s : pSessions) {
                LocalTime existStart = s.getStart_time();
                LocalTime existEnd = existStart.plusMinutes(s.getDuration());

                if (newStart.isBefore(existEnd) && existStart.isBefore(newEnd)) {
                    throw new ScheduleConflictException("Scheduling Conflict: Patient already has another session scheduled from " + existStart + " to " + existEnd + "!");
                }
            }

            Duration duration = Duration.ofMinutes(dto.getDuration());
            TherapistAvailability bookedAvailability = bookTimeSlotInternal(dto.getTherapistId(), dto.getSessionDate(), dto.getSessionTime(), duration, session);

            if (bookedAvailability == null) {
                throw new ScheduleConflictException("Scheduling Conflict: The requested time slot is not available in the Therapist's weekly schedule!");
            }

            TherapySession therapySession = new TherapySession();
            therapySession.setSession_id(dto.getSessionId());
            therapySession.setTherapist(therapist);
            therapySession.setPatient(patient);
            therapySession.setTherapy_program(program);
            therapySession.setTherapistAvailability(bookedAvailability);
            therapySession.setSession_date(dto.getSessionDate());
            therapySession.setStart_time(dto.getSessionTime());
            therapySession.setDuration(dto.getDuration());
            therapySession.setStatus(dto.getStatus());

            session.persist(therapySession);

            transaction.commit();
            return true;

        } catch (ScheduleConflictException e) {
            if (transaction != null) transaction.rollback();
            throw e;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            System.out.println(e.getMessage());
            return false;
        } finally {
            session.close();
        }
    }

    @Override
    public boolean updateSession(TherapySessionDTO dto) {

        Therapist therapistOpt = therapistDAO.search(dto.getTherapistId());
        Patient patientOpt = patientDAO.search(dto.getPatientId());
        TherapyProgram programOpt = therapyProgramDAO.search(dto.getTherapyProgramId());
        Optional<TherapySession> optionalSession = therapySessionDAO.findBySessionId(dto.getSessionId());

        if (therapistOpt == null || patientOpt == null || programOpt == null || optionalSession.isEmpty()) {
            return false;
        }

        TherapySession therapySession = optionalSession.get();

        therapySession.setTherapist(therapistOpt);
        therapySession.setPatient(patientOpt);
        therapySession.setTherapy_program(programOpt);

        if (dto.getAvailabilityId() != null) {
            TherapistAvailability availability = therapistAvailabilityDAO.search(dto.getAvailabilityId());

            if (availability != null) {
                therapySession.setTherapistAvailability(availability);
            } else {
                return false;
            }
        }

        therapySession.setSession_date(dto.getSessionDate());
        therapySession.setStart_time(dto.getSessionTime());
        therapySession.setDuration(dto.getDuration());
        therapySession.setStatus(dto.getStatus());

        return therapySessionDAO.update(therapySession);

    }

    @Override
    public boolean deleteSession(String sessionId) {

        Optional<TherapySession> optionalSession = therapySessionDAO.findBySessionId(sessionId);

        if (optionalSession.isEmpty()) return false;
        TherapySession session = optionalSession.get();

        boolean restored = therapistAvailabilityBO.restoreTimeSlot(
                session.getTherapist().getTherapist_id(),
                session.getSession_date(),
                session.getStart_time(),
                Duration.ofMinutes(session.getDuration())
        );

        if (!restored) return false;
        return therapySessionDAO.delete(sessionId);
    }

    @Override
    public List<TherapySessionDTO> getAllSessions() {

        List<TherapySession> sessions = therapySessionDAO.getAll();
        ArrayList<TherapySessionDTO> sessionDtos = new ArrayList<>();

        for (TherapySession session : sessions) {
            TherapySessionDTO dto = new TherapySessionDTO();
            dto.setSessionId(session.getSession_id());
            dto.setTherapistId(session.getTherapist().getTherapist_id());
            dto.setPatientId(session.getPatient().getPatient_id());
            dto.setTherapyProgramId(session.getTherapy_program().getProgramId());
            dto.setAvailabilityId(session.getTherapistAvailability() != null
                    ? session.getTherapistAvailability().getAvailability_id() : null);
            dto.setSessionDate(session.getSession_date());
            dto.setSessionTime(session.getStart_time());
            dto.setDuration(session.getDuration());
            dto.setStatus(session.getStatus());

            sessionDtos.add(dto);
        }

        return sessionDtos;
    }

    @Override
    public List<TherapySessionDTO> findByPatientId(String patientId) {
        List<TherapySession> sessions = therapySessionDAO.findByPatientId(patientId);
        ArrayList<TherapySessionDTO> sessionDtos = new ArrayList<>();

        for (TherapySession session : sessions) {
            TherapySessionDTO dto = new TherapySessionDTO();
            dto.setSessionId(session.getSession_id());
            dto.setTherapistId(session.getTherapist().getTherapist_id());
            dto.setPatientId(session.getPatient().getPatient_id());
            dto.setTherapyProgramId(session.getTherapy_program().getProgramId());
            dto.setAvailabilityId(session.getTherapistAvailability() != null
                    ? session.getTherapistAvailability().getAvailability_id() : null);
            dto.setSessionDate(session.getSession_date());
            dto.setSessionTime(session.getStart_time());
            dto.setDuration(session.getDuration());
            dto.setStatus(session.getStatus());

            sessionDtos.add(dto);
        }

        return sessionDtos;
    }

    @Override
    public String getNextSessionPK() {
        Optional<String> lastPkOpt = therapySessionDAO.getLastPK();

        if (lastPkOpt.isPresent()) {
            String lastPk = lastPkOpt.get();
            String numericPart = lastPk.substring(2);
            int currentId = Integer.parseInt(numericPart);
            int nextId = currentId + 1;
            return String.format("TS%03d", nextId);
        }

        return "TS001";
    }

    private TherapistAvailability bookTimeSlotInternal(String therapistId, LocalDate date, LocalTime startTime, Duration sessionDuration, Session session) {
        String hql = "FROM TherapistAvailability ta WHERE ta.therapist.therapist_id = :id AND ta.available_date = :date AND ta.is_available = true";
        List<TherapistAvailability> availabilityList = session.createQuery(hql, TherapistAvailability.class)
                .setParameter("id", therapistId)
                .setParameter("date", date)
                .getResultList();

        LocalTime endTime = startTime.plus(sessionDuration);

        for (TherapistAvailability availability : availabilityList) {
            LocalTime slotStart = availability.getStart_time();
            LocalTime slotEnd = availability.getEnd_time();

            if ((startTime.equals(slotStart) || startTime.isAfter(slotStart)) &&
                    (endTime.equals(slotEnd) || endTime.isBefore(slotEnd))) {

                availability.set_available(false);
                session.merge(availability);
                try {

                    if (slotStart.isBefore(startTime)) {
                        createNewAvailability(availability, slotStart, startTime, session);
                    }

                    if (slotEnd.isAfter(endTime)) {
                        createNewAvailability(availability, endTime, slotEnd, session);
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                return availability;
            }
        }
        return null;
    }

    private void createNewAvailability(TherapistAvailability oldRef, LocalTime start, LocalTime end, Session session) {
        TherapistAvailability newSlot = new TherapistAvailability();
        newSlot.setAvailability_id(getNextIdForInternalUse(session));
        newSlot.setTherapist(oldRef.getTherapist());
        newSlot.setAvailable_date(oldRef.getAvailable_date());
        newSlot.setStart_time(start);
        newSlot.setEnd_time(end);
        newSlot.set_available(true);

        session.persist(newSlot);
    }

    private String getNextIdForInternalUse(Session session) {
        String lastId = session.createQuery("SELECT a.availability_id FROM TherapistAvailability a ORDER BY a.availability_id DESC", String.class)
                .setMaxResults(1).uniqueResult();
        if (lastId != null) {
            int id = Integer.parseInt(lastId.substring(1)) + 1;
            return String.format("A%03d", id);
        }
        return "A001";
    }
}
