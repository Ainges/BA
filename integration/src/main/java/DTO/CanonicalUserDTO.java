package DTO;

public class CanonicalUserDTO {

    private String email;
    private String first_name;
    private String last_name;
    private String profile_picture_url;
    private String position;
    private String password;

    public CanonicalUserDTO() {
    }

    public CanonicalUserDTO(String email, String first_name, String last_name, String profile_picture_url, String position, String password) {
        this.email = email;
        this.first_name = first_name;
        this.last_name = last_name;
        this.profile_picture_url = profile_picture_url;
        this.position = position;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getProfile_picture_url() {
        return profile_picture_url;
    }

    public void setProfile_picture_url(String profile_picture_url) {
        this.profile_picture_url = profile_picture_url;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
