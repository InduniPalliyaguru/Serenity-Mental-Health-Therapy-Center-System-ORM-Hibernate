module lk.ijse.serenitymentalhealththerepycentersystem {
    requires javafx.controls;
    requires javafx.fxml;

    requires jbcrypt;

    // hibernate
    requires org.hibernate.orm.core;
    requires jakarta.persistence;
    requires java.naming;
    requires static lombok;
    requires javafx.base;

    opens lk.ijse.serenitymentalhealththerepycentersystem.config to jakarta.persistence;

    opens lk.ijse.serenitymentalhealththerepycentersystem to javafx.fxml;
    opens lk.ijse.serenitymentalhealththerepycentersystem.dto.tm to javafx.base;
    opens lk.ijse.serenitymentalhealththerepycentersystem.controller to javafx.fxml;
    opens lk.ijse.serenitymentalhealththerepycentersystem.entity to org.hibernate.orm.core;

    exports lk.ijse.serenitymentalhealththerepycentersystem;
    exports lk.ijse.serenitymentalhealththerepycentersystem.controller;
}