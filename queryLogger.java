/*
Name: Viviana Luna
Course: CNT 4714 Fall 2024
Assignment title: Project 3 – Query Transaction Log
Date: October 20, 2024
Class: queryLogger
*/

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;
import java.io.FileInputStream;
import java.io.IOException;

public class queryLogger {
    private Connection connection;

    public queryLogger() {
        try {
            // Connection to operations database
            Properties properties = new Properties();
            FileInputStream fileInputStream = new FileInputStream("operationsdb.properties");
            properties.load(fileInputStream);
            String url = properties.getProperty("MYSQL_DB_URL");
            String user = properties.getProperty("MYSQL_DB_USERNAME");
            String password = properties.getProperty("MYSQL_DB_PASSWORD");

            connection = DriverManager.getConnection(url, user, password);
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    // Updating the log each time a query is executed lol ha
    public void logQuery(String username) {
        updateLog(username, true);
    }

    // Note for me: these two go together for later
    public void logUpdate(String username) {
        updateLog(username, false);
    }

    private void updateLog(String username, boolean isQuery) {
        String sql = "INSERT INTO transaction_logs (username, query_count, update_count) VALUES (?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE query_count = query_count + ?, update_count = update_count + ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            statement.setInt(2, isQuery ? 1 : 0);
            statement.setInt(3, isQuery ? 0 : 1);
            statement.setInt(4, isQuery ? 1 : 0);
            statement.setInt(5, isQuery ? 0 : 1);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void disconnect() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
