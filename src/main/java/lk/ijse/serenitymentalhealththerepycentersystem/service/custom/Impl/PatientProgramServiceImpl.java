package lk.ijse.serenitymentalhealththerepycentersystem.service.custom.Impl;

import lk.ijse.serenitymentalhealththerepycentersystem.dao.DAOFactory;
import lk.ijse.serenitymentalhealththerepycentersystem.dao.custom.PatientDAO;
import lk.ijse.serenitymentalhealththerepycentersystem.dao.custom.PatientProgramDAO;
import lk.ijse.serenitymentalhealththerepycentersystem.dao.custom.PaymentDAO;
import lk.ijse.serenitymentalhealththerepycentersystem.dao.custom.TherapyProgramDAO;
import lk.ijse.serenitymentalhealththerepycentersystem.dto.PatientDTO;
import lk.ijse.serenitymentalhealththerepycentersystem.dto.PatientProgramDTO;
import lk.ijse.serenitymentalhealththerepycentersystem.dto.TherapyProgramDTO;
import lk.ijse.serenitymentalhealththerepycentersystem.entity.*;
import lk.ijse.serenitymentalhealththerepycentersystem.service.custom.PatientProgramService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PatientProgramServiceImpl implements PatientProgramService {

    PatientDAO patientDAO = (PatientDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOType.PATIENT);
    PatientProgramDAO patientProgramDAO = (PatientProgramDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOType.PATIENT_PROGRAM);
    PaymentDAO paymentDAO = (PaymentDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOType.PAYMENT);
    TherapyProgramDAO therapyProgramDAO = (TherapyProgramDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOType.THERAPY_PROGRAM);


    @Override
    public boolean savePatientProgram(PatientProgramDTO dto) {
        Patient patientOpt = patientDAO.search(dto.getPatientId());
        TherapyProgram programOpt = therapyProgramDAO.search(dto.getProgramId());
        Optional<Payment> paymentOtp = paymentDAO.findById(dto.getPaymentId());

        if (patientOpt == null || programOpt == null) {
            return false;
        }

        Payment payment = null;
        BigDecimal leftToPay = programOpt.getFee();

        if (paymentOtp.isPresent()) {
            payment = paymentOtp.get();
            leftToPay = leftToPay.subtract(payment.getAmount());
        }

        PatientProgram patientProgram = new PatientProgram();
        patientProgram.setId(new PatientProgramID(patientOpt.getPatient_id(), programOpt.getProgramId()));
        patientProgram.setPatient(patientOpt);
        patientProgram.setTherapy_program(programOpt);
        patientProgram.setRegistration_date(dto.getRegistrationDate());
        patientProgram.setPayment(payment);
        patientProgram.setProgram_fee(leftToPay);

        return patientProgramDAO.save(patientProgram);
    }

    @Override
    public boolean updatePatientProgram(PatientProgramDTO dto) {
        Patient patientOpt = patientDAO.search(dto.getPatientId());
        TherapyProgram programOpt = therapyProgramDAO.search(dto.getProgramId());

        if (patientOpt == null || programOpt == null) {
            return false;
        }
        PatientProgramID id = new PatientProgramID(patientOpt.getPatient_id(), programOpt.getProgramId());

        PatientProgram patientProgram = patientProgramDAO.search(id.toString());

        if (patientProgram == null) {
            patientProgram = new PatientProgram();
            patientProgram.setId(id);
        }

        patientProgram.setPatient(patientOpt);
        patientProgram.setTherapy_program(programOpt);
        patientProgram.setRegistration_date(dto.getRegistrationDate());

        patientProgram.setProgram_fee(dto.getLeftToPay());

        if (dto.getPaymentId() != null) {
            Optional<Payment> paymentOtp = paymentDAO.findById(dto.getPaymentId());
            if (paymentOtp.isPresent()) {
                patientProgram.setPayment(paymentOtp.get());
            }
        }

        return patientProgramDAO.update(patientProgram);
    }

    @Override
    public boolean deletePatientProgram(String patientName, String programName) throws Exception {
        String patientId = patientDAO.findByPatientName(patientName).getFirst().getPatient_id();
        String programId = therapyProgramDAO.findByName(programName).getProgramId();
        return patientProgramDAO.delete(patientId, programId);
    }

    @Override
    public ArrayList<PatientProgramDTO> getAllPatientPrograms() {
        List<PatientProgram> patientPrograms = patientProgramDAO.getAll();
        ArrayList<PatientProgramDTO> patientProgramDtos = new ArrayList<>();

        for (PatientProgram patientProgram : patientPrograms) {
            PatientProgramDTO dto = new PatientProgramDTO();
            dto.setPatientId(patientProgram.getPatient().getPatient_id());
            dto.setPatientName(patientProgram.getPatient().getName());
            dto.setProgramId(patientProgram.getTherapy_program().getProgramId());
            dto.setProgramName(patientProgram.getTherapy_program().getProgramName());
            dto.setLeftToPay(patientProgram.getProgram_fee());

            if (patientProgram.getPayment() != null) {
                dto.setPaymentId(patientProgram.getPayment().getPayment_id());
            } else {
                dto.setPaymentId(null);
            }

            dto.setRegistrationDate(patientProgram.getRegistration_date());
            patientProgramDtos.add(dto);
        }
        return patientProgramDtos;
    }

    @Override
    public PatientDTO findByPatientName(String patientName) {
        List<Patient> patients = patientDAO.findByPatientName(patientName);
        if (patients.isEmpty()) return null;

        Patient patient = patients.get(0);
        PatientDTO patientDto = new PatientDTO();
        patientDto.setPatientId(patient.getPatient_id());
        patientDto.setName(patient.getName());
        patientDto.setEmail(patient.getEmail());
        patientDto.setPhone(patient.getPhone());
        patientDto.setAddress(patient.getAddress());
        patientDto.setMedicalHistory(patient.getMedical_history());

        return patientDto;
    }

    @Override
    public TherapyProgramDTO findByProgramName(String programName) {
        TherapyProgram program = null;
        try {
            program = therapyProgramDAO.findByName(programName);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if (program == null) return null;

        TherapyProgramDTO programDto = new TherapyProgramDTO();
        programDto.setProgramId(program.getProgramId());
        programDto.setProgramName(program.getProgramName());
        programDto.setDescription(program.getDescription());
        programDto.setDuration(program.getDuration());

        return programDto;
    }

    @Override
    public ArrayList<PatientProgramDTO> search(String name, boolean isPatient) throws Exception {
        ArrayList<PatientProgramDTO> patientProgramDtos = new ArrayList<>();
        List<PatientProgram> patientPrograms;

        if (isPatient) {
            List<Patient> patients = patientDAO.findByPatientName(name);
            if (patients.isEmpty()) return patientProgramDtos;

            String patientId = patients.get(0).getPatient_id();
            patientPrograms = patientProgramDAO.findByPatientId(patientId);
        } else {
            TherapyProgram programs = therapyProgramDAO.findByName(name);
            if (programs == null) return patientProgramDtos;

            String programId = programs.getProgramId();
            patientPrograms = patientProgramDAO.findByProgramId(programId);
        }

        for (PatientProgram patientProgram : patientPrograms) {
            PatientProgramDTO dto = new PatientProgramDTO();
            dto.setPatientId(patientProgram.getPatient().getPatient_id());
            dto.setPatientName(patientProgram.getPatient().getName());
            dto.setProgramId(patientProgram.getTherapy_program().getProgramId());
            dto.setProgramName(patientProgram.getTherapy_program().getProgramName());
            dto.setLeftToPay(patientProgram.getProgram_fee());

            if (patientProgram.getPayment() != null) {
                dto.setPaymentId(patientProgram.getPayment().getPayment_id());
            } else {
                dto.setPaymentId("No Payment");
            }

            dto.setRegistrationDate(patientProgram.getRegistration_date());
            patientProgramDtos.add(dto);
        }

        return patientProgramDtos;
    }

    @Override
    public List<PatientProgramDTO> getProgramsByPatientId(String patientId) {
        List<PatientProgram> patientPrograms = patientProgramDAO.findByPatientId(patientId);
        List<PatientProgramDTO> dtos = new ArrayList<>();

        for (PatientProgram pp : patientPrograms) {
            PatientProgramDTO dto = new PatientProgramDTO();
            dto.setPatientId(pp.getPatient().getPatient_id());
            dto.setPatientName(pp.getPatient().getName());
            dto.setProgramId(pp.getTherapy_program().getProgramId());
            dto.setProgramName(pp.getTherapy_program().getProgramName());
            dto.setLeftToPay(pp.getTherapy_program().getFee());
            dto.setRegistrationDate(pp.getRegistration_date());

            if (pp.getPayment() != null) {
                dto.setPaymentId(pp.getPayment().getPayment_id());
            } else {
                dto.setPaymentId("No Payment");
            }
            dtos.add(dto);
        }

        return dtos;
    }

}
