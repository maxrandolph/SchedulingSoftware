package schedulingsoftware;

import java.sql.*;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author max.randolph
 */
public class ConnectionManager {

    private Connection connection;

    public ConnectionManager() {

    }

    public Connection open() {

        try {

            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(
                    "jdbc:mysql://52.206.157.109/U05mLi", "U05mLi", "53688547355"
            );
            return connection;
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Failed to get connection");
            return null;
        }
    }

    public void close(ResultSet rs) {

        if (rs != null) {
            try {
                rs.close();
            } catch (Exception e) {
            }

        }
    }

    public void close(Statement stmt) {

        if (stmt != null) {
            try {
                stmt.close();
            } catch (Exception e) {
            }

        }
    }

    public void destroy() {

        if (connection != null) {

            try {
                connection.close();
            } catch (Exception e) {
            }

        }
    }
}
