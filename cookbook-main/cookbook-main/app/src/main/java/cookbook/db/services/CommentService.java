package cookbook.db.services;

import cookbook.GlobalState;
import cookbook.db.DatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;


public class CommentService {
  
//  UPDATE comment
//  SET body = " edereRO"
//  WHERE id = "40445a38-5f75-4224-b985-f04d36d53e43"
  public static void editComment(String body, String comment_id) {
    String query = " UPDATE comment SET body = (?) WHERE id = (?);";
    Connection conn = DatabaseManager.getConn();
    try (PreparedStatement preparedStmnt = conn.prepareStatement(query)) {
      preparedStmnt.setString(1, body);
      preparedStmnt.setString(2, comment_id);

      preparedStmnt.executeUpdate();

    } catch (SQLException e) {
      System.out.println(e);
    }
  }


  // INSERT new comments
  public static void addComment(String body, String recipeId, String userId) {

    UUID id = UUID.randomUUID();
    String query = "INSERT into comment VALUES ( (?) , (?),  (?) , (?))";
    Connection conn = DatabaseManager.getConn();
    try (PreparedStatement preparedStmnt = conn.prepareStatement(query)) {
      preparedStmnt.setString(1, id.toString());
      preparedStmnt.setString(2, body);
      preparedStmnt.setString(3, recipeId);
      preparedStmnt.setString(4, GlobalState.user.getId());
      preparedStmnt.executeUpdate();

    } catch (SQLException e) {
      System.out.println(e);
    }

  }

  // INSERT new comments which have an ID
  public static void addComment(String id, String body, String recipeId, String userId) {

    String query = "INSERT into comment VALUES ( (?) , (?),  (?) , (?))";
    Connection conn = DatabaseManager.getConn();
    try (PreparedStatement preparedStmnt = conn.prepareStatement(query)) {
      preparedStmnt.setString(1, id);
      preparedStmnt.setString(2, body);
      preparedStmnt.setString(3, recipeId);
      preparedStmnt.setString(4, GlobalState.user.getId());
      preparedStmnt.executeUpdate();

    } catch (SQLException e) {
      System.out.println(e);
    }

  }


  // DELETE comment by id
  public static void deleteComment(String comment_id) {

    String query = "DELETE FROM comment WHERE id = (?)";
    Connection conn = DatabaseManager.getConn();
    try (PreparedStatement preparedStmnt = conn.prepareStatement(query)) {
      preparedStmnt.setString(1, comment_id);
      preparedStmnt.executeUpdate();

    } catch (SQLException e) {
      System.out.println(e);
    }

  }
}
