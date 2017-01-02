/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package si.bvukic.telehealth.web.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import si.bvukic.telehealth.core.model.Gender;
import si.bvukic.telehealth.core.model.MedicalCondition;
import si.bvukic.telehealth.core.model.User;

/**
 *
 * @author vukicb
 */
public class ProfileSettings {
    
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private Gender gender;
    private Set<MedicalCondition> medicalConditions = new HashSet<>();
    private Date birthDate;

    public ProfileSettings(String firstName, String lastName, String email, String phone, Gender gender, Set<MedicalCondition> medicalConditions, Date birthDate) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.gender = gender;
        this.medicalConditions = medicalConditions;
        this.birthDate = birthDate;
    }
    
    public ProfileSettings(User user) {
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.phone = user.getPhone();
        this.gender = user.getGender();
        this.medicalConditions = user.getMedicalConditions();
        this.birthDate = user.getBirthDate();
    }

    public ProfileSettings() {
    }
    
    

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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Set<MedicalCondition> getMedicalConditions() {
        return medicalConditions;
    }

    public void setMedicalConditions(Set<MedicalCondition> medicalConditions) {
        this.medicalConditions = medicalConditions;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    @Override
    public String toString() {
        return "ProfileSettings{" + "firstName=" + firstName + ", lastName=" + lastName + ", email=" + email + ", phone=" + phone + ", gender=" + gender + ", medicalConditions=" + medicalConditions + ", birthDate=" + birthDate + '}';
    }

    
    
    
}
