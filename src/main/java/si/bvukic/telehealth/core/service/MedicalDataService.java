/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package si.bvukic.telehealth.core.service;

import java.util.Date;
import java.util.List;
import si.bvukic.telehealth.core.model.MedicalData;
import si.bvukic.telehealth.core.model.MedicalParameter;
import si.bvukic.telehealth.core.model.User;



/**
 *
 * @author bostjanvukic
 */
public interface MedicalDataService {

    public MedicalData getMedicalDataById(Long id);
    public MedicalData getMedicalDataById(String id);
    public List <MedicalData> listMedicalData();
    public List <MedicalData> listMedicalData(User user);
    public List <MedicalData> listMedicalData(User user, Date start, Date stop);
    public List <MedicalData> listMedicalData(User user, MedicalParameter medicalParameter);
    public List <MedicalData> listMedicalData(User user, MedicalParameter medicalParameter, Date start, Date stop);
    public boolean isDataMissing(User user, MedicalParameter medicalParameter);
    public void addMedicalData(MedicalData medicalData);
    public Long saveMedicalData(MedicalData medicalData);
    public void updateMedicalData(MedicalData medicalData);
    public void removeMedicalData(Long id);
    public List<MedicalData> missingMedicalDataList(User user);
    
}
