package lk.ijse.serenitymentalhealththerepycentersystem.service.custom.Impl;

import lk.ijse.serenitymentalhealththerepycentersystem.config.FactoryConfiguration;
import lk.ijse.serenitymentalhealththerepycentersystem.dao.DAOFactory;
import lk.ijse.serenitymentalhealththerepycentersystem.dao.custom.*;
import lk.ijse.serenitymentalhealththerepycentersystem.dto.PaymentDTO;
import lk.ijse.serenitymentalhealththerepycentersystem.entity.*;
import lk.ijse.serenitymentalhealththerepycentersystem.service.custom.PaymentService;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

public class PaymentServiceImpl implements PaymentService {

    PatientDAO patientDAO = (PatientDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOType.PATIENT);
    PatientProgramDAO patientProgramDAO = (PatientProgramDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOType.PATIENT_PROGRAM);
    PaymentDAO paymentDAO = (PaymentDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOType.PAYMENT);
    TherapyProgramDAO programDAO = (TherapyProgramDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOType.THERAPY_PROGRAM);
    TherapySessionDAO sessionDAO = (TherapySessionDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOType.THERAPY_SESSION);


    @Override
    public boolean savePayment(PaymentDTO dto) {
        boolean isCompleted = false;

        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();

        try {
            Patient patientOpt = patientDAO.search(dto.getPatient().getPatient_id());
            TherapyProgram programOpt = programDAO.search(dto.getTherapyProgram().getProgramId());
            Optional<TherapySession> sessionOpt = Optional.empty();

            if (dto.getTherapySession() != null) {
                sessionOpt = sessionDAO.findBySessionId(dto.getTherapySession().getSession_id());
            }

            if (patientOpt == null || programOpt == null) return false;

            Payment payment = new Payment();
            payment.setPayment_id(dto.getPaymentId());
            payment.setPatient(patientOpt);
            payment.setTherapy_program(programOpt);
            payment.setTherapy_session(sessionOpt.orElse(null));
            payment.setAmount(dto.getAmount());
            payment.setPayment_date(dto.getPaymentDate());

            if (paymentDAO.save(payment)) {
                isCompleted = true;

                Optional<PatientProgram> patientProgram = patientProgramDAO.findById(patientOpt.getPatient_id(), programOpt.getProgramId());
                if (patientProgram.isPresent()) {
                    BigDecimal oldFee = patientProgram.get().getProgram_fee();
                    BigDecimal newFee = oldFee.subtract(dto.getAmount());

                    patientProgramDAO.updateTherapyProgramFee(patientOpt.getPatient_id(), programOpt.getProgramId(), newFee);
                }

                transaction.commit();
            } else {
                isCompleted = false;
            }

        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            System.out.println(e.getMessage());
        } finally {
            session.close();
        }

        return isCompleted;
    }

    @Override
    public boolean updatePayment(PaymentDTO dto) {
        boolean isCompleted = false;

        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();

        try {
            Patient patientOpt = patientDAO.search(dto.getPatient().getPatient_id());
            TherapyProgram programOpt = programDAO.search(dto.getTherapyProgram().getProgramId());
            Optional<TherapySession> sessionOpt = Optional.empty();

            if (dto.getTherapySession() != null) {
                sessionOpt = sessionDAO.findBySessionId(dto.getTherapySession().getSession_id());
            }

            if (patientOpt == null || programOpt == null) {
                return false;
            }

            Optional<Payment> existingPaymentOpt = paymentDAO.findById(dto.getPaymentId());
            if (existingPaymentOpt.isEmpty()) {
                return false;
            }

            Payment existingPayment = existingPaymentOpt.get();

            existingPayment.setPatient(patientOpt);
            existingPayment.setTherapy_program(programOpt);
            existingPayment.setTherapy_session(sessionOpt.orElse(null));
            existingPayment.setAmount(dto.getAmount());
            existingPayment.setPayment_date(dto.getPaymentDate());

            if (paymentDAO.update(existingPayment)) {
                isCompleted = true;

                Optional<PatientProgram> patientProgramOpt = patientProgramDAO.findById(patientOpt.getPatient_id(), programOpt.getProgramId());
                if (patientProgramOpt.isPresent()) {
                    PatientProgram patientProgram = patientProgramOpt.get();
                    BigDecimal oldFee = patientProgram.getProgram_fee();

                    BigDecimal paymentDifference = dto.getAmount().subtract(existingPayment.getAmount());
                    if (paymentDifference.compareTo(BigDecimal.ZERO) != 0) {

                        BigDecimal newFee = oldFee.subtract(paymentDifference);
                        patientProgramDAO.updateTherapyProgramFee(patientOpt.getPatient_id(), programOpt.getProgramId(), newFee);
                    }
                }

                transaction.commit();
            } else {
                isCompleted = false;
            }

        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            System.out.println(e.getMessage());
        } finally {
            session.close();
        }

        return isCompleted;
    }

    @Override
    public boolean deletePayment(String paymentId) {
        boolean isCompleted = false;

        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();

        try {
            Optional<Payment> paymentOpt = paymentDAO.findById(paymentId);
            if (paymentOpt.isEmpty()) {
                return false;
            }

            Payment payment = paymentOpt.get();
            Patient patient = payment.getPatient();
            TherapyProgram program = payment.getTherapy_program();

            Optional<PatientProgram> patientProgramOpt = patientProgramDAO.findById(patient.getPatient_id(), program.getProgramId());
            if (patientProgramOpt.isPresent()) {
                PatientProgram patientProgram = patientProgramOpt.get();
                BigDecimal oldFee = patientProgram.getProgram_fee();

                BigDecimal newFee = oldFee.add(payment.getAmount());

                patientProgramDAO.updateTherapyProgramFee(patient.getPatient_id(), program.getProgramId(), newFee);
            }

            if (paymentDAO.delete(paymentId)) {
                isCompleted = true;
                transaction.commit();
            } else {
                isCompleted = false;
            }

        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            System.out.println(e.getMessage());
        } finally {
            session.close();
        }

        return isCompleted;
    }

    @Override
    public ArrayList<PaymentDTO> getAllPayments() {
        List<Payment> payments = paymentDAO.getAll();
        ArrayList<PaymentDTO> dtos = new ArrayList<>();

        for (Payment p : payments) {
            PaymentDTO dto = new PaymentDTO(
                    p.getPayment_id(),
                    p.getPatient(),
                    p.getTherapy_program(),
                    p.getTherapy_session(),
                    p.getAmount(),
                    p.getPayment_date()
            );
            dtos.add(dto);
        }
        return dtos;
    }

    @Override
    public ArrayList<PaymentDTO> searchByPatientName(String name) {
        List<Payment> payments = paymentDAO.findByPatientName(name);
        ArrayList<PaymentDTO> dtos = new ArrayList<>();

        for (Payment p : payments) {
            PaymentDTO dto = new PaymentDTO(
                    p.getPayment_id(),
                    p.getPatient(),
                    p.getTherapy_program(),
                    p.getTherapy_session(),
                    p.getAmount(),
                    p.getPayment_date()
            );
            dtos.add(dto);
        }

        return dtos;
    }

    @Override
    public String getNextPaymentPK() {
        Optional<String> lastPK = paymentDAO.getLastPK();
        if (lastPK.isPresent()) {
            String pk = lastPK.get().replace("PAY-", "");
            int next = Integer.parseInt(pk) + 1;
            return String.format("PAY-%04d", next);
        }
        return "PAY-0001";
    }

    @Override
    public PaymentDTO constructPaymentDto(String paymentId, String patientId, String programId, String sessionId, BigDecimal amount, LocalDate date) {
        Patient patientOpt = patientDAO.search(patientId);
        TherapyProgram programOpt = programDAO.search(programId);
        Optional<TherapySession> sessionOpt = (sessionId != null && !sessionId.isEmpty())
                ? sessionDAO.findBySessionId(sessionId)
                : Optional.empty();

        if (patientOpt == null || programOpt == null) {
            throw new RuntimeException("Patient or Program not found.");
        }

        TherapySession session = sessionOpt.orElse(null);

        return new PaymentDTO(paymentId, patientOpt, programOpt, session, amount, date);
    }

    @Override
    public void generateInvoice(String paymentId) {
        paymentDAO.printInvoiceReport(paymentId);
    }

}
