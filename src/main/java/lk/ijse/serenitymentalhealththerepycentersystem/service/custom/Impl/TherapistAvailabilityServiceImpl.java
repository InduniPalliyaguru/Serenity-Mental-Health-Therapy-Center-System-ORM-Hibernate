package lk.ijse.serenitymentalhealththerepycentersystem.service.custom.Impl;

import lk.ijse.serenitymentalhealththerepycentersystem.dao.DAOFactory;
import lk.ijse.serenitymentalhealththerepycentersystem.dao.custom.TherapistAvailabilityDAO;
import lk.ijse.serenitymentalhealththerepycentersystem.dao.custom.TherapistDAO;
import lk.ijse.serenitymentalhealththerepycentersystem.dto.TherapistAvailabilityDTO;
import lk.ijse.serenitymentalhealththerepycentersystem.entity.Therapist;
import lk.ijse.serenitymentalhealththerepycentersystem.entity.TherapistAvailability;
import lk.ijse.serenitymentalhealththerepycentersystem.service.custom.TherapistAvailabilityService;

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
        entity.setAvailable_slots(generateSlots(dto.getStartTime(), dto.getEndTime()));
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

        entity.setAvailable_slots(generateSlots(dto.getStartTime(), dto.getEndTime()));
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
                    a.getAvailable_slots(),
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
                    a.getAvailable_slots(),
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
}
