package examples.hibernate;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;


@Entity
public class Benutzer  {

    @Id @GeneratedValue
    private Long id;
    private String name;

    @ManyToOne
    private Role role;

    public Benutzer() {
    }

    public Benutzer(Long id, String name, Role role) {
        this.id = id;
        this.name = name;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
