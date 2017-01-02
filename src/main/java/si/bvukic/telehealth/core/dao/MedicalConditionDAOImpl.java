/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package si.bvukic.telehealth.core.dao;

import java.util.HashMap;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import si.bvukic.telehealth.core.model.MedicalCondition;

/**
 *
 * @author bostjanvukic
 */
public class MedicalConditionDAOImpl implements MedicalConditionDAO {

    private static final Logger LOG = LoggerFactory.getLogger(MedicalConditionDAOImpl.class);
    
    private SessionFactory sessionFactory;
     
    public void setSessionFactory(SessionFactory sessionFactory){
        this.sessionFactory = sessionFactory;
    }
    
    @Override
    public MedicalCondition getMedicalConditionById(Long id) {
        Session session = this.sessionFactory.getCurrentSession();      
        MedicalCondition medicalCondition = (MedicalCondition) session.load(MedicalCondition.class, new Long(id));
        LOG.info("MedicalCondition " + medicalCondition.getName() + " loaded successfully");
        LOG.debug("MedicalCondition details [" + medicalCondition + "]");
        return medicalCondition;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<MedicalCondition> listMedicalConditions() {
        Session session = this.sessionFactory.getCurrentSession();
        List<MedicalCondition> medicalConditionsList = session.createQuery("from MedicalCondition").list();
        for(MedicalCondition medicalCondition : medicalConditionsList){
            LOG.debug("MedicalCondition List::"+medicalCondition);
        }
        
        return medicalConditionsList;
    }
    
    @Override
    public void addMedicalCondition(MedicalCondition medicalCondition) {
        Session session = this.sessionFactory.getCurrentSession();
        session.persist(medicalCondition);
        LOG.info("MedicalCondition " + medicalCondition.getName() +  " added successfully");
        LOG.debug("MedicalCondition details [" + medicalCondition + "]");
    }
 
    @Override
    public void updateMedicalCondition(MedicalCondition medicalCondition) {
        Session session = this.sessionFactory.getCurrentSession();
        session.update(medicalCondition);
        LOG.info("MedicalCondition " + medicalCondition.getName() + " updated successfully");
        LOG.debug("MedicalCondition details [" + medicalCondition + "]");
    }

    @Override
    public void removeMedicalCondition(Long id) {
        Session session = this.sessionFactory.getCurrentSession();
        MedicalCondition medicalCondition = (MedicalCondition) session.load(MedicalCondition.class, new Long(id));
        if(null != medicalCondition) {
            session.delete(medicalCondition);
        }
        LOG.info("MedicalCondition " + medicalCondition.getName() + " deleted successfully");
        LOG.debug("MedicalCondition details [" + medicalCondition + "]");
    }

    @Override
    public MedicalCondition getMedicalConditionById(String id) {
        
        return this.getMedicalConditionById(new Long(id));
    }

    @Override
    public HashMap<String, MedicalCondition> hashMapMedicalConditions() {
        List<MedicalCondition> medicalConditionsList = this.listMedicalConditions();
        HashMap<String, MedicalCondition> medicalConditionsHashMap = new HashMap<String, MedicalCondition>();
        for(MedicalCondition medicalCondition : medicalConditionsList){
            medicalConditionsHashMap.put(medicalCondition.getId().toString(), medicalCondition);
            LOG.debug("MedicalCondition HashMap::"+medicalCondition.getId().toString() + "::" + medicalCondition);
        }
        
        return medicalConditionsHashMap;
    }
    
}
