package repositories;

import Entities.OneTimePasswordEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class OneTimePasswordEntityRepository implements PanacheRepository<OneTimePasswordEntity> {

    public OneTimePasswordEntityRepository() {
    }

    @Transactional
    public OneTimePasswordEntity findByPasswordAndName(String one_time_password, String first_name, String last_name) {
        return find("one_time_password = ?1 and first_name = ?2 and last_name = ?3", one_time_password, first_name, last_name).firstResult();
    }
}
