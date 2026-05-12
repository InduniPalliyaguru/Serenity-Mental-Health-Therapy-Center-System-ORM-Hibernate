package lk.ijse.serenitymentalhealththerepycentersystem.service.custom.Impl;

import lk.ijse.serenitymentalhealththerepycentersystem.dao.DAOFactory;
import lk.ijse.serenitymentalhealththerepycentersystem.dao.custom.UserDAO;
import lk.ijse.serenitymentalhealththerepycentersystem.dto.UserDTO;
import lk.ijse.serenitymentalhealththerepycentersystem.entity.User;
import lk.ijse.serenitymentalhealththerepycentersystem.service.custom.UserService;
import lk.ijse.serenitymentalhealththerepycentersystem.util.KeepUserIDUtil;
import lk.ijse.serenitymentalhealththerepycentersystem.util.PasswordUtil;
import org.mindrot.jbcrypt.BCrypt;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserServiceImpl implements UserService {

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

    @Override
    public String validateUser(String username, String password) {
        User user = userDAO.findByUsername(username);

        if (user == null) {
            return null;
        }

        if (verifyPassword(password, user.getPassword())) {
            KeepUserIDUtil.getInstance().setCurrentUserId(user.getUser_id());
            return user.getRole();
        } else {
            return null;
        }
    }

    public boolean verifyPassword(String plainPassword, String hashedPassword) {
        if (hashedPassword == null || hashedPassword.isEmpty()) {
            throw new IllegalArgumentException("Hashed password cannot be null or empty");
        }

        try {
            return BCrypt.checkpw(plainPassword, hashedPassword);
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid password hash format: " + hashedPassword);
            return false;
        }
    }

    @Override
    public boolean registerUser(UserDTO dto) {
        User optionalUser = userDAO.findByUsername(dto.getUsername());
        if (optionalUser != null) {
            return false;
        }

        User user = new User();
        user.setUser_id(dto.getUser_id());
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setRole(dto.getRole());
        user.setPassword(PasswordUtil.hashPassword(dto.getPassword()));

        return userDAO.save(user);
    }

    @Override
    public boolean updateUser(UserDTO dto) {
        User optionalUser = userDAO.findByUsername(dto.getUsername());
        if (optionalUser == null) {
            return false;
        }

        User user = new User();
        user.setUser_id(dto.getUser_id());
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setRole(optionalUser.getRole());
        user.setPassword(PasswordUtil.hashPassword(dto.getPassword()));

        return userDAO.update(user);
    }

    @Override
    public boolean deleteUser(String userId) {
        return userDAO.delete(userId);
    }

    @Override
    public ArrayList<UserDTO> searchUser(String name) {
        User user = userDAO.findByUsername(name);
        ArrayList<UserDTO> userDtos = new ArrayList<>();

        if (user != null) {
            UserDTO userDto = new UserDTO();
            userDto.setUser_id(user.getUser_id());
            userDto.setUsername(user.getUsername());
            userDto.setEmail(user.getEmail());
            userDto.setPassword(user.getPassword());
            userDto.setRole(user.getRole());
            userDtos.add(userDto);
        }

        return userDtos;
    }

    @Override
    public String generateNextUserId() {
        Optional<String> lastPkOpt = userDAO.getLastPK();

        if (lastPkOpt.isPresent()) {
            String lastPk = lastPkOpt.get(); // e.g., U005
            int num = Integer.parseInt(lastPk.replace("U", ""));
            return String.format("U%03d", num + 1);
        } else {
            return "U001";
        }
    }

    @Override
    public ArrayList<UserDTO> getAllUsers() {
        List<User> users = userDAO.getAll();

        ArrayList<UserDTO> userDtos = new ArrayList<>();
        for (User user : users) {
            UserDTO userDto = new UserDTO();
            userDto.setUser_id(user.getUser_id());
            userDto.setUsername(user.getUsername());
            userDto.setEmail(user.getEmail());
            userDto.setPassword(user.getPassword());
            userDto.setRole(user.getRole());
            userDtos.add(userDto);
        }
        return userDtos;
    }

    @Override
    public UserDTO findUserByUserId(String userId) {
        Optional<User> optionalUser = userDAO.findByUserId(userId);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            UserDTO userDto = new UserDTO();
            userDto.setUser_id(user.getUser_id());
            userDto.setUsername(user.getUsername());
            userDto.setEmail(user.getEmail());
            userDto.setPassword(user.getPassword());
            userDto.setRole(user.getRole());
            return userDto;
        }

        return null;
    }

    @Override
    public boolean updateUsernameAndPassword(String userId, String newUsername, String newPassword) {
        String hashedPassword = PasswordUtil.hashPassword(newPassword);
        return userDAO.updateUsernameAndPassword(userId, newUsername, hashedPassword);
    }

}
