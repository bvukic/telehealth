/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package si.bvukic.telehealth.core.model.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import si.bvukic.telehealth.core.model.MedicalDataType;
import static si.bvukic.telehealth.core.model.MedicalDataType.*;

/**
 *
 * @author bostjanvukic
 */
@Converter
public class MedicalDataTypeConverter implements AttributeConverter<MedicalDataType, String> {
    
    @Override
    public String convertToDatabaseColumn(MedicalDataType attribute) {
        switch (attribute) {
            case BOOLEAN:
                return "BO";
            case NUMERIC:
                return "NU";
            case SCALE_CONDITION:
                return "SC";
            case SCALE_ARGUMENT:
                return "SA";
            case SCALE_QUANTITY:
                return "SQ";
            default:
                throw new IllegalArgumentException("Unknown " + attribute);
        }
    }
 
    @Override
    public MedicalDataType convertToEntityAttribute(String dbData) {
        switch (dbData) {
            case "BO":
                return BOOLEAN;
            case "NU":
                return NUMERIC;
            case "SC":
                return SCALE_CONDITION;
            case "SA":
                return SCALE_ARGUMENT;
            case "SQ":
                return SCALE_QUANTITY;
            default:
                throw new IllegalArgumentException("Unknown " + dbData);
        }
    }
    
}
