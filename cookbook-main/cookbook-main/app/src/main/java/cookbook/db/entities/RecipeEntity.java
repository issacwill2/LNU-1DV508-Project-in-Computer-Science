package cookbook.db.entities;

import java.util.ArrayList;
import java.util.List;

public class RecipeEntity extends BaseEntity {
  private String id;
  private String name;
  private String shortDesc;
  private String longDesc;
  private boolean starred = false;
  private List<QuantifiedIngredientEntity> quantifiedIngredients = new ArrayList<>();
  private List<TagEntity> tags = new ArrayList<>();
  private List<CommentEntity> comments = new ArrayList<>();

  public RecipeEntity(String id, String name, String shortDesc, String longDesc, Boolean starred) {
    this.id = id;
    this.name = name;
    this.shortDesc = shortDesc;
    this.longDesc = longDesc;
    this.starred = starred;
  }

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getShortDesc() {
    return shortDesc;
  }

  public String getLongDesc() {
    return longDesc;
  }

  public boolean getStarred() {
    return starred;
  }

  public void setStarred(boolean starred) {
    this.starred = starred;
  }

  public void addTag(TagEntity tag) {
    tags.add(tag);
  }

  public void addQuantifiedIngredient(QuantifiedIngredientEntity qi) {
    quantifiedIngredients.add(qi);
  }

  public void addComment(CommentEntity comment) {
    comments.add(comment);
  }

  public List<CommentEntity> getComments() {
    return comments;
  }

  public List<QuantifiedIngredientEntity> getQuantifiedIngredients() {
      return quantifiedIngredients;
  }
  public List<TagEntity> getTags() {
    return tags;
  }

}