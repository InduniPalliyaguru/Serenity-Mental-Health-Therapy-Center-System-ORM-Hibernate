package lk.ijse.serenitymentalhealththerepycentersystem.service.custom.Impl;

import lk.ijse.serenitymentalhealththerepycentersystem.dao.DAOFactory;
import lk.ijse.serenitymentalhealththerepycentersystem.dao.custom.PatientDAO;
import lk.ijse.serenitymentalhealththerepycentersystem.dto.PatientDTO;
import lk.ijse.serenitymentalhealththerepycentersystem.entity.Patient;
import lk.ijse.serenitymentalhealththerepycentersystem.exception.RegistrationException;
import lk.ijse.serenitymentalhealththerepycentersystem.service.custom.PatientService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PatientServiceImpl implements PatientService {

    PatientDAO patientDAO = (PatientDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOType.PATIENT);

    @Override
    public boolean savePatient(PatientDTO dto) {

        Patient duplicatePatient = patientDAO.search(dto.getPatientId());
        if (duplicatePatient != null) {
            throw new RegistrationException("Registration Failed: Patient ID '" + dto.getPatientId() + "' already exists!");
        }

        Patient patient = new Patient();
        patient.setPatient_id(dto.getPatientId());
        patient.setName(dto.getName());
        patient.setEmail(dto.getEmail());
        patient.setPhone(dto.getPhone());
        patient.setAddress(dto.getAddress());
        patient.setMedical_history(dto.getMedicalHistory());

        return patientDAO.save(patient);
    }

    @Override
    public boolean updatePatient(PatientDTO dto) {
        Patient patient = new Patient();
        patient.setPatient_id(dto.getPatientId());
        patient.setName(dto.getName());
        patient.setEmail(dto.getEmail());
        patient.setPhone(dto.getPhone());
        patient.setAddress(dto.getAddress());
        patient.setMedical_history(dto.getMedicalHistory());

        return patientDAO.update(patient);
    }

    @Override
    public boolean deletePatient(String id) {
        return patientDAO.delete(id);
    }

    @Override
    public ArrayList<PatientDTO> getAllPatients() {
        List<Patient> patients = patientDAO.getAll();

        ArrayList<PatientDTO> patientDTOs = new ArrayList<>();
        for (Patient patient : patients) {
            PatientDTO patientDto = new PatientDTO();
            patientDto.setPatientId(patient.getPatient_id());
            patientDto.setName(patient.getName());
            patientDto.setEmail(patient.getEmail());
            patientDto.setPhone(patient.getPhone());
            patientDto.setAddress(patient.getAddress());
            patientDto.setMedicalHistory(patient.getMedical_history());
            patientDTOs.add(patientDto);
        }
        return patientDTOs;
    }

    @Override
    public ArrayList<PatientDTO> findByPatientName(String name) {
        List<Patient> patients = patientDAO.findByPatientName(name);
        ArrayList<PatientDTO> patientDTOs = new ArrayList<>();

        for (Patient patient : patients) {
            PatientDTO patientDto = new PatientDTO();
            patientDto.setPatientId(patient.getPatient_id());
            patientDto.setName(patient.getName());
            patientDto.setEmail(patient.getEmail());
            patientDto.setPhone(patient.getPhone());
            patientDto.setAddress(patient.getAddress());
            patientDto.setMedicalHistory(patient.getMedical_history());
            patientDTOs.add(patientDto);
        }

        return patientDTOs;
    }

    @Override
    public PatientDTO findPatientByID(String id) {
        Patient patient = patientDAO.search(id);

        if (patient != null) {
            return new PatientDTO(
                    patient.getPatient_id(),
                    patient.getName(),
                    patient.getEmail(),
                    patient.getPhone(),
                    patient.getAddress(),
                    patient.getMedical_history()
            );
        }

        return null;
    }

    @Override
    public String getNextPatientPK() throws Exception {
        Optional<String> lastPkOpt = patientDAO.getLastPK();

        if (lastPkOpt.isPresent()) {
            String lastPk = lastPkOpt.get();
            String numericPart = lastPk.substring(1);
            int currentId = Integer.parseInt(numericPart);
            int nextId = currentId + 1;

            return String.format("P%03d", nextId);
        }
        return "P001";
    }

    @Override
    public List<PatientDTO> getPatientsBySession(String status) throws Exception {
        List<Patient> patients = patientDAO.searchBySessionStatus(status);
        List<PatientDTO> patientList = new ArrayList<>();
        for (Patient p : patients) {
            patientList.add(new PatientDTO(p.getPatient_id(), p.getName(), p.getEmail(), p.getPhone(), p.getAddress(), p.getMedical_history()));
        }
        return patientList;
    }

    @Override
    public List<PatientDTO> getPatientsEnrolledInAllPrograms() {
        List<Patient> patients = patientDAO.getPatientsEnrolledInAllPrograms();
        List<PatientDTO> dtoList = new ArrayList<>();

        for (Patient p : patients) {
            dtoList.add(new PatientDTO(p.getPatient_id(), p.getName(), p.getEmail(), p.getPhone(), p.getAddress(), p.getMedical_history()));
        }
        return dtoList;
    }

}
