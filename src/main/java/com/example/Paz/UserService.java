package com.example.Paz;

import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Service
public class UserService {

    private static final String URL = "jdbc:mysql://localhost:3307/prueba";
    private static final String USER = "root";
    private static final String PASSWORD = "S3gura@2024";

    public void addUser(String name, String email, String password, String number, String cityCode, String countryCode) {
        String sql = "INSERT INTO usuarios (name, email, password, number, citycode, countrycode) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, password);
            preparedStatement.setString(4, number);
            preparedStatement.setString(5, cityCode);
            preparedStatement.setString(6, countryCode);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
