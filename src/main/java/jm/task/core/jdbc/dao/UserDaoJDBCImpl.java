package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    private static Connection conn;

    static {
        try {
            conn = Util.getConnection();
            conn.setAutoCommit(false);
        } catch (SQLException e) {
            System.err.println("Connection not established");
        }
    }


    public UserDaoJDBCImpl() {
    }

    public void createUsersTable() {
        try (Statement statement = conn.createStatement()) {

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS users " +
                    "(id BIGINT PRIMARY KEY AUTO_INCREMENT, name VARCHAR(255), last_name VARCHAR(255), age TINYINT)");
            conn.commit();
        } catch (SQLException e) {
            Util.setRollback(conn);
        }
    }


    public void dropUsersTable() {
        try (Statement statement = conn.createStatement()) {
            statement.executeUpdate("DROP TABLE IF EXISTS users");
            conn.commit();
        } catch (SQLException e) {
            Util.setRollback(conn);
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        try (PreparedStatement pst = conn.prepareStatement("INSERT INTO users (name, last_name, age) VALUES (?, ?, ?)")) {
            pst.setString(1, name);
            pst.setString(2, lastName);
            pst.setByte(3, age);
            pst.executeUpdate();
            conn.commit();
        } catch (SQLException e) {
            Util.setRollback(conn);
        }
    }

    public void removeUserById(long id) {
        try (PreparedStatement preparedStatement = conn.prepareStatement("DELETE FROM users WHERE id = ?")) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
            conn.commit();
        } catch (SQLException e) {
            Util.setRollback(conn);
        }
    }

    public List<User> getAllUsers() {

        List<User> users = new ArrayList<>();

        try (ResultSet resultSet = conn.createStatement().executeQuery("SELECT * FROM users")) {
            while (resultSet.next()) {
                User user = new User(resultSet.getString("name"),
                        resultSet.getString("last_name"), resultSet.getByte("age"));
                user.setId(resultSet.getLong("id"));
                users.add(user);
            }
            conn.commit();
        } catch (SQLException e) {
            Util.setRollback(conn);
        }
        return users;
    }

    public void cleanUsersTable() {
        try (Statement statement = conn.createStatement()) {
            statement.executeUpdate("TRUNCATE TABLE users");
            conn.commit();
        } catch (SQLException e) {
            Util.setRollback(conn);
        }
    }
}
