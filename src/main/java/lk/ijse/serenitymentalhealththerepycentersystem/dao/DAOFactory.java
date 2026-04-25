package lk.ijse.serenitymentalhealththerepycentersystem.dao;

import lk.ijse.serenitymentalhealththerepycentersystem.dao.custom.impl.UserDAOImpl;

public class DAOFactory {

    private static DAOFactory daoFactory;

    private DAOFactory() {
    }

    public static DAOFactory getInstance() {
        return (daoFactory == null) ? (daoFactory = new DAOFactory()) : daoFactory;
    }

    public enum DAOType {
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

    public SuperDAO getDAO(DAOType type) {
        switch (type) {
            case PATIENT:
//                return new PatientDAOImpl();
            case PATIENT_PROGRAM:
//                return new PatientProgramDAOImpl();
            case PAYMENT:
//                return new PaymentDAOImpl();
            case THERAPIST:
//                return new TherapistDAOImpl();
            case THERAPIST_AVAILABILITY:
//                return new TherapistAvailabilityDAOImpl();
            case THERAPIST_PROGRAM:
//                return new TherapistProgramDAOImpl();
            case THERAPY_PROGRAM:
//                return new TherapyProgramDAOImpl();
            case THERAPY_SESSION:
//                return new TherapySessionDAOImpl();
            case USER:
                return new UserDAOImpl();
            default:
                return null;
        }
    }

}
