package DTO;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class SendWelcomeMessageToEmployeeDTO {


    // DTO for this Datastructure:
    //{
    //	"firstWorkingDay": "2025-02-22",
    //	"contactPerson": "Dirk Müller",
    //	"beginOfFirstWorkingDay": "08:00 Uhr",
    //	"documentsNeededForFirstWorkingDay": "Personalausweis",
    //	"newEmployeeData": {
    //		"firstName": "Max",
    //		"LastName": "Mustermann",
    //		"E-Mail": "max@mustermann.de"
    //	}
    //}


    private String firstWorkingDay;
    private String contactPerson;
    private String beginOfFirstWorkingDay;
    private String documentsNeededForFirstWorkingDay;
    private NewEmployeeData newEmployeeData;

    public String getFirstWorkingDay() {
        return firstWorkingDay;
    }

    public void setFirstWorkingDay(String firstWorkingDay) {
        this.firstWorkingDay = firstWorkingDay;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getBeginOfFirstWorkingDay() {
        return beginOfFirstWorkingDay;
    }

    public void setBeginOfFirstWorkingDay(String beginOfFirstWorkingDay) {
        this.beginOfFirstWorkingDay = beginOfFirstWorkingDay;
    }

    public String getDocumentsNeededForFirstWorkingDay() {
        return documentsNeededForFirstWorkingDay;
    }

    public void setDocumentsNeededForFirstWorkingDay(String documentsNeededForFirstWorkingDay) {
        this.documentsNeededForFirstWorkingDay = documentsNeededForFirstWorkingDay;
    }

    public NewEmployeeData getNewEmployeeData() {
        return newEmployeeData;
    }

    public void setNewEmployeeData(NewEmployeeData newEmployeeData) {
        this.newEmployeeData = newEmployeeData;
    }

    public static class NewEmployeeData {
    private String firstName;
    private String lastName;
    private String email;

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }




}
