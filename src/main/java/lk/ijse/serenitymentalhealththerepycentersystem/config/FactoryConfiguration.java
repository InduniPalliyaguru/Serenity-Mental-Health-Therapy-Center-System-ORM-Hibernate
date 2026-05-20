package lk.ijse.serenitymentalhealththerepycentersystem.config;

import lk.ijse.serenitymentalhealththerepycentersystem.entity.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class FactoryConfiguration {

    private static FactoryConfiguration factoryConfiguration;
    private SessionFactory sessionFactory;

    private FactoryConfiguration() {
        Configuration configuration = new Configuration();

        configuration.addAnnotatedClass(User.class)
                .addAnnotatedClass(TherapyProgram.class)
                .addAnnotatedClass(Patient.class)
                .addAnnotatedClass(PatientProgram.class)
                .addAnnotatedClass(Therapist.class)
                .addAnnotatedClass(TherapistProgram.class)
                .addAnnotatedClass(TherapistAvailability.class)
                .addAnnotatedClass(TherapySession.class)
                .addAnnotatedClass(Payment.class);

        sessionFactory = configuration.buildSessionFactory();
    }

    public static FactoryConfiguration getInstance() {
        return (factoryConfiguration == null) ? factoryConfiguration = new FactoryConfiguration() : factoryConfiguration;
    }

    public Session getSession() {
        return sessionFactory.openSession();
    }


}
