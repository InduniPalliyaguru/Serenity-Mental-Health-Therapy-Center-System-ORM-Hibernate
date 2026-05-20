package lk.ijse.serenitymentalhealththerepycentersystem.service.custom;

import lk.ijse.serenitymentalhealththerepycentersystem.dto.UserDTO;
import lk.ijse.serenitymentalhealththerepycentersystem.service.SuperService;

import java.util.ArrayList;

public interface UserService extends SuperService {

    String authenticateUser(String username, String password) throws Exception;

    boolean registerUser(UserDTO dto);

    boolean updateUser(UserDTO dto);

    boolean deleteUser(String userId);

    ArrayList<UserDTO> searchUser(String userId);

    String generateNextUserId();

    ArrayList<UserDTO> getAllUsers();

}
