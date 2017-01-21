/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package si.bvukic.telehealth.core.model;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import si.bvukic.telehealth.core.model.converter.GenderConverter;
import si.bvukic.telehealth.core.security.AppAuthority;

/**
 *
 * @author bostjanvukic
 */
@Entity
@Table(name="user", uniqueConstraints = {
		@UniqueConstraint(columnNames = "username")})
@SuppressWarnings("ConsistentAccessType")
public class User extends GenericEntity implements UserDetails {
    
    @Column(name = "username", unique = true, nullable = false, length = 16) 
    private String username;
    
    @Column(name = "password") 
    private String password;
    
    @Column(name = "enabled", nullable = false, columnDefinition = "TINYINT(1)")
    private boolean enabled;

    @NotNull
    @Column(name = "email", nullable = false)
    private String email;
    
    @Column(name = "firstname")
    private String firstName;
    
    @Column(name = "lastname")
    private String lastName;
    
    @NotNull
    @Column(name = "phone", nullable = false, unique = true)
    private String phone;
     
    @ManyToMany(fetch = FetchType.EAGER)
    @Cascade({CascadeType.MERGE})
    @JoinTable(name = "user_role", 
                joinColumns = {@JoinColumn(name = "user_id")}, 
                inverseJoinColumns = {@JoinColumn(name = "role_id")})
    private Set<Role> roles = new HashSet<Role>();
    
    @ManyToMany(fetch = FetchType.EAGER)
    @Cascade({CascadeType.MERGE})
    @JoinTable(name = "user_medicalcondition",
                joinColumns = {@JoinColumn(name = "user_id")}, 
                inverseJoinColumns = {@JoinColumn(name = "medicalcondition_id")})
    private Set<MedicalCondition> medicalConditions = new HashSet<MedicalCondition>();
    
    @Column(name = "gender", nullable = false, length = 1)
    @Convert(converter = GenderConverter.class)
    private Gender gender;
    
    @Column(name = "birthdate", nullable = false)  
    @Temporal(TemporalType.DATE)  
    @DateTimeFormat(pattern = "dd.MM.yyyy")
    @NotNull
    @Past
    private Date birthDate;
    
    @Transient
    private final String PERMISSION_PREFIX = "ROLE_PERMISSION_";

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhone() {
        return phone;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public Set<MedicalCondition> getMedicalConditions() {
        return medicalConditions;
    }

    public Gender getGender() {
        return gender;
    }

    public Date getBirthDate() {
        return birthDate;
    }
    
    @Transient
    public int getAge() {
        LocalDate start = this.birthDate.toInstant().
                atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate end = LocalDate.now(ZoneId.systemDefault());
        
        return (int) ChronoUnit.YEARS.between(start, end);
    }
    
    @Transient
    public List<MedicalParameter> getMedicalParameters() {
        List<MedicalParameter> parameters = new ArrayList();
        for (MedicalCondition condition : this.medicalConditions) {
            //first remove then add to avoid duplicated items
            parameters.removeAll(condition.getMedicalParameter());
            parameters.addAll(condition.getMedicalParameter());
        }
        
        return parameters;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public void setMedicalConditions(Set<MedicalCondition> medicalConditions) {
        this.medicalConditions = medicalConditions;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }
   
    @Override
    @Transient
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<AppAuthority> authorities = new HashSet<AppAuthority>();
        for (Role role : this.roles) {
            for (Permission permission : role.getPermissions()) {
               AppAuthority appAuthority = new AppAuthority(PERMISSION_PREFIX + permission.getName());
                authorities.add(appAuthority);
            }
        }
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public String toString() {
        return "User{" + "username=" + username + ", password=" + password + ", enabled=" + enabled + ", email=" + email + ", firstName=" + firstName + ", lastName=" + lastName + ", phone=" + phone + ", roles=" + roles + ", medicalConditions=" + medicalConditions + ", gender=" + gender + ", birthDate=" + birthDate + '}';
    }

    
    
}
