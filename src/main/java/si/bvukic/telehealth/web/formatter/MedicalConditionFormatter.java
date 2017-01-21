/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package si.bvukic.telehealth.web.formatter;

import java.text.ParseException;
import java.util.Locale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.format.Formatter;
import si.bvukic.telehealth.core.model.MedicalCondition;
import si.bvukic.telehealth.core.service.MedicalConditionService;

/**
 *
 * @author bostjanvukic
 */
public class MedicalConditionFormatter implements Formatter<MedicalCondition> {

    private static final Logger LOG = LoggerFactory.getLogger(MedicalConditionFormatter.class);
    private MedicalConditionService medicalConditionService;

    @Autowired(required = true)
    @Qualifier(value = "medicalConditionService")
    public void setMedicalConditionService(MedicalConditionService medicalConditionService) {
        this.medicalConditionService = medicalConditionService;
    }

    @Override
    public String print(MedicalCondition condition, Locale locale) {
        return "medicalcondition_" + condition.getId().toString();
    }

    @Override
    public MedicalCondition parse(String id, Locale locale) throws ParseException {
        id = id.replace("medicalcondition_", "");
        try {
            MedicalCondition condition = medicalConditionService.getMedicalConditionById(Long.parseLong(id));
            return condition;
        } catch (NumberFormatException ex) {
            LOG.error("Can't convert string " + id + " to the id of a MedicalCondtion object!");
        }
        return null;
    }
}
