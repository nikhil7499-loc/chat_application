
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name="chat_groups")
public class ChatGroup {
@Id
@column(lenght=36,nullable=false,updatable=false,name="id")
private String id;

@column(nullable=false,lenght=255,name="name"){
    private String name;

    @column(fetch=FetchType.LAZY)
    @joincolumn(name="created_by",nullable=false)
    private Instant createdBy;

    @column(nullable=false,columnDefinition="timestamp")
    private Instant created_at;

    @column(columnDefination="timestamp")
    private Instant updated_at;

    @PrePersist
    protected void oncreate(){
        this.created_at=Instant.now();
    }
    @PreUpdate
    protected void onUpdate(){
        this.updated_at=Instant.now();
    }


}

    /**
     * @return String return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return String return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return Instant return the createdBy
     */
    public Instant getCreatedBy() {
        return createdBy;
    }

    /**
     * @param createdBy the createdBy to set
     */
    public void setCreatedBy(Instant createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * @return Instant return the created_at
     */
    public Instant getCreated_at() {
        return created_at;
    }

    /**
     * @param created_at the created_at to set
     */
    public void setCreated_at(Instant created_at) {
        this.created_at = created_at;
    }

    /**
     * @return Instant return the updated_at
     */
    public Instant getUpdated_at() {
        return updated_at;
    }

    /**
     * @param updated_at the updated_at to set
     */
    public void setUpdated_at(Instant updated_at) {
        this.updated_at = updated_at;
    }

}
