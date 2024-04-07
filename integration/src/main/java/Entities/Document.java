package Entities;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

import java.util.UUID;

@Entity
public class Document {

    @Id @GeneratedValue
    private UUID id;

    private String name;
    private String url;
    @ManyToOne
    private Employee employee;
}
