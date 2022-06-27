package cookbook.db.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import cookbook.db.DatabaseManager;
import cookbook.db.entities.UserEntity;

 


// Get a UserEntity from db by username and passsword. 
public class UserService {

    public static List<UserEntity> getUsers() {
        String queryString = "SELECT * FROM user ;";
        List<UserEntity> users = new ArrayList<>();
        Connection conn = DatabaseManager.getConn();
        try (PreparedStatement preparedStmnt = conn.prepareStatement(queryString)) {
            ResultSet result = preparedStmnt.executeQuery();
            while (result.next()) {
                String Id = result.getString("id");
                UserEntity user = new UserEntity(
                        Id,
                        result.getString("display_name"),
                        result.getString("username"),
                        result.getString("password"),
                        result.getBoolean("is_admin"));

                users.add(user);

            }

            result.close();
        } catch (SQLException e) {
            System.out.println(e);
        }

        return users;
    }

    /// copied from Devinvci work

    public static UserEntity get(String username, String password) {
        String queryString = "SELECT * FROM user WHERE username=(?) AND password=(?) LIMIT 1;";

        // If no user is retrieved, return null user.
        UserEntity user = null;

        Connection conn = DatabaseManager.getConn();
        try (PreparedStatement preparedStmnt = conn.prepareStatement(queryString)) {
            preparedStmnt.setString(1, username);
            preparedStmnt.setString(2, password);
            ResultSet result = preparedStmnt.executeQuery();
            if (result.next()) {
                user = new UserEntity(
                    result.getString("id"),
                    result.getString("display_name"),
                    result.getString("username"),
                    result.getString("password"),
                    result.getBoolean("is_admin")
                );
            }

            result.close();
        } catch (SQLException e) {
            System.out.println(e);
        }

        return user;
    }

    // Get a user object by username alone.
    public static UserEntity getByUsername(String username) {
        String queryString = "SELECT * FROM user WHERE username=(?) LIMIT 1;";

        // If no user is retrieved, return null user.
        UserEntity user = null;

        Connection conn = DatabaseManager.getConn();
        try (PreparedStatement preparedStmnt = conn.prepareStatement(queryString)) {
            preparedStmnt.setString(1, username);
            ResultSet result = preparedStmnt.executeQuery();
            if (result.next()) {
                user = new UserEntity(
                        result.getString("id"),
                        result.getString("display_name"),
                        result.getString("username"),
                        result.getString("password"),
                        result.getBoolean("is_admin"));
            }

            result.close();
        } catch (SQLException e) {
            System.out.println(e);
        }

        return user;
    }

    // Get a user object by user id alone.
    public static UserEntity getByUserId(String userId) {
        String queryString = "SELECT * FROM user WHERE id=(?) LIMIT 1;";

        // If no user is retrieved, return null user.
        UserEntity user = null;

        Connection conn = DatabaseManager.getConn();
        try (PreparedStatement preparedStmnt = conn.prepareStatement(queryString)) {
            preparedStmnt.setString(1, userId);
            ResultSet result = preparedStmnt.executeQuery();
            if (result.next()) {
                user = new UserEntity(
                        userId,
                        result.getString("display_name"),
                        result.getString("username"),
                        result.getString("password"),
                        result.getBoolean("is_admin"));
            }

            result.close();
        } catch (SQLException e) {
            System.out.println(e);
        }

        return user;
    }

    // Pass a user to have its details changed.s
    public static void update(String displayName, String username, String passsword, boolean isAdmin, String userID) {
        Connection conn = DatabaseManager.getConn();

        String editString = "UPDATE user SET display_name=(?), username=(?), password=(?), is_admin=(?) WHERE id=(?);";

        try (PreparedStatement preparedStmnt = conn.prepareStatement(editString)) {

            preparedStmnt.setString(1, displayName);
            preparedStmnt.setString(2, username);
            preparedStmnt.setString(3, passsword);
            preparedStmnt.setBoolean(4, isAdmin);
            preparedStmnt.setString(5, userID);
            preparedStmnt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    // pass a user to be deleted in the db.
    public static void delete(UserEntity user) {
        String id = user.getId();

        String deleteString = "DELETE FROM user WHERE id=(?);";

        Connection conn = DatabaseManager.getConn();
        try (PreparedStatement preparedStmnt = conn.prepareStatement(deleteString)) {
            preparedStmnt.setString(1, id);
            preparedStmnt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e);
            // If there's an error inserting records, return false.
        }
    }

    // Add a new user. The userID is made within the method.
    // Return true if User added successfully, false if anything else.
    public static void add(String displayName, String username, String passsword, boolean isAdmin) {

        Connection conn = DatabaseManager.getConn();

        // Generate unique user id.
        String userID = generateUniqueUserID();

        // Insert records into db.
        String insertString = "INSERT INTO user VALUES(?, ?, ?, ?, ?);";

        try (PreparedStatement preparedStmnt = conn.prepareStatement(insertString)) {
            preparedStmnt.setString(1, userID);
            preparedStmnt.setString(2, displayName);
            preparedStmnt.setString(3, username);
            preparedStmnt.setString(4, passsword);
            preparedStmnt.setBoolean(5, isAdmin);

            preparedStmnt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e);
            // If there's an error inserting records, return false.
        }
    }

    public static boolean exists(String username) {
        // First we check if the username used exists already
        String queryString = "SELECT * FROM user WHERE username=(?) LIMIT 1;";

        // If no user is retrieved, return null user.
        UserEntity checked_user = null;

        Connection conn = DatabaseManager.getConn();
        try (PreparedStatement preparedStmnt = conn.prepareStatement(queryString)) {
            preparedStmnt.setString(1, username);
            ResultSet result = preparedStmnt.executeQuery();
            if (result.next()) {
                checked_user = new UserEntity(
                        result.getString("id"),
                        result.getString("display_name"),
                        result.getString("username"),
                        result.getString("password"),
                        result.getBoolean("is_admin"));
            }
            result.close();

        
        } catch (SQLException e) {
            System.out.println(e);
            // If there's an error retrieving records, return false.
            return false;
        }
        Boolean user_state = Boolean.valueOf(checked_user.toString());
        return user_state;
    }


    // Generate a uuid for   
    public static String generateUniqueUserID() {
        UUID uuid = UUID.randomUUID();
        String userID = uuid.toString();
        return userID;
    }
}
