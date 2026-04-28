package lk.ijse.serenitymentalhealththerepycentersystem.service;

import lk.ijse.serenitymentalhealththerepycentersystem.service.custom.Impl.PatientBOImpl;
import lk.ijse.serenitymentalhealththerepycentersystem.service.custom.Impl.TherapistBOImpl;
import lk.ijse.serenitymentalhealththerepycentersystem.service.custom.Impl.TherapyProgramBOImpl;
import lk.ijse.serenitymentalhealththerepycentersystem.service.custom.Impl.UserBOImpl;

public class BOFactory {

    private static BOFactory boFactory;

    private BOFactory() {
    }

    public static BOFactory getInstance() {
        return (boFactory == null) ? (boFactory = new BOFactory()) : boFactory;
    }

    public enum BOType {
        PATIENT,
        PATIENT_PROGRAM,
        PAYMENT,
        THERAPIST,
        THERAPIST_AVAILABILITY,
        THERAPIST_PROGRAM,
        THERAPY_PROGRAM,
        THERAPY_SESSION,
        USER
    }

    public SuperBO getBO(BOType type) {
        switch (type) {
            case PATIENT:
                return new PatientBOImpl();
            case PATIENT_PROGRAM:
//                return new PatientProgramBOImpl();
            case PAYMENT:
//                return new PaymentBOImpl();
            case THERAPIST:
                return new TherapistBOImpl();
            case THERAPIST_AVAILABILITY:
//                return new TherapistAvailabilityBOImpl();
            case THERAPIST_PROGRAM:
//                return new TherapistProgramBOImpl();
            case THERAPY_PROGRAM:
                return new TherapyProgramBOImpl();
            case THERAPY_SESSION:
//                return new TherapySessionBOImpl();
            case USER:
                return new UserBOImpl();
            default:
                return null;
        }
    }

}
