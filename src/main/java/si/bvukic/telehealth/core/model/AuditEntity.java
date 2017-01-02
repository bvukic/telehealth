/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package si.bvukic.telehealth.core.model;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size; 

/**
 *
 * @author bostjanvukic
 */
@MappedSuperclass  
public abstract class AuditEntity extends GenericEntity {  
    
    @Column(name = "createdat")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
  
    @Size(max = 20)  
    @Column(name = "createdby", length = 16)  
    private String createdBy;  
  
    @Column(name = "updatedat")  
    @Temporal(TemporalType.TIMESTAMP)  
    private Date updatedAt;  
  
    @Size(max = 20)  
    @Column(name = "updatedby", length = 16)  
    private String updatedBy;  
  
    public Date getCreatedAt() {  
            return createdAt;  
    }  
  
    public void setCreatedAt(Date createdAt) {  
            this.createdAt = createdAt;  
    }  
  
    public String getCreatedBy() {  
            return createdBy;  
    }  
  
    public void setCreatedBy(String createdBy) {  
            this.createdBy = createdBy;  
    }  
  
    public Date getUpdatedAt() {  
            return updatedAt;  
    }  
  
    public void setUpdatedAt(Date updatedAt) {  
            this.updatedAt = updatedAt;  
    }  
  
    public String getUpdatedBy() {  
            return updatedBy;  
    }  
  
    public void setUpdatedBy(String updatedBy) {  
            this.updatedBy = updatedBy;  
    }  
  
    /** 
     * Sets createdAt before insert 
     */  
    @PrePersist  
    public void setCreationDate() {  
        this.createdAt = new Date();  
    }  
  
    /** 
     * Sets updatedAt before update 
     */  
    @PreUpdate  
    public void setChangeDate() {  
        this.updatedAt = new Date();  
    }  
  
}  