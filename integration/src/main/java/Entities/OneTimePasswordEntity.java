package Entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.transaction.Transactional;

import java.util.UUID;


@Entity
public class OneTimePasswordEntity {
    @Id
    @GeneratedValue
    private UUID id;
    private String one_time_password;
    private String first_name;
    private String last_name;

    public OneTimePasswordEntity(String one_time_password, String first_name, String last_name) {
        this.one_time_password = one_time_password;
        this.first_name = first_name;
        this.last_name = last_name;
    }

    public OneTimePasswordEntity() {
    }



    public String getOne_time_password() {
        return one_time_password;
    }

    public void setOne_time_password(String one_time_password) {
        this.one_time_password = one_time_password;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

}
