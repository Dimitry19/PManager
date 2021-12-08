package cm.packagemanager.pmanager.common.ent.vo;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

// @Entity annotation specifies that the class is mapped to a database table.
//@Entity
public class Movie {

    // @Id annotation specifies the primary key of an entity.
    @Id
    // @GeneratedValue provides the generation strategy specification for the primary key values.
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String title;
    private String description;
    private boolean released;

    public Movie() {
        // Default constructor of initialization purpose.
    }

    // Getters and Setters.
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isReleased() {
        return released;
    }

    public void setReleased(boolean released) {
        this.released = released;
    }
}
