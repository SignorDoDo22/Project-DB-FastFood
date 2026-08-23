package project.db.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DAOUtils {

    public static Connection localMySQLConnection(String database, String username, String password) {
        try {
            var host = "localhost";
            var port = "3306";
            var connectionString = "jdbc:mysql://" + host + ":" + port + "/" + database;
            return DriverManager.getConnection(connectionString, username, password);
        } catch (Exception e) {
            throw new DAOException(e);
        }
    }

    public static PreparedStatement prepare(Connection conn, String query, Object... params) throws SQLException {
        PreparedStatement statement = null;
        try {
            statement = conn.prepareStatement(query);
            for (int i = 0; i < params.length; i++) {
                statement.setObject(i + 1, params[i]);
            }
        } catch (SQLException e) {
            if (statement != null) {
                statement.close();
            }
            throw e;
        }
        return statement;
    }
}
