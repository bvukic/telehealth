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
import si.bvukic.telehealth.core.dao.MedicalParameterDAO;
import si.bvukic.telehealth.core.model.MedicalParameter;



/**
 *
 * @author bostjanvukic
 */
@Service
public class MedicalParameterServiceImpl implements MedicalParameterService {
    
    private MedicalParameterDAO medicalParameterDAO;
    
    public void setMedicalParameterDAO(MedicalParameterDAO medicalParameterDAO) {
        this.medicalParameterDAO = medicalParameterDAO;
    }
    
    @Override
    @Transactional
    public void addMedicalParameter(MedicalParameter medicalParameter) {
        this.medicalParameterDAO.addMedicalParameter(medicalParameter);
    }
 
    @Override
    @Transactional
    public void updateMedicalParameter(MedicalParameter medicalParameter) {
        this.medicalParameterDAO.updateMedicalParameter(medicalParameter);
    }
 
    @Override
    @Transactional
    public List<MedicalParameter> listMedicalParameters() {
        return this.medicalParameterDAO.listMedicalParameters();
    }
 
    @Override
    @Transactional
    public MedicalParameter getMedicalParameterById(Long id) {
        return this.medicalParameterDAO.getMedicalParameterById(id);
    }
 
    @Override
    @Transactional
    public void removeMedicalParameter(Long id) {
        this.medicalParameterDAO.removeMedicalParameter(id);
    }

    @Override
    @Transactional
    public MedicalParameter getMedicalParameterById(String id) {
        return this.medicalParameterDAO.getMedicalParameterById(id);
    }

    @Override
    @Transactional
    public HashMap<String, MedicalParameter> hashMapMedicalParameters() {
        return this.hashMapMedicalParameters();
    }
    
}
