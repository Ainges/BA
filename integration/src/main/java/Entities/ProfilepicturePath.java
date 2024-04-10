package Entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

import java.util.UUID;

@Entity
public class ProfilepicturePath {

    @OneToOne
    @Id
    private Employee employee;

    private String path;

    public ProfilepicturePath() {
    }

    public ProfilepicturePath(Employee employee, String path) {
        this.employee = employee;
        this.path = path;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
