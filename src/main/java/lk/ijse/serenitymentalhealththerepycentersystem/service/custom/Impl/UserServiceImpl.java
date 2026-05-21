package lk.ijse.serenitymentalhealththerepycentersystem.service.custom.Impl;

import lk.ijse.serenitymentalhealththerepycentersystem.dao.DAOFactory;
import lk.ijse.serenitymentalhealththerepycentersystem.dao.custom.UserDAO;
import lk.ijse.serenitymentalhealththerepycentersystem.dto.UserDTO;
import lk.ijse.serenitymentalhealththerepycentersystem.entity.User;
import lk.ijse.serenitymentalhealththerepycentersystem.exception.LoginException;
import lk.ijse.serenitymentalhealththerepycentersystem.service.custom.UserService;
import lk.ijse.serenitymentalhealththerepycentersystem.util.PasswordUtil;

import java.util.*;

public class UserServiceImpl implements UserService {

    UserDAO userDAO = (UserDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOType.USER);

    private final Map<String, Integer> loginAttemptsMap = new HashMap<>();
    private static final int MAX_ATTEMPTS = 3;

    @Override
    public String authenticateUser(String username, String password) {

        if (loginAttemptsMap.containsKey(username) && loginAttemptsMap.get(username) >= MAX_ATTEMPTS) {
            throw new LoginException("Login Failed: Your account has been locked due to multiple failed attempts! Please contact Admin.");
        }

        User user = userDAO.findByUsername(username);

        if (user != null) {
            boolean isMatched = PasswordUtil.checkPassword(password, user.getPassword());

            if (isMatched) {
                loginAttemptsMap.remove(username);
                return user.getRole();
            }
        }

        int currentAttempts = loginAttemptsMap.getOrDefault(username, 0) + 1;
        loginAttemptsMap.put(username, currentAttempts);

        if (currentAttempts >= MAX_ATTEMPTS) {
            throw new LoginException("Login Failed: Maximum login attempts exceeded. Your account is now locked!");
        }
        int remainingAttempts = MAX_ATTEMPTS - currentAttempts;
        throw new LoginException("Login Failed: Invalid Username or Password! (" + remainingAttempts + " attempts remaining)");
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
            String lastPk = lastPkOpt.get();
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


}
