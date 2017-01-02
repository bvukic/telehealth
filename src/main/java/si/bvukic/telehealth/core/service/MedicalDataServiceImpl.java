/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package si.bvukic.telehealth.core.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import si.bvukic.telehealth.core.dao.MedicalDataDAO;
import si.bvukic.telehealth.core.model.MedicalData;
import si.bvukic.telehealth.core.model.MedicalDataPeriod;
import si.bvukic.telehealth.core.model.MedicalParameter;
import si.bvukic.telehealth.core.model.User;

/**
 *
 * @author bostjanvukic
 */
@Service
public class MedicalDataServiceImpl implements MedicalDataService{

    private static final Logger LOG = LoggerFactory.getLogger(MedicalDataService.class);

    
    private MedicalDataDAO medicalDataDAO;
    
    public void setMedicalDataDAO(MedicalDataDAO medicalDataDAO) {
        this.medicalDataDAO = medicalDataDAO;
    }
    
    @Override
    @Transactional
    public MedicalData getMedicalDataById(Long id) {
        return this.medicalDataDAO.getMedicalDataById(id);
    }

    @Override
    @Transactional
    public MedicalData getMedicalDataById(String id) {
        return this.medicalDataDAO.getMedicalDataById(id);
    }

    @Override
    @Transactional
    public List<MedicalData> listMedicalData() {
        return this.medicalDataDAO.listMedicalData();
    }

    @Override
    @Transactional
    public List<MedicalData> listMedicalData(User user) {
        return this.medicalDataDAO.listMedicalData(user);
    }

    @Override
    @Transactional
    public List<MedicalData> listMedicalData(User user, Date start, Date stop) {
        return this.medicalDataDAO.listMedicalData(user, start, stop);
    }

    @Override
    @Transactional
    public List<MedicalData> listMedicalData(User user, MedicalParameter medicalParameter) {
        return this.medicalDataDAO.listMedicalData(user, medicalParameter);
    }

    @Override
    @Transactional
    public List<MedicalData> listMedicalData(User user, MedicalParameter medicalParameter, Date start, Date stop) {
        return this.medicalDataDAO.listMedicalData(user, medicalParameter, start, stop);
    }

    @Override
    @Transactional
    public void addMedicalData(MedicalData medicalData) {
        this.medicalDataDAO.addMedicalData(medicalData);
    }

    @Override
    @Transactional
    public void updateMedicalData(MedicalData medicalData) {
        this.medicalDataDAO.updateMedicalData(medicalData);
    }

    @Override
    @Transactional
    public void removeMedicalData(Long id) {
        this.medicalDataDAO.removeMedicalData(id);
    }

    @Override
    @Transactional
    public boolean isDataMissing(User user, MedicalParameter medicalParameter) {

        MedicalDataPeriod period = medicalParameter.getDataPeriod(); 
        
        //Set start & stop to current time
        Calendar now = GregorianCalendar.getInstance();
        Calendar start = now;
        
        switch (period) {
            case SECOND:
                //If dataPeriod is SECOND than data needs to be entered
                return true;
            case MINUTE:
                start.set(Calendar.SECOND, 0);
                break;
            case HOUR:
                start.set(Calendar.SECOND, 0);
                start.set(Calendar.MINUTE, 0);
                break;
            case DAY:
                start.set(Calendar.SECOND, 0);
                start.set(Calendar.MINUTE, 0);
                start.set(Calendar.HOUR_OF_DAY, 0);
                break;
            case WEEK:
                start.add(Calendar.DAY_OF_WEEK, start.get(Calendar.DAY_OF_WEEK) * -1);
                start.set(Calendar.SECOND, 0);
                start.set(Calendar.MINUTE, 0);
                start.set(Calendar.HOUR_OF_DAY, 0);
                break;
            case MONTH:
                start.set(start.get(Calendar.YEAR), Calendar.MONTH, 0);
                break;
            case YEAR:
                start.set(start.get(Calendar.YEAR), 0, 0);
                break;
            default:
                // If we missed someting let user enter the data
                LOG.error("Data period {} missed in isDataNeeded() (MedicalDataService) implementation!", period);
                return true;
        }
        LOG.debug("Calculated search start time is: {}, for medicalParameter.id: {}, medicalParameter.medicalDataPerid: {}", 
                start.getTime(), medicalParameter.getId(), period);
        
        return (medicalDataDAO.countMedicalData(user, medicalParameter, start.getTime(), new Date()) == 0);
        
    }

    @Override
    @Transactional
    public Long saveMedicalData(MedicalData medicalData) {
        return this.medicalDataDAO.saveMedicalData(medicalData);
    }
    
    @Override
    @Transactional
    public List<MedicalData> missingMedicalDataList(User user) {
        LOG.info("Scaning for missing medicalData for user: {}", user);
        
        //Generate mdecialDataList
        List<MedicalData> medicalDataList = new ArrayList();
        Date now = new Date();
        int index = 0;
        for (MedicalParameter parameter : user.getMedicalParameters()) {
            if(isDataMissing(user, parameter)) {
                MedicalData data = new MedicalData();
                data.setMedicalParameter(parameter);
                data.setUser(user);
                data.setCreatedBy(user.getUsername());
                data.setCreatedAt(now);
                medicalDataList.add(index, data);
                index++;
            }
        }
        
        return medicalDataList;
    }
    
    
    
}
