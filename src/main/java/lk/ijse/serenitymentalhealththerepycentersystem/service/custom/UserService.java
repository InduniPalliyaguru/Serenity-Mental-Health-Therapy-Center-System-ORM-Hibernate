package lk.ijse.serenitymentalhealththerepycentersystem.service.custom;

import lk.ijse.serenitymentalhealththerepycentersystem.dto.UserDTO;
import lk.ijse.serenitymentalhealththerepycentersystem.service.SuperService;

import java.util.ArrayList;

public interface UserService extends SuperService {

    String checkCredentials(String username, String password);

    String login(String username, String password);

    String authenticateUser(String username, String password) throws Exception;

     String validateUser(String username, String password);
     boolean registerUser(UserDTO dto);
     boolean updateUser(UserDTO dto);
     boolean deleteUser(String userId);
     ArrayList<UserDTO> searchUser(String userId);
     String generateNextUserId();
     ArrayList<UserDTO> getAllUsers();
     UserDTO findUserByUserId(String userId);
     boolean updateUsernameAndPassword(String userId, String newUsername, String newPassword);

}
