package cookbook.db.services;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import cookbook.GlobalState;
import cookbook.db.DatabaseManager;
import cookbook.db.entities.*;

public class ScheduledRecipeEntityServices {
    
    // perform a join and get a new Scheduled recipe entity instance for a given
    public static List<ScheduledRecipeEntity> getDateSchedule(Date date) {
        List<ScheduledRecipeEntity> schedRecs = new ArrayList<>();
        Connection conn = DatabaseManager.getConn();

        // Perform join and create SchedRecipeEntity 
        try (PreparedStatement preparedStmnt = conn.prepareStatement("""
            SELECT 
                w.date, w.recipe_id, w.user_id, r.name FROM week_list w 
            INNER JOIN recipe r ON r.id = w.recipe_id
                WHERE user_id = (?) AND date = (?);
                ;
            """)) {
            preparedStmnt.setString(1, GlobalState.user.getId());
            preparedStmnt.setDate(2, date);
            ResultSet result = preparedStmnt.executeQuery();
            
            // Create Entity instance
            while (result.next()) {
                String userId = result.getString("user_id");
                String recipeId = result.getString("recipe_id");
                date = result.getDate("date");
                ScheduledRecipeEntity schedRec = new ScheduledRecipeEntity(
                    recipeId,
                    result.getString("name"),
                    userId,
                    date);
            
                // Get all Quantified ingredients for the recipe
                PreparedStatement ingredientsStmnt = conn.prepareStatement(
                    """
                    SELECT * FROM ingredient i inner join recipe_ingredient ri on ri.ingredient_id = i.id where ri.recipe_id = (?);
                    """);
                ingredientsStmnt.setString(1, recipeId);
                ResultSet ingredientResult = ingredientsStmnt.executeQuery();

                while (ingredientResult.next()) {
                    // Add all Quanitified ingredients to the Scheduled Recipe Entity
                    schedRec.addIngredient(new QuantifiedIngredientEntity(ingredientResult.getString("unit"),
                            ingredientResult.getFloat("amount"), new IngredientEntity(
                            ingredientResult.getString("id"), ingredientResult.getString("name"))));

                }        
            
            schedRecs.add(schedRec);
            
            }
        } catch (SQLException e) {
            System.out.println(e);
        }

        return schedRecs;
    }
}