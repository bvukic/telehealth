/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package si.bvukic.telehealth.core.dao;

import java.util.HashMap;
import java.util.List;
import si.bvukic.telehealth.core.model.MedicalParameter;
import si.bvukic.telehealth.core.model.User;

/**
 *
 * @author bostjanvukic
 */
public interface MedicalParameterDAO {
 
    public MedicalParameter getMedicalParameterById(Long id);
    public MedicalParameter getMedicalParameterById(String id);
    public List <MedicalParameter> listMedicalParameters();
    public HashMap<String, MedicalParameter> hashMapMedicalParameters();
    public void addMedicalParameter(MedicalParameter medicalParameter);
    public void updateMedicalParameter(MedicalParameter medicalParameter);
    public void removeMedicalParameter(Long id);

}
