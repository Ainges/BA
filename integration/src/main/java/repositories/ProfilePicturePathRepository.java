package repositories;

import Entities.ProfilepicturePath;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ProfilePicturePathRepository implements PanacheRepository<ProfilepicturePath> {

    public ProfilePicturePathRepository() {
    }
}
