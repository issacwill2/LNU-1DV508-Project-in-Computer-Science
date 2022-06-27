package cookbook.db.entities;

public class IngredientEntity extends BaseEntity {
    private String name;

    public IngredientEntity(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}