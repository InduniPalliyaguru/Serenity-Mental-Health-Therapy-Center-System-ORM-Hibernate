package lk.ijse.serenitymentalhealththerepycentersystem.service.custom.Impl;

import lk.ijse.serenitymentalhealththerepycentersystem.dao.DAOFactory;
import lk.ijse.serenitymentalhealththerepycentersystem.dao.custom.UserDAO;
import lk.ijse.serenitymentalhealththerepycentersystem.entity.User;
import lk.ijse.serenitymentalhealththerepycentersystem.service.custom.UserBO;
import org.mindrot.jbcrypt.BCrypt;

public class UserBOImpl implements UserBO {

    UserDAO userDAO = (UserDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOType.USER);

        @Override
        public String checkCredentials(String username, String password) {
            User user = userDAO.findByUsername(username);
            if (user != null) {
                // BCrypt.matches() මෙතඩ් එකෙන් පාස්වර්ඩ් පරීක්ෂා කිරීම
                if (BCrypt.checkpw(password, user.getPassword())) {
                    return user.getRole(); // "Admin" හෝ "Receptionist" ලෙවල් එක ලබා දේ [cite: 47]
                }
            }
            return null;
        }

}
