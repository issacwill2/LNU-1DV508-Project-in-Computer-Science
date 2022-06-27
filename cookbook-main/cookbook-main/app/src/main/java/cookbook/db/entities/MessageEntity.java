package cookbook.db.entities;

import java.sql.Timestamp;

/**
 * id
 * fromUserId
 * toUserId
 * recipeId
 * body
 */

public class MessageEntity extends BaseEntity{
    private String id;
    private String fromUserId;
    private String toUserId;
    private String recipeId;
    private String body;
    private Timestamp createdAt;

    public MessageEntity(String id, String fromUserId, String toUserId, String recipeId, String body, Timestamp createdAt) {
        this.id = id;
        this.fromUserId = fromUserId;
        this.toUserId = toUserId;
        this.recipeId = recipeId;
        this.body = body;
        this.createdAt = createdAt;
    }

    public String getId() {
        return this.id;
    }

    public String getFromUserId() {
        return this.fromUserId;
    }

    public String getToUserId() {
        return this.toUserId;
    }

    public String getRecipeId() {
        return this.recipeId;
    }

    public String getBody() {
        return this.body;
    }

    public Timestamp getCreatedAt() {
        return this.createdAt;
    }
}
