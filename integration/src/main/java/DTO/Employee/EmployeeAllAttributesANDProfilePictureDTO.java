package DTO.Employee;

import java.util.Date;

public class EmployeeAllAttributesANDProfilePictureDTO extends EmployeeAllAttributesDTO{

    private String profile_picture_url;

    public EmployeeAllAttributesANDProfilePictureDTO() {
    }

    public EmployeeAllAttributesANDProfilePictureDTO(String profile_picture_url) {
        this.profile_picture_url = profile_picture_url;
    }

    public EmployeeAllAttributesANDProfilePictureDTO(String email, String password, String first_name, String last_name, String profile_picture_url, String position, String private_email, Date birth_date, String status, String postal_address, String profile_picture_url1) {
        super(email, password, first_name, last_name, profile_picture_url, position, private_email, birth_date, status, postal_address);
        this.profile_picture_url = profile_picture_url1;
    }

    public EmployeeAllAttributesANDProfilePictureDTO(EmployeeAllAttributesDTO employeeAllAttributesDTO, String profile_picture_url) {
        // clearer then calling the larger constructor...
        super();
        super.setEmail(employeeAllAttributesDTO.getEmail());
        super.setPassword(employeeAllAttributesDTO.getPassword());
        super.setFirst_name(employeeAllAttributesDTO.getFirst_name());
        super.setLast_name(employeeAllAttributesDTO.getLast_name());
        super.setPosition(employeeAllAttributesDTO.getPosition());
        super.setPrivate_email(employeeAllAttributesDTO.getPrivate_email());
        super.setBirth_date(employeeAllAttributesDTO.getBirth_date());
        super.setEmployment_status(employeeAllAttributesDTO.getEmployment_status());
        super.setPostal_address(employeeAllAttributesDTO.getPostal_address());

        this.profile_picture_url = profile_picture_url;
    }

    public String getProfile_picture_url() {
        return profile_picture_url;
    }

    public void setProfile_picture_url(String profile_picture_url) {
        this.profile_picture_url = profile_picture_url;
    }

    @Override
    public String toString() {
        String employeeAllAttributesAsString = super.toString();
        return employeeAllAttributesAsString.substring(0, employeeAllAttributesAsString.length() - 1) + ",\"profile_picture_url\":\"" + profile_picture_url + "\"}";
    }
}
