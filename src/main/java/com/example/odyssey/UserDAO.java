package com.example.odyssey;

import com.example.odyssey.model.User;
import io.github.cdimascio.dotenv.Dotenv;

import java.sql.*;

public class UserDAO {
    private final Dotenv dotenv = Dotenv.load();
    private final String INSERT_USER = "INSERT INTO users(user_name, user_password, email) VALUES(?,?,?)";
    private final String GET_USER_BY_NAME = "SELECT * FROM users WHERE user_name = ?";
    private final String GET_USER_BY_EMAIL = "SELECT * FROM users WHERE email = ?";


    public void insertUser(User user) {
        try(Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(INSERT_USER)){
            ps.setString(1, user.getName());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getEmail());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public User getUserByName(String name, String password) {
        return getUser(name, password, GET_USER_BY_NAME);
    }

    public User getUserByEmail(String email, String password) {
        return getUser(email, password, GET_USER_BY_EMAIL);
    }

    private User getUser(String info, String password, String getUserByEmail) {
        try(Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(getUserByEmail)){
            ps.setString(1, info);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                return new User(rs.getString("user_name"), rs.getString("email") ,rs.getString("user_password"));
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Connection getConnection() {
        String DB_URL = dotenv.get("DB_URL");
        String DB_USER = dotenv.get("DB_USER");
        String DB_PASSWORD = dotenv.get("DB_PASSWORD");
        try {
            return DriverManager.getConnection(DB_URL , DB_USER, DB_PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
