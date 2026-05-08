package lk.ijse.serenitymentalhealththerepycentersystem.service.custom.Impl;

import lk.ijse.serenitymentalhealththerepycentersystem.dao.DAOFactory;
import lk.ijse.serenitymentalhealththerepycentersystem.dao.custom.TherapistAvailabilityDAO;
import lk.ijse.serenitymentalhealththerepycentersystem.dao.custom.TherapistDAO;
import lk.ijse.serenitymentalhealththerepycentersystem.dto.TherapistAvailabilityDTO;
import lk.ijse.serenitymentalhealththerepycentersystem.entity.Therapist;
import lk.ijse.serenitymentalhealththerepycentersystem.entity.TherapistAvailability;
import lk.ijse.serenitymentalhealththerepycentersystem.service.custom.TherapistAvailabilityService;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TherapistAvailabilityServiceImpl implements TherapistAvailabilityService {

    TherapistAvailabilityDAO availDAO = (TherapistAvailabilityDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOType.THERAPIST_AVAILABILITY);
    TherapistDAO therapistDAO = (TherapistDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOType.THERAPIST);

    @Override
    public boolean saveAvailability(TherapistAvailabilityDTO dto) throws Exception {
        Therapist therapist = therapistDAO.search(dto.getTherapistId());
        if (therapist == null) return false;

        TherapistAvailability entity = new TherapistAvailability();
        entity.setAvailability_id(dto.getAvailabilityId());
        entity.setTherapist(therapist);
        entity.setAvailable_date(dto.getAvailableDate());
        entity.setStart_time(dto.getStartTime());
        entity.setEnd_time(dto.getEndTime());
        entity.set_available(dto.isAvailable());

        return availDAO.save(entity);
    }

    @Override
    public boolean updateAvailability(TherapistAvailabilityDTO dto) throws Exception {

        Therapist therapist = therapistDAO.search(dto.getTherapistId());
        if (therapist == null) return false;

        TherapistAvailability entity = new TherapistAvailability();
        entity.setAvailability_id(dto.getAvailabilityId());
        entity.setTherapist(therapist);
        entity.setAvailable_date(dto.getAvailableDate());
        entity.setStart_time(dto.getStartTime());
        entity.setEnd_time(dto.getEndTime());

        entity.set_available(dto.isAvailable());

        return availDAO.update(entity);
    }

    @Override
    public boolean deleteAvailability(String id) throws Exception {
        return availDAO.delete(id);
    }

    @Override
    public List<TherapistAvailabilityDTO> getAllAvailability() throws Exception {
        List<TherapistAvailability> entities = availDAO.getAll();
        List<TherapistAvailabilityDTO> dtoList = new ArrayList<>();

        for (TherapistAvailability a : entities) {
            dtoList.add(new TherapistAvailabilityDTO(
                    a.getAvailability_id(),
                    a.getTherapist().getTherapist_id(),
                    a.getTherapist().getName(),
                    a.getAvailable_date(),
                    a.getStart_time(),
                    a.getEnd_time(),
                    a.is_available()
            ));
        }
        return dtoList;
    }

    @Override
    public String getNextAvailabilityId() throws Exception {
        Optional<String> lastId = availDAO.getLastPK();
        if (lastId.isPresent()) {
            int id = Integer.parseInt(lastId.get().substring(1)) + 1;
            return String.format("A%03d", id);
        }
        return "A001";
    }

    @Override
    public List<TherapistAvailabilityDTO> findAvailabilityByTherapistName(String name) throws Exception {
        List<TherapistAvailability> entities = availDAO.findByTherapistName(name);
        List<TherapistAvailabilityDTO> dtoList = new ArrayList<>();

        for (TherapistAvailability a : entities) {
            dtoList.add(new TherapistAvailabilityDTO(
                    a.getAvailability_id(),
                    a.getTherapist().getTherapist_id(),
                    a.getTherapist().getName(),
                    a.getAvailable_date(),
                    a.getStart_time(),
                    a.getEnd_time(),
                    a.is_available()
            ));
        }
        return dtoList;
    }

    @Override
    public List<TherapistAvailabilityDTO> getAvailableSlots(String therapistId, LocalDate date) throws Exception {

        List<TherapistAvailability> entities = availDAO.getAvailableSlots(therapistId, date);

        List<TherapistAvailabilityDTO> dtoList = new ArrayList<>();

        for (TherapistAvailability a : entities) {
            dtoList.add(new TherapistAvailabilityDTO(
                    a.getAvailability_id(),
                    a.getTherapist().getTherapist_id(),
                    null,
                    a.getAvailable_date(),
                    a.getStart_time(),
                    a.getEnd_time(),
                    a.is_available()
            ));
        }
        return dtoList;
    }

    private List<String> generateSlots(LocalTime start, LocalTime end) {
        List<String> slots = new ArrayList<>();
        LocalTime temp = start;
        while (temp.isBefore(end)) {
            LocalTime next = temp.plusHours(1);
            if (next.isAfter(end)) next = end;
            slots.add(temp + " - " + next);
            temp = next;
        }
        return slots;
    }

    public boolean bookTimeSlot(String therapistId, LocalDate date, LocalTime startTime, Duration sessionDuration) {
        List<TherapistAvailability> availabilityList = availDAO.findByTherapistAndDate(therapistId, date);

        if (availabilityList.isEmpty()) return false;

        Duration slotDuration = Duration.ofMinutes(30);
        int requiredSlotCount = (int) (sessionDuration.toMinutes() / slotDuration.toMinutes());

        for (TherapistAvailability availability : availabilityList) {
            List<String> availableSlots = availability.getAvailable_slots();

            // Find the index of the slot starting at the given startTime
            int startIndex = -1;
            for (int i = 0; i < availableSlots.size(); i++) {
                String slotStart = availableSlots.get(i).split("-")[0];
                if (LocalTime.parse(slotStart).equals(startTime)) {
                    startIndex = i;
                    break;
                }
            }

            if (startIndex != -1 && startIndex + requiredSlotCount <= availableSlots.size()) {
                List<String> subList = availableSlots.subList(startIndex, startIndex + requiredSlotCount);

                if (areConsecutive(subList, slotDuration)) {
                    // Book these slots
                    availability.getAvailable_slots().removeAll(subList);

                    // Mark as unavailable if no slots left
                    if (availability.getAvailable_slots().isEmpty()) {
                        availability.set_available(false);
                    }

                    return therapistAvailabilityDAO.update(availability);
                }
            }
        }
        return false;
    }

}
