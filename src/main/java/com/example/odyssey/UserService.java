package com.example.odyssey;

import org.mindrot.jbcrypt.BCrypt;

public class UserService {
    private UserDAO userDAO = new UserDAO();

    public void addUser(String name, String email, String password) {
        userDAO.insertUser(new User(name, email, password));
    }

    public boolean verifyUser(String info, String password) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        if (info.matches(emailRegex)) {
            return verifyUserByEmail(info, password);
        }
        return verifyUserByName(info, password);
    }

    private boolean verifyUserByEmail(String email, String password) {
        boolean isUser = false;
        User user = userDAO.getUserByEmail(email, password);
        if (user != null) {
            return BCrypt.checkpw(password, user.getPassword());
        }
        return isUser;
    }

    private boolean verifyUserByName(String userName, String password) {
        boolean isUser = false;
        User user = userDAO.getUserByName(userName, password);
        if (user != null) {
            return BCrypt.checkpw(password, user.getPassword());
        }
        return isUser;
    }
}
