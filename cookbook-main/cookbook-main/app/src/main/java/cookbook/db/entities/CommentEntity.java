package cookbook.db.entities;

import java.util.UUID;

public class CommentEntity extends BaseEntity {
  private String body;
  private String userId;

  public CommentEntity(String id, String userId, String body) {
    this.id = id;
    this.userId = userId;
    this.body = body;
  }

  public CommentEntity(String userId, String body) {
    this.id = UUID.randomUUID().toString();
    this.userId = userId;
    this.body = body;
  }

  public String getId() {
    return id;
  }

  public void setBody(String body) {
    this.body = body;
  }

  public String getBody() {
    return body;
  }

  public String getUserId() {
    return userId;
  }
}
