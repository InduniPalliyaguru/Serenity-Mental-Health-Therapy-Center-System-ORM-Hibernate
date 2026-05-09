package lk.ijse.serenitymentalhealththerepycentersystem.config;

import lk.ijse.serenitymentalhealththerepycentersystem.entity.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.io.IOException;
import java.util.Properties;

public class FactoryConfiguration {
//
//    private static FactoryConfiguration factoryConfiguration;
//    private static SessionFactory sessionFactory;
//
//    private FactoryConfiguration() throws IOException {
//        Configuration configuration = new Configuration();
//        Properties properties = new Properties();
//
//        properties.load(ClassLoader.getSystemResourceAsStream("hibernate.properties"));
//
//        configuration.setProperties(properties);
//
//      configuration.addAnnotatedClass(User.class);
//
//        sessionFactory = configuration.buildSessionFactory();
//    }
//
//    public static FactoryConfiguration getInstance() throws IOException {
//        return (factoryConfiguration == null) ? new FactoryConfiguration() : factoryConfiguration;
//    }
//
//    public Session getSession() {
//        return sessionFactory.openSession();
//    }

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
