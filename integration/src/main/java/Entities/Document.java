package Entities;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;

import jakarta.persistence.ManyToOne;

@Entity
public class Document extends PanacheEntity {

    private String name;
    private String url;
    @ManyToOne
    private Employee employee;
}
