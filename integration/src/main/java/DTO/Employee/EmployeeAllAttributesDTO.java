package DTO.Employee;

import java.util.Date;


/**
 * Used for creating a new Employee in the Canonical Data Model
 * Contains all possible fields for an Employee
 * */
public class EmployeeAllAttributesDTO {

    private String email;
    private String password;
    private String first_name;
    private String last_name;
    private String position;
    private String private_email;
    private Date birth_date;
    private String employment_status;
    private String postal_address;


    public EmployeeAllAttributesDTO() {
    }


    public EmployeeAllAttributesDTO(String email, String password, String first_name, String last_name, String profile_picture_url, String position, String private_email, Date birth_date, String status, String postal_address) {
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

    // as Json with only the fields that are not null

    public String toString() {
        String result = "{";
        if (email != null) {
            result += "\"email\":\"" + email + "\",";
        }
        if (password != null) {
            result += "\"password\":\"" + password + "\",";
        }
        if (first_name != null) {
            result += "\"first_name\":\"" + first_name + "\",";
        }
        if (last_name != null) {
            result += "\"last_name\":\"" + last_name + "\",";
        }
        if (position != null) {
            result += "\"position\":\"" + position + "\",";
        }
        if (private_email != null) {
            result += "\"private_email\":\"" + private_email + "\",";
        }
        if (birth_date != null) {
            result += "\"birth_date\":\"" + birth_date + "\",";
        }
        if (employment_status != null) {
            result += "\"employment_status\":\"" + employment_status + "\",";
        }
        if (postal_address != null) {
            result += "\"postal_address\":\"" + postal_address + "\",";
        }
        if (result.length() > 1) {
            result = result.substring(0, result.length() - 1);
        }
        result += "}";
        return result;
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
