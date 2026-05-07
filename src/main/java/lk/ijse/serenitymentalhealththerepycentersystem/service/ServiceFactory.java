package lk.ijse.serenitymentalhealththerepycentersystem.service;

import lk.ijse.serenitymentalhealththerepycentersystem.service.custom.Impl.*;

public class ServiceFactory {

    private static ServiceFactory boFactory;

    private ServiceFactory() {
    }

    public static ServiceFactory getInstance() {
        return (boFactory == null) ? (boFactory = new ServiceFactory()) : boFactory;
    }

    public enum ServiceType {
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

    public SuperService getService(ServiceType type) {
        switch (type) {
            case PATIENT:
                return new PatientServiceImpl();
            case PATIENT_PROGRAM:
//                return new PatientProgramBOImpl();
            case PAYMENT:
//                return new PaymentBOImpl();
            case THERAPIST:
                return new TherapistServiceImpl();
            case THERAPIST_AVAILABILITY:
                return new TherapistAvailabilityServiceImpl();
            case THERAPIST_PROGRAM:
//                return new TherapistProgramBOImpl();
            case THERAPY_PROGRAM:
                return new TherapyProgramServiceImpl();
            case THERAPY_SESSION:
//                return new TherapySessionBOImpl();
            case USER:
                return new UserServiceImpl();
            default:
                return null;
        }
    }

}
