package cookbook.db.entities;

public class TagEntity extends BaseEntity {
    private String name;
    private String id;

    public TagEntity(String id, String name) {
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