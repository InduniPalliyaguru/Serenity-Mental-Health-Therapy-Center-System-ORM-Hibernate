package lk.ijse.serenitymentalhealththerepycentersystem.dao.custom;

import lk.ijse.serenitymentalhealththerepycentersystem.dao.CrudDAO;
import lk.ijse.serenitymentalhealththerepycentersystem.entity.Payment;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PaymentDAO extends CrudDAO<Payment> {

     List<Payment> findByPatientName(String name);
     Optional<Payment> findById(String pk);
     List<Payment> findByDate(LocalDate date);
    Optional<String> getLastPK();

}
