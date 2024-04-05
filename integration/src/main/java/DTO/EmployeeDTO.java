package DTO;

import java.util.Date;


public class EmployeeDTO {

    private String email;
    private String password;
    private String first_name;
    private String last_name;
    private String position;
    private String private_email;
    private Date birth_date;
    private String employment_status;
    private String postal_address;


    public EmployeeDTO() {
    }


    public EmployeeDTO(String email, String password, String first_name, String last_name, String profile_picture_url, String position, String private_email, Date birth_date, String status, String postal_address) {
        this.email = email;
        this.password = password;
        this.first_name = first_name;
        this.last_name = last_name;
        this.position = position;
        this.private_email = private_email;
        this.birth_date = birth_date;
        this.employment_status = status;
        this.postal_address = postal_address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getPrivate_email() {
        return private_email;
    }

    public void setPrivate_email(String private_email) {
        this.private_email = private_email;
    }

    public Date getBirth_date() {
        return birth_date;
    }

    public void setBirth_date(Date birth_date) {
        this.birth_date = birth_date;
    }

    public String getEmployment_status() {
        return employment_status;
    }

    public void setEmployment_status(String employment_status) {
        this.employment_status = employment_status;
    }

    public String getPostal_address() {
        return postal_address;
    }

    public void setPostal_address(String postal_address) {
        this.postal_address = postal_address;
    }
}
