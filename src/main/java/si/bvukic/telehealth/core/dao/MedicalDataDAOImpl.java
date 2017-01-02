/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package si.bvukic.telehealth.core.dao;

import java.util.Date;
import java.util.List;
import org.hibernate.Filter;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import si.bvukic.telehealth.core.model.MedicalData;
import si.bvukic.telehealth.core.model.MedicalParameter;
import si.bvukic.telehealth.core.model.User;

/**
 *
 * @author bostjanvukic
 */
public class MedicalDataDAOImpl implements MedicalDataDAO {

    private static final Logger LOG = LoggerFactory.getLogger(MedicalDataDAOImpl.class);
    
    private SessionFactory sessionFactory;
     
    public void setSessionFactory(SessionFactory sessionFactory){
        this.sessionFactory = sessionFactory;
    }
    
    @Override
    public MedicalData getMedicalDataById(Long id) {
        Session session = this.sessionFactory.getCurrentSession();      
        MedicalData medicalData = (MedicalData) session.load(MedicalData.class, new Long(id));
        LOG.info("MedicalData " + medicalData + " loaded successfully");
        LOG.debug("MedicalData details [" + medicalData + "]");
        return medicalData;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<MedicalData> listMedicalData() {
        Session session = this.sessionFactory.getCurrentSession();
        List<MedicalData> medicalDatasList = session.createQuery("from MedicalData").list();
        for(MedicalData medicalData : medicalDatasList){
            LOG.debug("MedicalData List::"+medicalData);
        }
        
        return medicalDatasList;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public List<MedicalData> listMedicalData(User user) {
        Session session = this.sessionFactory.getCurrentSession();
        
        Filter filter = session.enableFilter("medicalDataFilter");
        filter.setParameter("userIdParam", user.getId());
        List<MedicalData> medicalDatasList = session.createQuery("from MedicalData").list();
        session.disableFilter("medicalDataFilter");
        
        for(MedicalData medicalData : medicalDatasList){
            LOG.debug("MedicalData List::"+ medicalData);
        }
        
        return medicalDatasList;
    }
       
    @SuppressWarnings("unchecked")
    @Override
    public List<MedicalData> listMedicalData(User user, MedicalParameter medicalParameter) {
        Session session = this.sessionFactory.getCurrentSession();
        
        Filter filter = session.enableFilter("medicalDataFilter");
        filter.setParameter("userIdParam", user.getId());
        filter.setParameter("medicalParameterIdParam", medicalParameter.getId());
        List<MedicalData> medicalDatasList = session.createQuery("from MedicalData").list();
        session.disableFilter("medicalDataFilter");
        
        for(MedicalData medicalData : medicalDatasList){
            LOG.debug("MedicalData List::"+ medicalData);
        }
        
        return medicalDatasList;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<MedicalData> listMedicalData(User user, Date startDate, Date stopDate) {
        Session session = this.sessionFactory.getCurrentSession();
        
        Filter filter = session.enableFilter("medicalDataFilter");
        filter.setParameter("userIdParam", user.getId());
        filter.setParameter("startDateParam", startDate);
        filter.setParameter("stopDateParam", stopDate);
        List<MedicalData> medicalDatasList = session.createQuery("from MedicalData").list();
        session.disableFilter("medicalDataFilter");
        
        for(MedicalData medicalData : medicalDatasList){
            LOG.debug("MedicalData List::"+ medicalData);
        }
        
        return medicalDatasList;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<MedicalData> listMedicalData(User user, MedicalParameter medicalParameter, Date startDate, Date stopDate) {
        Session session = this.sessionFactory.getCurrentSession();
        
        Filter filter = session.enableFilter("medicalDataFilter");
        filter.setParameter("userIdParam", user.getId());
        filter.setParameter("medicalParameterIdParam", medicalParameter.getId());
        filter.setParameter("startDateParam", startDate);
        filter.setParameter("stopDateParam", stopDate);
        List<MedicalData> medicalDatasList = session.createQuery("from MedicalData").list();
        session.disableFilter("medicalDataFilter");
        
        for(MedicalData medicalData : medicalDatasList){
            LOG.debug("MedicalData List::"+ medicalData);
        }
        
        return medicalDatasList;
    }

    
    @Override
    public void addMedicalData(MedicalData medicalData) {
        Session session = this.sessionFactory.getCurrentSession();
        session.persist(medicalData);
        LOG.info("MedicalData " + medicalData +  " added successfully");
        LOG.debug("MedicalData details [" + medicalData + "]");
    }
 
    @Override
    public void updateMedicalData(MedicalData medicalData) {
        Session session = this.sessionFactory.getCurrentSession();
        session.update(medicalData);
        LOG.info("MedicalData " + medicalData + " updated successfully");
        LOG.debug("MedicalData details [" + medicalData + "]");
    }

    @Override
    public void removeMedicalData(Long id) {
        Session session = this.sessionFactory.getCurrentSession();
        MedicalData medicalData = (MedicalData) session.load(MedicalData.class, new Long(id));
        if(null != medicalData) {
            session.delete(medicalData);
        }
        LOG.info("MedicalData " + medicalData + " deleted successfully");
        LOG.debug("MedicalData details [" + medicalData + "]");
    }

    @Override
    public MedicalData getMedicalDataById(String id) {
        
        return this.getMedicalDataById(new Long(id));
    }

    @Override
    public int countMedicalData(User user, MedicalParameter medicalParameter, Date start, Date stop) {
        Session session = this.sessionFactory.getCurrentSession();
        
        Filter filter = session.enableFilter("medicalDataFilter");
        filter.setParameter("userIdParam", user.getId());
        filter.setParameter("medicalParameterIdParam", medicalParameter.getId());
        filter.setParameter("startDateParam", start);
        filter.setParameter("stopDateParam", stop);
        Long count = (Long) session.createQuery("select count(*) from MedicalData").uniqueResult();
        session.disableFilter("medicalDataFilter");
        LOG.debug("Count of MedicalData with medicalParameter.id: {} for user {}, timeframe: {} -> {}, is: {}", 
                user.getUsername(), medicalParameter.getId(), start, stop, count);
        
        return count.intValue();
    }

    @Override
    public Long saveMedicalData(MedicalData medicalData) {
        Session session = this.sessionFactory.getCurrentSession();
        Long id = (Long) session.save(medicalData);
        LOG.info("MedicalData " + medicalData +  " saved successfully with id: {}", id);
        LOG.debug("MedicalData details [" + medicalData + "]");
    
        return id;
    }
    
}
