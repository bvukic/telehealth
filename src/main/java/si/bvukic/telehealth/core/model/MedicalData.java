/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package si.bvukic.telehealth.core.model;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.Filters;
import org.hibernate.annotations.ParamDef;

/**
 *
 * @author bostjanvukic
 */
@Entity
@Table(name = "medicaldata")
@FilterDef(name = "medicalDataFilter", parameters = {
        @ParamDef(name = "userIdParam", type = "long"),
        @ParamDef(name = "medicalParameterIdParam", type = "long"),
        @ParamDef(name = "startDateParam", type = "timestamp"),
        @ParamDef(name = "stopDateParam", type = "timestamp")
})
@Filters({
    @Filter(name = "medicalDataFilter", 
            condition = "user_id = :userIdParam"),
    @Filter(name = "medicalDataFilter", 
            condition = "medicalparameter_id = :medicalParameterIdParam"),
    @Filter(name = "medicalDataFilter", 
            condition = "datavaluedate between :startDateParam and :stopDateParam")
})
public class MedicalData extends AuditEntity {
    
    @Column(name = "datavalue", nullable = false, columnDefinition = "FLOAT")
    private float dataValue;
    
    @Column(name = "datavaluedate", nullable = false)    
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataValueDate;
    
    @ManyToOne
    @JoinColumn(name="user_id", nullable = false, columnDefinition = "BIGINT UNSIGNED")
    private User user;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="medicalparameter_id", nullable = false, columnDefinition = "BIGINT UNSIGNED")
    private MedicalParameter medicalParameter;

    public float getDataValue() {
        return dataValue;
    }
    
    public Date getDataValueDate() {
        return dataValueDate;
    }

    public User getUser() {
        return user;
    }

    public MedicalParameter getMedicalParameter() {
        return medicalParameter;
    }

    public void setDataValue(float dataValue) {
        this.dataValue = dataValue;
    }

    public void setDataValueDate(Date dataValueDate) {
        this.dataValueDate = dataValueDate;
    }

    public void setUser(User user) {
        this.user = user;
    }
    
    @Transient
    public boolean isValueValid() {
        float min = medicalParameter.getDataValueMin();
        float max = medicalParameter.getDataValueMax();
        return (dataValue >= min && dataValue  <= max);
    }
    
    @Transient
    public void setDefaultValue() {
        dataValue = medicalParameter.getDataValueDefault();;
    }
    
    public void setMedicalParameter(MedicalParameter medicalParameter) {
        this.medicalParameter = medicalParameter;
    }

    
    
}
