/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package si.bvukic.telehealth.core.model;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Table;
import si.bvukic.telehealth.core.model.converter.MedicalDataPeriodConverter;
import si.bvukic.telehealth.core.model.converter.MedicalDataTypeConverter;

/**
 *
 * @author bostjanvukic
 */
@Entity
@Table(name = "medicalparameter")
public class MedicalParameter extends GenericEntity {
    
    @Column(name = "name", nullable = false, length = 32)
    private String name;
    
    @Column(name = "title", nullable = false, length = 32)
    private String title;
    
    @Column(name = "details", nullable = false, length = 256)
    private String details;
    
    @Column(name = "voiceprompt", nullable = false, length = 256)
    private String voicePrompt;
    
    @Column(name = "medicaldataperiod", nullable = false, length = 2)
    @Convert(converter = MedicalDataPeriodConverter.class)
    private MedicalDataPeriod dataPeriod;
    
    @Column(name = "medicaldatatype", nullable = false, length = 2)
    @Convert(converter = MedicalDataTypeConverter.class)
    private MedicalDataType dataType;
    
    @Column(name = "datavalueunit", nullable = true, length = 11)
    private String dataValueUnit;
    
    @Column(name = "datavaluelabel", nullable = false, length = 24)
    private String dataValueLabel;
    
    @Column(name = "datavaluedefault", nullable = false, columnDefinition = "FLOAT")
    private float dataValueDefault;
    
    @Column(name = "datavaluestep", nullable = false, columnDefinition = "FLOAT")
    private float dataValueStep;
    
    @Column(name = "datavaluemin", nullable = false, columnDefinition = "FLOAT")
    private float dataValueMin;
    
    @Column(name = "datavaluemax", nullable = false, columnDefinition = "FLOAT")
    private float dataValueMax;
    
    @Column(name = "dataentriesperperiod", nullable = false)
    private int dataEntriesPerPeriod;
    
    @Column(name = "instructions", nullable = false)
    private String instructions;
    
    public String getName() {
        return name;
    }

    public String getTitle() {
        return title;
    }

    public String getDetails() {
        return details;
    }

    public String getVoicePrompt() {
        return voicePrompt;
    }

    public MedicalDataPeriod getDataPeriod() {
        return dataPeriod;
    }

    public MedicalDataType getDataType() {
        return dataType;
    }

    public String getDataValueUnit() {
        return dataValueUnit;
    }

    public String getDataValueLabel() {
        return dataValueLabel;
    }

    public float getDataValueDefault() {
        return dataValueDefault;
    }

    public float getDataValueStep() {
        return dataValueStep;
    }

    public float getDataValueMin() {
        return dataValueMin;
    }

    public float getDataValueMax() {
        return dataValueMax;
    }

    public int getDataEntriesPerPeriod() {
        return dataEntriesPerPeriod;
    }

    public String getInstructions() {
        return instructions;
    }

    
    
    public void setName(String name) {
        this.name = name;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public void setVoicePrompt(String voicePrompt) {
        this.voicePrompt = voicePrompt;
    }

    public void setDataPeriod(MedicalDataPeriod dataPeriod) {
        this.dataPeriod = dataPeriod;
    }

    public void setDataType(MedicalDataType dataType) {
        this.dataType = dataType;
    }

    public void setDataValueUnit(String dataValueUnit) {
        this.dataValueUnit = dataValueUnit;
    }

    public void setDataValueLabel(String dataValueLabel) {
        this.dataValueLabel = dataValueLabel;
    }

    public void setDataValueDefault(float dataValueDefault) {
        this.dataValueDefault = dataValueDefault;
    }

    public void setDataValueStep(float dataValueStep) {
        this.dataValueStep = dataValueStep;
    }

    public void setDataValueMin(float dataValueMin) {
        this.dataValueMin = dataValueMin;
    }

    public void setDataValueMax(float dataValueMax) {
        this.dataValueMax = dataValueMax;
    }

    public void setDataEntriesPerPeriod(int dataEntriesPerPeriod) {
        this.dataEntriesPerPeriod = dataEntriesPerPeriod;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }
    
    
    
}
