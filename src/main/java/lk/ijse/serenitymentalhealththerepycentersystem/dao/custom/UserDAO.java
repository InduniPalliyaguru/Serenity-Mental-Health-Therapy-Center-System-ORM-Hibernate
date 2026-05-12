package lk.ijse.serenitymentalhealththerepycentersystem.dao.custom;

import lk.ijse.serenitymentalhealththerepycentersystem.dao.CrudDAO;
import lk.ijse.serenitymentalhealththerepycentersystem.entity.User;

import java.util.Optional;

public interface UserDAO extends CrudDAO<User> {

    User findByUsername(String username);
    public String validateUser(String username, String password);
    public Optional<User> findByUserId(String userId);
    public boolean updateUsernameAndPassword(String userId, String newUsername, String newPassword);
    Optional<String> getLastPK();

}
