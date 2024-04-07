package repositories;

import Entities.Employment_status;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class Employment_statusRepository implements PanacheRepository<Employment_status> {

    public Employment_statusRepository() {
    }
}
