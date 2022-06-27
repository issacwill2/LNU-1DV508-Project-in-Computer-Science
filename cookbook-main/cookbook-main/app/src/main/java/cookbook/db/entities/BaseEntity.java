package cookbook.db.entities;

public abstract class BaseEntity {
    protected String id;

    public String getId() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof BaseEntity) {
            return getId().equals(((BaseEntity) obj).getId()) ;
        }
        return super.equals(obj);
    }
}
