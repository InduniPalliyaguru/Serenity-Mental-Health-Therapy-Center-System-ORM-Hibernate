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
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class TherapistAvailabilityServiceImpl implements TherapistAvailabilityService {

    TherapistAvailabilityDAO availDAO = (TherapistAvailabilityDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOType.THERAPIST_AVAILABILITY);
    TherapistDAO therapistDAO = (TherapistDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOType.THERAPIST);

    @Override
    public boolean saveAvailability(TherapistAvailabilityDTO dto) {
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
    public boolean updateAvailability(TherapistAvailabilityDTO dto) {

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
    public boolean deleteAvailability(String id) {
        return availDAO.delete(id);
    }

    @Override
    public List<TherapistAvailabilityDTO> getAllAvailability() {
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
                    a.getAvailable_slots(),
                    a.is_available()
            ));
        }
        return dtoList;
    }

    @Override
    public String getNextAvailabilityId() {
        Optional<String> lastId = availDAO.getLastPK();
        if (lastId.isPresent()) {
            int id = Integer.parseInt(lastId.get().substring(1)) + 1;
            return String.format("A%03d", id);
        }
        return "A001";
    }

    @Override
    public List<TherapistAvailabilityDTO> findAvailabilityByTherapistName(String name) {
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
                    a.getAvailable_slots(),
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
                    a.getTherapist().getName(),
                    a.getAvailable_date(),
                    a.getStart_time(),
                    a.getEnd_time(),
                    a.getAvailable_slots(),
                    a.is_available()
            ));
        }
        return dtoList;
    }

    @Override
    public List<TherapistAvailabilityDTO> findByTherapistAndDate(String therapistId, LocalDate date) {
        List<TherapistAvailability> entities = availDAO.findByTherapistAndDate(therapistId, date);

        if (entities.isEmpty()) return new ArrayList<>();

        List<TherapistAvailabilityDTO> dtos = new ArrayList<>();
        for (TherapistAvailability entity : entities) {
            dtos.add(new TherapistAvailabilityDTO(
                    entity.getAvailability_id(),
                    entity.getTherapist().getTherapist_id(),
                    entity.getTherapist().getName(),
                    entity.getAvailable_date(),
                    entity.getStart_time(),
                    entity.getEnd_time(),
                    entity.getAvailable_slots(),
                    entity.is_available()
            ));
        }

        return dtos;
    }

    public boolean bookTimeSlot(String therapistId, LocalDate date, LocalTime startTime, Duration sessionDuration) {
        List<TherapistAvailability> availabilityList = availDAO.findByTherapistAndDate(therapistId, date);

        if (availabilityList.isEmpty()) return false;

        Duration slotDuration = Duration.ofMinutes(30);
        int requiredSlotCount = (int) (sessionDuration.toMinutes() / slotDuration.toMinutes());

        for (TherapistAvailability availability : availabilityList) {
            List<String> availableSlots = availability.getAvailable_slots();

            int startIndex = -1;

            for (int i = 0; i < availableSlots.size(); i++) {

                String slotStartStr = availableSlots.get(i).split("-")[0].trim();

                LocalTime slotStart = LocalTime.parse(slotStartStr);

                if (slotStart.equals(startTime)) {
                    startIndex = i;
                    break;
                }
            }

            if (startIndex != -1 && startIndex + requiredSlotCount <= availableSlots.size()) {
                List<String> subList = availableSlots.subList(startIndex, startIndex + requiredSlotCount);

                if (areConsecutive(subList, slotDuration)) {

                    availability.getAvailable_slots().removeAll(subList);

                    if (availability.getAvailable_slots().isEmpty()) {
                        availability.set_available(false);
                    }

                    return availDAO.update(availability);
                }
            }
        }
        return false;
    }

    private boolean areConsecutive(List<String> slots, Duration slotDuration) {
        for (int i = 0; i < slots.size() - 1; i++) {
            String currentEnd = slots.get(i).split("-")[1];
            String nextStart = slots.get(i + 1).split("-")[0];

            if (!LocalTime.parse(currentEnd).equals(LocalTime.parse(nextStart))) {
                return false;
            }
        }

        String firstStart = slots.get(0).split("-")[0];
        String lastEnd = slots.get(slots.size() - 1).split("-")[1];

        Duration totalDuration = Duration.between(LocalTime.parse(firstStart), LocalTime.parse(lastEnd));
        return totalDuration.equals(slotDuration.multipliedBy(slots.size()));
    }

    public boolean restoreTimeSlot(String therapistId, LocalDate date, LocalTime startTime, Duration sessionDuration) {
        List<TherapistAvailability> availabilityList = availDAO.findByTherapistAndDate(therapistId, date);

        if (availabilityList.isEmpty()) return false;

        Duration slotDuration = Duration.ofMinutes(30);
        int slotCount = (int) (sessionDuration.toMinutes() / slotDuration.toMinutes());

        List<String> slotsToRestore = new ArrayList<>();
        LocalTime current = startTime;
        for (int i = 0; i < slotCount; i++) {
            String slot = current + "-" + current.plus(slotDuration);
            slotsToRestore.add(slot);
            current = current.plus(slotDuration);
        }

        for (TherapistAvailability availability : availabilityList) {
            if (availability.getAvailable_date().equals(date)) {
                List<String> existingSlots = availability.getAvailable_slots();

                existingSlots.addAll(slotsToRestore);
                existingSlots.sort(Comparator.comparing(slot -> LocalTime.parse(slot.split("-")[0])));

                availability.set_available(true);
                return availDAO.update(availability);
            }
        }
        return false;
    }


}
