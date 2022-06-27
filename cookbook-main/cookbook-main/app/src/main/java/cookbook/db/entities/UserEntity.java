package cookbook.db.entities;

public class UserEntity extends BaseEntity {
    private String displayName;
    private String username;
    private String password;
    private Boolean isAdmin;

    public UserEntity(String id, String displayName, String username, String password, Boolean isAdmin) {
        this.id = id;
        this.displayName = displayName;
        this.username = username;
        this.password = password;
        this.isAdmin = isAdmin;
    }
    
    public String getId() {
        return this.id;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getIsAdmin() {
        return this.isAdmin;
    }

    public void setIsAdmin(Boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return this.displayName;
    } 
}
