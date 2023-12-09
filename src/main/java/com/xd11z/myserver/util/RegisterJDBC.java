package com.xd11z.myserver.util;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RegisterJDBC {
    // 查询用户是否存在
    public static boolean isUserExists(String username) {
        try (Connection connection = DriverManager.getConnection(JDBCconnection.connectionurl)) {
            String query = "SELECT COUNT(*) FROM Users WHERE Username = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, username);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        int count = resultSet.getInt(1);
                        return count > 0;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // 生成新的用户ID
    public static String generateNewUserID() {
        try (Connection connection = DriverManager.getConnection(JDBCconnection.connectionurl)) {
            String query = "SELECT MAX(CAST(UserID AS INT)) FROM Users";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        int maxUserID = resultSet.getInt(1);
                        int newUserID = maxUserID + 1;
                        return String.format("%02d", newUserID); // 前面补0
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    // 插入新用户
    public static void insertUser(String username, String password) {
        try (Connection connection = DriverManager.getConnection(JDBCconnection.connectionurl)) {
            String query = "INSERT INTO Users (UserID, Username, Password, Role) VALUES (?, ?, ?, 0)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                String newUserID = generateNewUserID();
                preparedStatement.setString(1, newUserID);
                preparedStatement.setString(2, username);
                preparedStatement.setString(3, password);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
