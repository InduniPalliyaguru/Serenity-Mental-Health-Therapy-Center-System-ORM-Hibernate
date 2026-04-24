module lk.ijse.serenitymentalhealththerepycentersystem {
    requires javafx.controls;
    requires javafx.fxml;

    // hibernate
    requires org.hibernate.orm.core;
    requires jakarta.persistence;
    requires java.naming;

//    opens lk.ijse.supermarketfx.entity to org.hibernate.orm.core;
    opens lk.ijse.serenitymentalhealththerepycentersystem.config to jakarta.persistence;

    opens lk.ijse.serenitymentalhealththerepycentersystem to javafx.fxml;
    exports lk.ijse.serenitymentalhealththerepycentersystem;
}