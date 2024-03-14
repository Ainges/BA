package DTO;

public class EmployeeDTO {
    String id;
    String username;
    String email;
    String fullName;
    String pictureURI;
    String companyAndPosition;

    public EmployeeDTO(String id, String username, String email, String fullName, String pictureURI, String companyAndPosition) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.fullName = fullName;
        this.pictureURI = pictureURI;
        this.companyAndPosition = companyAndPosition;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPictureURI() {
        return pictureURI;
    }

    public void setPictureURI(String pictureURI) {
        this.pictureURI = pictureURI;
    }

    public String getCompanyAndPosition() {
        return companyAndPosition;
    }

    public void setCompanyAndPosition(String companyAndPosition) {
        this.companyAndPosition = companyAndPosition;
    }
}
