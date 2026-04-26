package lk.ijse.serenitymentalhealththerepycentersystem.service.custom.Impl;

import lk.ijse.serenitymentalhealththerepycentersystem.dao.DAOFactory;
import lk.ijse.serenitymentalhealththerepycentersystem.dao.custom.UserDAO;
import lk.ijse.serenitymentalhealththerepycentersystem.entity.User;
import lk.ijse.serenitymentalhealththerepycentersystem.service.custom.UserBO;
import lk.ijse.serenitymentalhealththerepycentersystem.util.PasswordUtil;
import org.mindrot.jbcrypt.BCrypt;

public class UserBOImpl implements UserBO {

    UserDAO userDAO = (UserDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOType.USER);

    @Override
    public String checkCredentials(String username, String password) {
            User user = userDAO.findByUsername(username);
            if (user != null) {

                if (BCrypt.checkpw(password, user.getPassword())) {
                    return user.getRole();
                }
            }
            return null;
        }

    @Override
    public String login(String username, String password) {
        User user = userDAO.findByUsername(username);
        if (user != null && BCrypt.checkpw(password, user.getPassword())) {
            return user.getRole();
        }
        return null;

    }

    @Override
    public String authenticateUser(String username, String password) throws Exception {

        User user = userDAO.findByUsername(username);

        if (user != null) {
            boolean isMatched = PasswordUtil.checkPassword(password, user.getPassword());

            if (isMatched) {
                return user.getRole();
            }
        }

        return null;
    }

}
