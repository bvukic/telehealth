/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package si.bvukic.telehealth.core.model.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import si.bvukic.telehealth.core.model.Gender;
import static si.bvukic.telehealth.core.model.Gender.*;

/**
 *
 * @author bostjanvukic
 */
@Converter
public class GenderConverter implements AttributeConverter<Gender, String> {
    
    @Override
    public String convertToDatabaseColumn(Gender attribute) {
        switch (attribute) {
            case MALE:
                return "M";
            case FEMALE:
                return "F";
            default:
                throw new IllegalArgumentException("Unknown " + attribute);
        }
    }
 
    @Override
    public Gender convertToEntityAttribute(String dbData) {
        switch (dbData) {
            case "M":
                return MALE;
            case "F":
                return FEMALE;
            default:
                throw new IllegalArgumentException("Unknown " + dbData);
        }
    }
    
}
