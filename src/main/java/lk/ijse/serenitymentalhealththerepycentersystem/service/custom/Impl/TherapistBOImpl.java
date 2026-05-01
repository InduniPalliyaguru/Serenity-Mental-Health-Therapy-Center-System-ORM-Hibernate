package lk.ijse.serenitymentalhealththerepycentersystem.service.custom.Impl;

import lk.ijse.serenitymentalhealththerepycentersystem.dao.DAOFactory;
import lk.ijse.serenitymentalhealththerepycentersystem.dao.custom.TherapistDAO;
import lk.ijse.serenitymentalhealththerepycentersystem.dao.custom.TherapyProgramDAO;
import lk.ijse.serenitymentalhealththerepycentersystem.dto.TherapistDTO;
import lk.ijse.serenitymentalhealththerepycentersystem.dto.tm.TherapistProgramTM;
import lk.ijse.serenitymentalhealththerepycentersystem.entity.Therapist;
import lk.ijse.serenitymentalhealththerepycentersystem.entity.TherapistProgram;
import lk.ijse.serenitymentalhealththerepycentersystem.entity.TherapistProgramId;
import lk.ijse.serenitymentalhealththerepycentersystem.entity.TherapyProgram;
import lk.ijse.serenitymentalhealththerepycentersystem.service.custom.TherapistBO;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TherapistBOImpl implements TherapistBO {

    TherapistDAO therapistDAO = (TherapistDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOType.THERAPIST);
    TherapyProgramDAO programDAO = (TherapyProgramDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOType.THERAPY_PROGRAM);

    @Override
    public boolean updateTherapist(TherapistDTO dto) {
        Therapist therapist = therapistDAO.search(dto.getTherapistId());

        if (therapist != null) {

            therapist.setName(dto.getName());
            therapist.setEmail(dto.getEmail());
            therapist.setPhone(dto.getPhone());
            therapist.setSpecialization(dto.getSpecialization());

            therapist.getTherapistPrograms().clear();

            if (dto.getAssignedPrograms() != null) {
                for (TherapistProgramTM tm : dto.getAssignedPrograms()) {

                    TherapyProgram programEntity = programDAO.search(tm.getTherapyProgramId());

                    if (programEntity != null) {

                        TherapistProgramId compositeId = new TherapistProgramId(
                                therapist.getTherapist_id(),
                                programEntity.getProgramId()
                        );

                        TherapistProgram bridge = new TherapistProgram();
                        bridge.setId(compositeId);
                        bridge.setTherapist(therapist);
                        bridge.setTherapy_program(programEntity);

                        therapist.getTherapistPrograms().add(bridge);
                    }
                }
            }

            return therapistDAO.update(therapist);
        }
        return false;
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
        Therapist th = therapistDAO.search(id);

        if (th != null) {

            TherapistDTO dto = new TherapistDTO();
            dto.setTherapistId(th.getTherapist_id());
            dto.setName(th.getName());
            dto.setEmail(th.getEmail());
            dto.setPhone(th.getPhone());
            dto.setSpecialization(th.getSpecialization());

            List<TherapistProgramTM> tmList = new ArrayList<>();

            if (th.getTherapistPrograms() != null) {
                for (TherapistProgram tp : th.getTherapistPrograms()) {
                    tmList.add(new TherapistProgramTM(
                            tp.getTherapy_program().getProgramId(),
                            tp.getTherapy_program().getProgramName()
                    ));
                }
            }

            dto.setAssignedPrograms(tmList);

            return dto;
        }
        return null;
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

    @Override
    public boolean saveTherapistWithPrograms(TherapistDTO dto) throws Exception {
        Therapist therapist = new Therapist();
        therapist.setTherapist_id(dto.getTherapistId());
        therapist.setName(dto.getName());
        therapist.setEmail(dto.getEmail());
        therapist.setPhone(dto.getPhone());
        therapist.setSpecialization(dto.getSpecialization());

        List<TherapistProgram> list = new ArrayList<>();

        if (dto.getAssignedPrograms() != null) {
            for (TherapistProgramTM tm : dto.getAssignedPrograms()) {
                TherapyProgram programEntity = programDAO.search(tm.getTherapyProgramId());

                if (programEntity != null) {

                    TherapistProgram bridge = new TherapistProgram();

                    TherapistProgramId compositeId = new TherapistProgramId(
                            therapist.getTherapist_id(),
                            programEntity.getProgramId()
                    );

                    bridge.setId(compositeId);
                    bridge.setTherapist(therapist);
                    bridge.setTherapy_program(programEntity);

                    list.add(bridge);
                }
            }
        }

        // මෙතැනදී අනිවාර්යයෙන්ම සකස් කරගත් list එක set කරන්න
        therapist.setTherapistPrograms(list);

        return therapistDAO.save(therapist);
    }
}
