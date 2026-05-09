package lk.ijse.serenitymentalhealththerepycentersystem.service.custom.Impl;

import lk.ijse.serenitymentalhealththerepycentersystem.dao.DAOFactory;
import lk.ijse.serenitymentalhealththerepycentersystem.dao.custom.TherapistDAO;
import lk.ijse.serenitymentalhealththerepycentersystem.dao.custom.TherapistProgramDAO;
import lk.ijse.serenitymentalhealththerepycentersystem.dao.custom.TherapyProgramDAO;
import lk.ijse.serenitymentalhealththerepycentersystem.dto.TherapistProgramDTO;
import lk.ijse.serenitymentalhealththerepycentersystem.entity.Therapist;
import lk.ijse.serenitymentalhealththerepycentersystem.entity.TherapistProgram;
import lk.ijse.serenitymentalhealththerepycentersystem.entity.TherapistProgramId;
import lk.ijse.serenitymentalhealththerepycentersystem.entity.TherapyProgram;
import lk.ijse.serenitymentalhealththerepycentersystem.service.custom.TherapistProgramService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TherapistProgramServiceImpl implements TherapistProgramService {

    TherapistProgramDAO therapistProgramDAO = (TherapistProgramDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOType.THERAPIST_PROGRAM);
    TherapistDAO therapistDAO = (TherapistDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOType.THERAPIST);
    TherapyProgramDAO therapyProgramDAO = (TherapyProgramDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOType.THERAPY_PROGRAM);

    @Override
    public boolean saveTherapistProgram(String therapistId, String programId) {
        Therapist therapistOpt = therapistDAO.search(therapistId);
        TherapyProgram programOpt = therapyProgramDAO.search(programId);

        if (therapistOpt == null || programOpt == null) {
            return false;
        }
        if (therapistProgramDAO.findById(therapistId, programId).isPresent()) {
            return false;
        }

        TherapistProgram therapistProgram = new TherapistProgram();
        therapistProgram.setId(new TherapistProgramId(therapistId, programId));
        therapistProgram.setTherapist(therapistOpt);
        therapistProgram.setTherapy_program(programOpt);

        return therapistProgramDAO.save(therapistProgram);
    }

    @Override
    public boolean updateTherapistProgram(String therapistId, String programId) {
        Therapist therapistOpt = therapistDAO.search(therapistId);
        TherapyProgram programOpt = therapyProgramDAO.search(programId);

        if (therapistOpt == null || programOpt == null) {
            return false;
        }


        TherapistProgram entity = new TherapistProgram(
                new TherapistProgramId(therapistId, programId),
                therapistOpt,
                programOpt
        );

        return therapistProgramDAO.update(entity);
    }

    @Override
    public boolean deleteTherapistProgram(String therapistId, String programId) {
        return therapistProgramDAO.delete(therapistId, programId);

    }

    @Override
    public TherapistProgramDTO findById(String therapistId, String programId) {
        Optional<TherapistProgram> result = therapistProgramDAO.findById(therapistId, programId);
        if (result.isPresent()) {
            TherapistProgram entity = result.get();
            return new TherapistProgramDTO(
                    entity.getId().getTherapistId(),
                    entity.getId().getProgramId(),
                    entity.getTherapy_program().getProgramName()
            );
        } else {
            return null;
        }    }

    @Override
    public List<TherapistProgramDTO> getAllTherapistPrograms() {
        List<TherapistProgram> programs = therapistProgramDAO.getAll();
        List<TherapistProgramDTO> dtos = new ArrayList<>();

        for (TherapistProgram entity : programs) {
            TherapistProgramDTO dto = new TherapistProgramDTO(
                    entity.getId().getTherapistId(),
                    entity.getId().getProgramId(),
                    entity.getTherapy_program().getProgramName()
            );
            dtos.add(dto);
        }

        return dtos;
    }

    @Override
    public List<TherapistProgramDTO> findByProgramName(String name) {
        List<TherapistProgram> programs = therapistProgramDAO.findByProgramName(name);
        List<TherapistProgramDTO> dtos = new ArrayList<>();

        for (TherapistProgram entity : programs) {
            TherapistProgramDTO dto = new TherapistProgramDTO(
                    entity.getId().getTherapistId(),
                    entity.getId().getProgramId(),
                    entity.getTherapy_program().getProgramName()
            );
            dtos.add(dto);
        }

        return dtos;
    }

    @Override
    public List<TherapistProgramDTO> getTherapistProgramsByTherapist(String id) {
        List<TherapistProgram> programs = therapistProgramDAO.findByTherapist(id);
        List<TherapistProgramDTO> dtos = new ArrayList<>();

        for (TherapistProgram entity : programs) {
            TherapistProgramDTO dto = new TherapistProgramDTO(
                    entity.getId().getTherapistId(),
                    entity.getId().getProgramId(),
                    entity.getTherapy_program().getProgramName()
            );
            dtos.add(dto);
        }

        return dtos;
    }

    @Override
    public List<TherapistProgramDTO> getTherapistProgramsByTherapistId(String therapistId) {
        List<TherapistProgram> programs = therapistProgramDAO.findByTherapistId(therapistId);
        List<TherapistProgramDTO> dtos = new ArrayList<>();

        for (TherapistProgram entity : programs) {
            TherapistProgramDTO dto = new TherapistProgramDTO(
                    entity.getId().getTherapistId(),
                    entity.getId().getProgramId(),
                    entity.getTherapy_program().getProgramName()
            );
            dtos.add(dto);
        }

        return dtos;
    }

    @Override
    public List<TherapistProgramDTO> getTherapistProgramsByProgramId(String programId) {
        List<TherapistProgram> programs = therapistProgramDAO.findByProgramId(programId);
        List<TherapistProgramDTO> dtos = new ArrayList<>();

        for (TherapistProgram entity : programs) {
            TherapistProgramDTO dto = new TherapistProgramDTO(
                    entity.getId().getTherapistId(),
                    entity.getId().getProgramId(),
                    entity.getTherapy_program().getProgramName()
            );
            dtos.add(dto);
        }

        return dtos;
    }
}
