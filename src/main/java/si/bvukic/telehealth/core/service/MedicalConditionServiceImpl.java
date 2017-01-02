/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package si.bvukic.telehealth.core.service;

import java.util.HashMap;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import si.bvukic.telehealth.core.dao.MedicalConditionDAO;
import si.bvukic.telehealth.core.model.MedicalCondition;


/**
 *
 * @author bostjanvukic
 */
@Service
public class MedicalConditionServiceImpl implements MedicalConditionService {

    private MedicalConditionDAO medicalConditionDAO;
    
    public void setMedicalConditionDAO(MedicalConditionDAO medicalConditionDAO) {
        this.medicalConditionDAO = medicalConditionDAO;
    }
    
    
    @Override
    @Transactional
    public MedicalCondition getMedicalConditionById(Long id) {
        return this.medicalConditionDAO.getMedicalConditionById(id);
    }

    @Override
    @Transactional
    public MedicalCondition getMedicalConditionById(String id) {
        return this.medicalConditionDAO.getMedicalConditionById(id);
    }

    @Override
    @Transactional
    public List<MedicalCondition> listMedicalConditions() {
        return this.medicalConditionDAO.listMedicalConditions();
    }

    @Override
    @Transactional
    public HashMap<String, MedicalCondition> hashMapMedicalConditions() {
        return this.medicalConditionDAO.hashMapMedicalConditions();
    }

    @Override
    @Transactional
    public void addMedicalCondition(MedicalCondition medicalCondition) {
        this.medicalConditionDAO.addMedicalCondition(medicalCondition);
    }

    @Override
    @Transactional
    public void updateMedicalCondition(MedicalCondition medicalCondition) {
        this.medicalConditionDAO.updateMedicalCondition(medicalCondition);
    }

    @Override
    @Transactional
    public void removeMedicalCondition(Long id) {
        this.medicalConditionDAO.removeMedicalCondition(id);
    }
    
}
