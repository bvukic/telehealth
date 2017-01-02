/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package si.bvukic.telehealth.core.service;

import java.util.HashMap;
import java.util.List;
import si.bvukic.telehealth.core.model.MedicalCondition;

/**
 *
 * @author bostjanvukic
 */
public interface MedicalConditionService {

    public MedicalCondition getMedicalConditionById(Long id);
    public MedicalCondition getMedicalConditionById(String id);
    public List<MedicalCondition> listMedicalConditions();
    public HashMap<String, MedicalCondition> hashMapMedicalConditions();
    public void addMedicalCondition(MedicalCondition medicalCondition);
    public void updateMedicalCondition(MedicalCondition medicalCondition);
    public void removeMedicalCondition(Long id);
    
}
