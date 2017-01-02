/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package si.bvukic.telehealth.core.model;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import org.hibernate.annotations.Cascade;

/**
 *
 * @author bostjanvukic
 */
@Entity
@Table(name = "medicalcondition")
public class MedicalCondition extends GenericEntity {
    
    @Column(name = "name", nullable = false)
    private String name;
    
    @ManyToMany(fetch = FetchType.EAGER)
    @Cascade({org.hibernate.annotations.CascadeType.MERGE})
    @JoinTable(name = "medicalcondition_medicalparameter",
            joinColumns = {
                @JoinColumn(name = "medicalcondition_id", nullable = false, columnDefinition = "BIGINT UNSIGNED")},
            inverseJoinColumns = {
                @JoinColumn(name = "medicalparameter_id", nullable = false, columnDefinition = "BIGINT UNSIGNED")})
    private Set<MedicalParameter> medicalParameter = new HashSet<MedicalParameter>();

    public String getName() {
        return name;
    }

    public Set<MedicalParameter> getMedicalParameter() {
        return medicalParameter;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMedicalParameter(Set<MedicalParameter> medicalParameter) {
        this.medicalParameter = medicalParameter;
    }
    
}
