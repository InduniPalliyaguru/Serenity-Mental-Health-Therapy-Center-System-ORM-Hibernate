package lk.ijse.serenitymentalhealththerepycentersystem.service.custom.Impl;

import lk.ijse.serenitymentalhealththerepycentersystem.dao.DAOFactory;
import lk.ijse.serenitymentalhealththerepycentersystem.dao.custom.TherapyProgramDAO;
import lk.ijse.serenitymentalhealththerepycentersystem.dto.TherapyProgramDTO;
import lk.ijse.serenitymentalhealththerepycentersystem.entity.TherapyProgram;
import lk.ijse.serenitymentalhealththerepycentersystem.service.custom.TherapyProgramService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TherapyProgramServiceImpl implements TherapyProgramService {

    TherapyProgramDAO programDAO = (TherapyProgramDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOType.THERAPY_PROGRAM);

    @Override
    public boolean saveProgram(TherapyProgramDTO dto) {
        return programDAO.save(new TherapyProgram(dto.getProgramId(), dto.getProgramName(), dto.getDuration(), dto.getFee(), dto.getDescription()));
    }

    @Override
    public List<TherapyProgramDTO> getAllPrograms() {
        List<TherapyProgram> all = programDAO.getAll();
        List<TherapyProgramDTO> dtoList = new ArrayList<>();
        for (TherapyProgram p : all) {
            dtoList.add(new TherapyProgramDTO(p.getProgramId(), p.getProgramName(), p.getDuration(), p.getFee(), p.getDescription()));
        }
        return dtoList;
    }

    @Override
    public TherapyProgramDTO searchProgram(String id) {
        TherapyProgram entity = programDAO.search(id);

        if (entity != null) {
            return new TherapyProgramDTO(
                    entity.getProgramId(),
                    entity.getProgramName(),
                    entity.getDuration(),
                    entity.getFee(),
                    entity.getDescription()
            );
        }

        return null;
    }

    @Override
    public boolean updateProgram(TherapyProgramDTO dto) {
        return programDAO.update(new TherapyProgram(dto.getProgramId(), dto.getProgramName(), dto.getDuration(), dto.getFee(), dto.getDescription()));
    }

    @Override
    public boolean deleteProgram(String id) {
        return programDAO.delete(id);
    }

    @Override
    public String getNextTherapyProgramPK() {
        Optional<String> lastPkOpt = programDAO.getLastPK();

        if (lastPkOpt.isPresent()) {
            String lastPk = lastPkOpt.get();
            if (lastPk.startsWith("MT")) {
                String numericPart = lastPk.substring(2);

                try {
                    int currentId = Integer.parseInt(numericPart);
                    int nextId = currentId + 1;

                    return String.format("MT%03d", nextId);
                } catch (NumberFormatException e) {
                    System.err.println("Error parsing numeric part of primary key: " + numericPart);
                    return "MT1001";
                }
            }
        }

        return "MT1001";
    }

    @Override
    public TherapyProgramDTO findByName(String name) throws Exception {
        TherapyProgram p = programDAO.findByName(name);
        if (p != null) {
            return new TherapyProgramDTO(
                    p.getProgramId(),
                    p.getProgramName(),
                    p.getDescription(),
                    p.getFee(),
                    p.getDuration()
            );
        }
        return null;
    }

    @Override
    public ArrayList<TherapyProgramDTO> findTherapyProgramByName(String name) {
        List<TherapyProgram> programs = programDAO.findByTherapyProgramName(name);
        ArrayList<TherapyProgramDTO> dtos = new ArrayList<>();

        for (TherapyProgram program : programs) {
            dtos.add(new TherapyProgramDTO(
                    program.getProgramId(),
                    program.getProgramName(),
                    program.getDuration(),
                    program.getFee(),
                    program.getDescription()
            ));
        }
        return dtos;
    }

}
