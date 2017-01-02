/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package si.bvukic.telehealth.core.model.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import si.bvukic.telehealth.core.model.MedicalDataPeriod;
import static si.bvukic.telehealth.core.model.MedicalDataPeriod.*;

/**
 *
 * @author bostjanvukic
 */
@Converter
public class MedicalDataPeriodConverter implements AttributeConverter<MedicalDataPeriod, String> {
    
    @Override
    public String convertToDatabaseColumn(MedicalDataPeriod attribute) {
        switch (attribute) {
            case SECOND:
                return "SE";
            case MINUTE:
                return "MI";
            case HOUR:
                return "HO";
            case DAY:
                return "DA";
            case WEEK:
                return "WE";
            case MONTH:
                return "MO";
            case YEAR:
                return "YE";
            default:
                throw new IllegalArgumentException("Unknown " + attribute);
        }
    }
 
    @Override
    public MedicalDataPeriod convertToEntityAttribute(String dbData) {
        switch (dbData) {
            case "SE":
                return SECOND;
            case "MI":
                return MINUTE;
            case "HO":
                return HOUR;
            case "DA":
                return DAY;
            case "WE":
                return WEEK;
            case "MO":
                return MONTH;
            case "YE":
                return YEAR;
            default:
                throw new IllegalArgumentException("Unknown " + dbData);
        }
    }
    
}
