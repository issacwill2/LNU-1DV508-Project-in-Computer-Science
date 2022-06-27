package cookbook.db.services;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import cookbook.GlobalState;
import cookbook.db.DatabaseManager;
import cookbook.db.entities.*;

public class RecipeService {

  // get all the recipes information
  public static List<RecipeEntity> getAll() {
      List<RecipeEntity> recipes = new ArrayList<>();
      Connection conn = DatabaseManager.getConn();
      try (PreparedStatement preparedStmnt = conn.prepareStatement("""
              SELECT
                  r.*,
                  us.recipe_id IS NOT NULL is_starred
              FROM cookbook.recipe r
              LEFT JOIN cookbook.user_starred us ON us.recipe_id = r.id AND us.user_id = (?);
              """)) {
          preparedStmnt.setString(1, GlobalState.user.getId());
          ResultSet result = preparedStmnt.executeQuery();

          while (result.next()) {
              String recipeId = result.getString("id");
              RecipeEntity recipe = new RecipeEntity(
                      recipeId,
                      result.getString("name"),
                      result.getString("short_desc"),
                      result.getString("long_desc"),

                      result.getBoolean("is_starred"));

              PreparedStatement tagsStmnt = conn.prepareStatement("""
                      SELECT * FROM tag t inner join recipe_tag rt on rt.tag_id = t.id where rt.recipe_id = (?);
                      """);
              tagsStmnt.setString(1, recipe.getId());
              ResultSet tagResult = tagsStmnt.executeQuery();

              while (tagResult.next()) {
                  recipe.addTag(new TagEntity(tagResult.getString("id"), tagResult.getString("name")));

              }

              PreparedStatement ingredientsStmnt = conn.prepareStatement(
                      """
                              SELECT * FROM ingredient i inner join recipe_ingredient ri on ri.ingredient_id = i.id where ri.recipe_id = (?);
                              """);
              ingredientsStmnt.setString(1, recipe.getId());
              ResultSet ingredientResult = ingredientsStmnt.executeQuery();

              while (ingredientResult.next()) {
                  recipe.addQuantifiedIngredient(new QuantifiedIngredientEntity(ingredientResult.getString("unit"),
                          ingredientResult.getFloat("amount"), new IngredientEntity(
                                  ingredientResult.getString("id"), ingredientResult.getString("name"))));

              }

              recipes.add(recipe);

              getComments(recipeId).forEach(recipe::addComment);

          }

          result.close();
      } catch (SQLException e) {
          System.out.println(e);
      }
      return recipes;
  }

  public static void add(RecipeEntity recipe) {
      Connection conn = DatabaseManager.getConn();
      try (PreparedStatement preparedStmnt = conn.prepareStatement("""
              INSERT INTO recipe VALUES ((?), (?), (?), (?));
              """)) {
          preparedStmnt.setString(1, recipe.getId());
          preparedStmnt.setString(2, recipe.getName());
          preparedStmnt.setString(3, recipe.getShortDesc());
          preparedStmnt.setString(4, recipe.getLongDesc());
          preparedStmnt.executeUpdate();
      } catch (SQLException e) {
          System.out.println(e);
      }
      for (QuantifiedIngredientEntity ingr : recipe.getQuantifiedIngredients()) {
          try (PreparedStatement preparedStmnt2 = conn.prepareStatement("""
                SELECT id, name FROM ingredient WHERE name = (?);
                """)) {
            preparedStmnt2.setString(1, ingr.getIngredient().getName());
            ResultSet result = preparedStmnt2.executeQuery();

            if (!result.next()) {
                PreparedStatement preparedStmnt3 = conn.prepareStatement("""
                        INSERT INTO ingredient VALUES ((?), (?))
                        """);
                preparedStmnt3.setString(1, ingr.getIngredient().getId());
                preparedStmnt3.setString(2, ingr.getIngredient().getName());
                preparedStmnt3.executeUpdate();

                PreparedStatement preparedStmnt4 = conn.prepareStatement("""
                        INSERT INTO recipe_ingredient VALUES ((?), (?), (?), (?))
                        """);
                preparedStmnt4.setString(1, recipe.getId());
                preparedStmnt4.setString(2, ingr.getIngredient().getId());
                preparedStmnt4.setString(3, ingr.getUnit());
                preparedStmnt4.setFloat(4, ingr.getAmount());
                preparedStmnt4.executeUpdate();
            
            } else {

            String ing_id = result.getString("id");
            String ing_unit = ingr.getUnit();
            float ing_amount = ingr.getAmount();
            
            PreparedStatement preparedStmnt4 = conn.prepareStatement("""
                        INSERT INTO recipe_ingredient VALUES ((?), (?), (?), (?))
                        """);
                preparedStmnt4.setString(1, recipe.getId());
                preparedStmnt4.setString(2, ing_id);
                preparedStmnt4.setString(3, ing_unit);
                preparedStmnt4.setFloat(4, ing_amount);
                preparedStmnt4.executeUpdate();

            }

              result.close();
          } catch (SQLException e) {
              System.out.println(e);
          }
      }
      for (TagEntity tag : recipe.getTags()) {
          try (PreparedStatement preparedStmnt5 = conn.prepareStatement("""
                    SELECT id FROM tag WHERE name = (?);
                    """)) {
                preparedStmnt5.setString(1, tag.getName());
                ResultSet result = preparedStmnt5.executeQuery();

                if (!result.next()) {
                    PreparedStatement preparedStmnt6 = conn.prepareStatement("""
                            INSERT INTO tag VALUES ((?), (?))
                            """);
                    preparedStmnt6.setString(1, tag.getId());
                    preparedStmnt6.setString(2, tag.getName());
                    preparedStmnt6.executeUpdate();

                    PreparedStatement preparedStmnt7 = conn.prepareStatement("""
                            INSERT INTO recipe_tag VALUES ((?), (?))
                            """);
                    preparedStmnt7.setString(1, recipe.getId());
                    preparedStmnt7.setString(2, tag.getId());
                    preparedStmnt7.executeUpdate();

                } else {
                    String tag_id = result.getString("id");
                    PreparedStatement preparedStmnt7 = conn.prepareStatement("""
                            INSERT INTO recipe_tag VALUES ((?), (?))
                            """);
                    preparedStmnt7.setString(1, recipe.getId());
                    preparedStmnt7.setString(2, tag_id);
                    preparedStmnt7.executeUpdate();
              }

              result.close();
          } catch (SQLException e) {
              System.out.println(e);
          }
      }
  }

  private static List<CommentEntity> getComments(String recipeId) {
    List<CommentEntity> comments = new ArrayList<>();

    Connection conn = DatabaseManager.getConn();
    try (PreparedStatement preparedStmnt = conn.prepareStatement("""
            SELECT c.id, c.body, u.id uid
            FROM comment c
            INNER JOIN recipe r ON r.id = c.recipe_id
            INNER JOIN user u ON c.user_id = u.id
            WHERE c.recipe_id = (?);
            """)) {
      preparedStmnt.setString(1, recipeId);
      ResultSet result = preparedStmnt.executeQuery();

      while (result.next()) {
        CommentEntity comment = new CommentEntity(
                result.getString("id"),
                result.getString("uid"),
                result.getString("body")
        );
        comments.add(comment);
      }

      result.close();
    } catch (SQLException e) {
      System.out.println(e);
    }
    return comments;
  }

  // add new tags uncompleted
  public static void addTag(String recipeId, String tag) {

    Connection conn = DatabaseManager.getConn();
    try (PreparedStatement preparedStmnt = conn.prepareStatement("""
            SELECT c.id, c.body, u.display_name
            FROM comment c
            INNER JOIN recipe r ON r.id = c.recipe_id
            INNER JOIN user u ON c.user_id = u.id
            WHERE c.recipe_id = (?);
            """)) {
        preparedStmnt.setString(1, recipeId);
        ResultSet result = preparedStmnt.executeQuery();

        while (result.next()) {
            // QuantifiedTagEntity tag = new QuantifiedTagEntity(new
            // TagEntity(result.getString("id"), result.getString("name")));
            // tags.add(tag);
        }

        result.close();
    } catch (SQLException e) {
        System.out.println(e);
    }
  }

  // every user can only edit or delete only his comments
  public static boolean canModifyComment(String comment, String recipeId) {
    String query = "SELECT * FROM comment WHERE recipe_id = (?) AND user_id = (?) AND body = (?)";
    Connection conn = DatabaseManager.getConn();
    try (PreparedStatement preparedStmnt = conn.prepareStatement(query)) {
        preparedStmnt.setString(1, recipeId);
        preparedStmnt.setString(2, GlobalState.user.getId());
        preparedStmnt.setString(3, comment);
        ResultSet result = preparedStmnt.executeQuery();

        if (result.next())
            return true;

        result.close();
    } catch (SQLException e) {
        System.out.println(e);
    }
    return false;
  }

  public static boolean updateStarredStatus(String recipeId) throws SQLException {
    Connection con = DatabaseManager.getConn();
    Statement stmt = con.createStatement();
    String sql1 = String.format("SELECT * FROM user_starred   " +
            "where user_id = '%s' and " +
            "recipe_id = '%s' ", GlobalState.user.getId(), recipeId);
    PreparedStatement ps = con.prepareStatement(sql1);

    ResultSet rs = ps.executeQuery();

    if (rs.next()) {
        // if duplicate, delete
        try {
            String sql = String.format("delete from user_starred " +
                    "where user_id = '%s' and recipe_id = '%s'  ", GlobalState.user.getId(), recipeId);
            stmt.executeUpdate(sql);

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    } else {
        try {
            String sql = String.format("insert into user_starred " +
                    "values('%s', '%s' ) ", GlobalState.user.getId(), recipeId);
            stmt.executeUpdate(sql);

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    return false;
  }

    // UPDATE comment
    // SET body = " edereRO"
    // WHERE recipe_id = "648b8840-086e-4dd7-932f-fbbfc0935649" AND user_id =
    // "40445a38-5f75-4224-b985-f04d36d53e43"
    // edit the comment


  // Add date to the week_list DB
  public static void adddate(String recipeId, Timestamp created_at , String userId) {


    String query = "INSERT into week_list values ((?),(?),(?))";
    Connection conn = DatabaseManager.getConn();
    try (PreparedStatement preparedStmnt = conn.prepareStatement(query)) {
      preparedStmnt.setString(1, String.valueOf(created_at));
      preparedStmnt.setString(2, recipeId);
      preparedStmnt.setString(3, userId);

      preparedStmnt.executeUpdate();


    } catch (SQLException e) {
      if (e instanceof SQLIntegrityConstraintViolationException) {
        return;
      } else {
          e.printStackTrace();
      }
    }

  }


  }



