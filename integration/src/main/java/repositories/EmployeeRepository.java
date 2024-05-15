package repositories;

import Entities.Employee;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;


@ApplicationScoped
public class EmployeeRepository implements PanacheRepository<Employee> {
    public EmployeeRepository() {
    }


    @Transactional
    public boolean isEmailAvailable(String email) {
        return find("email", email).count() == 0;
    }
}
