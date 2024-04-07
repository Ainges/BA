package examples.hibernate;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class BenutzerRepository implements PanacheRepository<Benutzer> {
    public BenutzerRepository() {
    }
}
