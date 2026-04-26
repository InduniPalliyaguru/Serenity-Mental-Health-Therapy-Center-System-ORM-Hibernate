package lk.ijse.serenitymentalhealththerepycentersystem.service.custom;

import lk.ijse.serenitymentalhealththerepycentersystem.service.SuperBO;

public interface UserBO extends SuperBO {

    String checkCredentials(String username, String password);

    String login(String username, String password);

    String authenticateUser(String username, String password) throws Exception;

}
