package cookbook.db.services;

import cookbook.db.DatabaseManager;
import cookbook.db.entities.MessageEntity;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MessageService {

    // Send a message from one user to another.
    public static void sendMessage(String fromUserId, String toUserId, String recipeId, String body, Timestamp createdAt) {
        
        // Generate a unique message id.
        String messageID = UUID.randomUUID().toString();

        // Insert message into db.
        String insertString = "INSERT INTO message VALUES(?, ?, ?, ?, ?, ?);";
        
        Connection conn = DatabaseManager.getConn();
        try (PreparedStatement preparedStmnt = conn.prepareStatement(insertString)) {
            preparedStmnt.setString(1, messageID);
            preparedStmnt.setString(2, fromUserId);
            preparedStmnt.setString(3, toUserId);
            preparedStmnt.setString(4, recipeId);
            preparedStmnt.setString(5, body);
            preparedStmnt.setTimestamp(6, createdAt);

            preparedStmnt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e);
            // If there's an error inserting records, print error.
        }
    }



    // Get Messages from one user to other user.
    public static List<MessageEntity> getMessagesFromOneUserToOther(String fromUserId, String toUserId) {
        String queryString = "SELECT * FROM message WHERE to_User_Id=(?) AND from_User_Id=(?);";
        List<MessageEntity> messages = new ArrayList<>();
        Connection conn = DatabaseManager.getConn();
        try (PreparedStatement preparedStmnt = conn.prepareStatement(queryString)) {
            preparedStmnt.setString(1, toUserId);
            preparedStmnt.setString(2, fromUserId);
            ResultSet result = preparedStmnt.executeQuery();
            while (result.next()) {
                String Id = result.getString("id");
                MessageEntity user = new MessageEntity(
                    Id,
                    result.getString("from_User_Id"),
                    result.getString("to_User_Id"),
                    result.getString("recipe_Id"),
                    result.getString("body"),
                    result.getTimestamp("created_At")
                );

                messages.add(user);

            }

            result.close();
        } catch (SQLException e) {
            System.out.println(e);
        }

        return messages;
    }
// display the name of the user
    public static String getDisplayName(String userId) {
        String queryString = "SELECT display_name FROM user WHERE id=(?);";
        Connection conn = DatabaseManager.getConn();
        String displayName = null;
        try (PreparedStatement preparedStmnt = conn.prepareStatement(queryString)) {
            preparedStmnt.setString(1, userId);
            ResultSet result = preparedStmnt.executeQuery();
            while (result.next()) {
                 displayName = result.getString("display_name");
            }
            result.close();
        } catch (SQLException e) {
            System.out.println(e);
        }
        return displayName;
    }



    // Get all Messages from a user by id.
    public static List<MessageEntity> getMessagesFromUser(String userId) {
        String queryString = "SELECT * FROM message WHERE to_User_Id=(?);";

        List<MessageEntity> messages = new ArrayList<>();
        Connection conn = DatabaseManager.getConn();
        try (PreparedStatement preparedStmnt = conn.prepareStatement(queryString)) {
            preparedStmnt.setString(1, userId);
            ResultSet result = preparedStmnt.executeQuery();
            while (result.next()) {
                String Id = result.getString("id");
                MessageEntity user = new MessageEntity(
                    Id,
                    result.getString("from_User_Id"),
                    result.getString("to_User_Id"),
                    result.getString("recipe_Id"),
                    result.getString("body"),
                    result.getTimestamp("created_At")
                );

                messages.add(user);

            }

            result.close();
        } catch (SQLException e) {
            System.out.println(e);
        }

        return messages;
    }

// get the recipe name from the id
    public static String getRecipeName(String userId) {
        String queryString = "SELECT name FROM recipe WHERE id=(?);";
        Connection conn = DatabaseManager.getConn();
        String RecipeName = null;
        try (PreparedStatement preparedStmnt = conn.prepareStatement(queryString)) {
            preparedStmnt.setString(1, userId);
            ResultSet result = preparedStmnt.executeQuery();
            while (result.next()) {
                RecipeName = result.getString("name");
            }
            result.close();
        } catch (SQLException e) {
            System.out.println(e);
        }
        return RecipeName;
    }


    // Get all Messages to or from a user by id.
    public static List<MessageEntity> getMessagesToAndFromUser(String userId) {
        String queryString = "SELECT * FROM message WHERE to_User_Id=(?) OR from_User_Id=(?);";
        List<MessageEntity> messages = new ArrayList<>();
        Connection conn = DatabaseManager.getConn();
        try (PreparedStatement preparedStmnt = conn.prepareStatement(queryString)) {
            preparedStmnt.setString(1, userId);
            preparedStmnt.setString(1, userId);
            ResultSet result = preparedStmnt.executeQuery();
            while (result.next()) {
                String Id = result.getString("id");
                MessageEntity user = new MessageEntity(
                    Id,
                    result.getString("from_User_Id"),
                    result.getString("to_User_Id"),
                    result.getString("recipe_Id"),
                    result.getString("body"),
                    result.getTimestamp("created_At")
                );

                messages.add(user);

            }

            result.close();
        } catch (SQLException e) {
            System.out.println(e);
        }

        return messages;
    }


}
