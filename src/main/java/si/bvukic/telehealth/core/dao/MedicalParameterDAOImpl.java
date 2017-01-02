/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package si.bvukic.telehealth.core.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Set;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import si.bvukic.telehealth.core.model.MedicalCondition;
import si.bvukic.telehealth.core.model.MedicalParameter;
import si.bvukic.telehealth.core.model.User;

/**
 *
 * @author bostjanvukic
 */
public class MedicalParameterDAOImpl implements MedicalParameterDAO {

    private static final Logger LOG = LoggerFactory.getLogger(MedicalParameterDAOImpl.class);
    
    private SessionFactory sessionFactory;
     
    public void setSessionFactory(SessionFactory sessionFactory){
        this.sessionFactory = sessionFactory;
    }
    
    @Override
    public MedicalParameter getMedicalParameterById(Long id) {
        Session session = this.sessionFactory.getCurrentSession();      
        MedicalParameter medicalCondition = (MedicalParameter) session.load(MedicalParameter.class, new Long(id));
        LOG.info("MedicalParameter " + medicalCondition.getName() + " loaded successfully");
        LOG.debug("MedicalParameter details [" + medicalCondition + "]");
        return medicalCondition;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<MedicalParameter> listMedicalParameters() {
        Session session = this.sessionFactory.getCurrentSession();
        List<MedicalParameter> medicalConditionsList = session.createQuery("from MedicalParameter").list();
        for(MedicalParameter medicalCondition : medicalConditionsList){
            LOG.debug("MedicalParameter List::"+medicalCondition);
        }
        
        return medicalConditionsList;
    }
    
    @Override
    public void addMedicalParameter(MedicalParameter medicalCondition) {
        Session session = this.sessionFactory.getCurrentSession();
        session.persist(medicalCondition);
        LOG.info("MedicalParameter " + medicalCondition.getName() +  " added successfully");
        LOG.debug("MedicalParameter details [" + medicalCondition + "]");
    }
 
    @Override
    public void updateMedicalParameter(MedicalParameter medicalCondition) {
        Session session = this.sessionFactory.getCurrentSession();
        session.update(medicalCondition);
        LOG.info("MedicalParameter " + medicalCondition.getName() + " updated successfully");
        LOG.debug("MedicalParameter details [" + medicalCondition + "]");
    }

    @Override
    public void removeMedicalParameter(Long id) {
        Session session = this.sessionFactory.getCurrentSession();
        MedicalParameter medicalCondition = (MedicalParameter) session.load(MedicalParameter.class, new Long(id));
        if(null != medicalCondition) {
            session.delete(medicalCondition);
        }
        LOG.info("MedicalParameter " + medicalCondition.getName() + " deleted successfully");
        LOG.debug("MedicalParameter details [" + medicalCondition + "]");
    }

    @Override
    public MedicalParameter getMedicalParameterById(String id) {
        
        return this.getMedicalParameterById(new Long(id));
    }

    @Override
    public HashMap<String, MedicalParameter> hashMapMedicalParameters() {
        List<MedicalParameter> medicalConditionsList = this.listMedicalParameters();
        HashMap<String, MedicalParameter> medicalConditionsHashMap = new HashMap<String, MedicalParameter>();
        for(MedicalParameter medicalCondition : medicalConditionsList){
            medicalConditionsHashMap.put(medicalCondition.getId().toString(), medicalCondition);
            LOG.debug("MedicalParameter HashMap::"+medicalCondition.getId().toString() + "::" + medicalCondition);
        }
        
        return medicalConditionsHashMap;
    }

    
}
