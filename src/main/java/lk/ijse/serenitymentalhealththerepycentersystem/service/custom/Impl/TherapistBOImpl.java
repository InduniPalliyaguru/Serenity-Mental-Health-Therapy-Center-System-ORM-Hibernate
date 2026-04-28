package lk.ijse.serenitymentalhealththerepycentersystem.service.custom.Impl;

import lk.ijse.serenitymentalhealththerepycentersystem.dao.DAOFactory;
import lk.ijse.serenitymentalhealththerepycentersystem.dao.custom.TherapistDAO;
import lk.ijse.serenitymentalhealththerepycentersystem.dto.TherapistDTO;
import lk.ijse.serenitymentalhealththerepycentersystem.entity.Therapist;
import lk.ijse.serenitymentalhealththerepycentersystem.service.custom.TherapistBO;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TherapistBOImpl implements TherapistBO {

    TherapistDAO therapistDAO = (TherapistDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOType.THERAPIST);

    @Override
    public boolean saveTherapist(TherapistDTO dto) {
        Therapist therapist = new Therapist();
        therapist.setTherapist_id(dto.getTherapistId());
        therapist.setName(dto.getName());
        therapist.setEmail(dto.getEmail());
        therapist.setPhone(dto.getPhone());
        therapist.setSpecialization(dto.getSpecialization());

        return therapistDAO.save(therapist);
    }

    @Override
    public boolean updateTherapist(TherapistDTO dto) {
        Therapist therapist = new Therapist();
        therapist.setTherapist_id(dto.getTherapistId());
        therapist.setName(dto.getName());
        therapist.setEmail(dto.getEmail());
        therapist.setPhone(dto.getPhone());
        therapist.setSpecialization(dto.getSpecialization());

        return therapistDAO.update(therapist);
    }

    @Override
    public boolean deleteTherapist(String id) {
        return therapistDAO.delete(id);
    }

    @Override
    public ArrayList<TherapistDTO> getAllTherapists() {
        List<Therapist> therapists = therapistDAO.getAll();

        ArrayList<TherapistDTO> therapistDTOS = new ArrayList<>();
        for (Therapist therapist : therapists) {

            TherapistDTO therapistDTO = new TherapistDTO();
            therapistDTO.setTherapistId(therapist.getTherapist_id());
            therapistDTO.setName(therapist.getName());
            therapistDTO.setEmail(therapist.getEmail());
            therapistDTO.setPhone(therapist.getPhone());
            therapistDTO.setSpecialization(therapist.getSpecialization());

            therapistDTOS.add(therapistDTO);
        }
        return therapistDTOS;
    }

    @Override
    public ArrayList<TherapistDTO> findByTherapistName(String name) {
        List<Therapist> therapists = therapistDAO.findByTherapistName(name);
        ArrayList<TherapistDTO> therapistDtos = new ArrayList<>();

        for (Therapist therapist : therapists) {

            TherapistDTO therapistDTO = new TherapistDTO();

            therapistDTO.setTherapistId(therapist.getTherapist_id());
            therapistDTO.setName(therapist.getName());
            therapistDTO.setEmail(therapist.getEmail());
            therapistDTO.setPhone(therapist.getPhone());
            therapistDTO.setSpecialization(therapist.getSpecialization());

            therapistDtos.add(therapistDTO);
        }

        return therapistDtos;
    }

    @Override
    public TherapistDTO findByTherapistId(String id) {
        Therapist tp = therapistDAO.search(id);
        if (tp == null) {
            return null;
        }

        TherapistDTO therapistDto = new TherapistDTO();

        therapistDto.setTherapistId(tp.getTherapist_id());
        therapistDto.setName(tp.getName());
        therapistDto.setEmail(tp.getEmail());
        therapistDto.setPhone(tp.getPhone());
        therapistDto.setSpecialization(tp.getSpecialization());

        return therapistDto;
    }

    @Override
    public String getNextTherapistPK() {
        Optional<String> lastPkOpt = therapistDAO.getLastPK();

        if (lastPkOpt.isPresent()) {
            String lastPk = lastPkOpt.get();
            String numericPart = lastPk.substring(1);
            int currentId = Integer.parseInt(numericPart);
            int nextId = currentId + 1;

            return String.format("T%03d", nextId);
        }

        return "T001";
    }
}
