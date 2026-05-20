package lk.ijse.serenitymentalhealththerepycentersystem.dao.custom;

import lk.ijse.serenitymentalhealththerepycentersystem.dao.CrudDAO;
import lk.ijse.serenitymentalhealththerepycentersystem.entity.User;

import java.util.Optional;

public interface UserDAO extends CrudDAO<User> {

    User findByUsername(String username);

    Optional<String> getLastPK();

}
