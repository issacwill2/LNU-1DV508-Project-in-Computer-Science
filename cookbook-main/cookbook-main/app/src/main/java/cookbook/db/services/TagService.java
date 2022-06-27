package cookbook.db.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import cookbook.db.DatabaseManager;
import cookbook.db.entities.TagEntity;

public class TagService {
    public static List<TagEntity> getAll() {
        String queryString = "SELECT * FROM tag;";
        List<TagEntity> tags = new ArrayList<>();
        Connection conn = DatabaseManager.getConn();
        try (PreparedStatement preparedStmnt = conn.prepareStatement(queryString)) {
            ResultSet result = preparedStmnt.executeQuery();
            while (result.next()) {
                String id = result.getString("id");
                TagEntity user = new TagEntity(
                        id,
                        result.getString("name"));

                tags.add(user);

            }

            result.close();
        } catch (SQLException e) {
            System.out.println(e);
        }

        return tags;
    }

    public static TagEntity add(String name) {

        Connection conn = DatabaseManager.getConn();

        String id = UUID.randomUUID().toString();

        // Insert records into db.
        String insertString = "INSERT INTO user VALUES(?, ?);";

        try (PreparedStatement preparedStmnt = conn.prepareStatement(insertString)) {
            preparedStmnt.setString(1, id);
            preparedStmnt.setString(2, name);

            preparedStmnt.executeUpdate();

            return new TagEntity(id, name);

        } catch (SQLException e) {
            System.out.println(e);
            // If there's an error inserting records, return false.
        }
        return null;
    }
}
