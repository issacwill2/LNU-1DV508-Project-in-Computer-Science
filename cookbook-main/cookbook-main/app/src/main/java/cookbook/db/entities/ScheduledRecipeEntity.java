package cookbook.db.entities;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class ScheduledRecipeEntity extends BaseEntity{
        private String recipeId;
        private String recipeName;
        private String userId;
        private Date date;
        private List<QuantifiedIngredientEntity> ingredients = new ArrayList<>();

        public ScheduledRecipeEntity(String recipeId, String recipeName, String userId, Date date) {
            this.recipeId = recipeId;
            this.recipeName = recipeName;
            this.userId = userId;
            this.date = date;
        }

        public String getRecipeId() {
            return recipeId;
        }

        public String getRecipeName() {
            return recipeName;
        }

        public String getUserId() {
            return userId;
        }

        public Date getDate() {
            return date;
        }
        
        public void addIngredient(QuantifiedIngredientEntity ingredient) {
            this.ingredients.add(ingredient);
        }

        public List<QuantifiedIngredientEntity> getIngredients() {
            return ingredients;
        }

  @Override
  public String toString() {
    return "ScheduledRecipeEntity{" +
            "recipeId='" + recipeId + '\'' +
            ", recipeName='" + recipeName + '\'' +
            ", userId='" + userId + '\'' +
            ", date=" + date +
            ", ingredients=" + ingredients +
            '}';
  }
}


