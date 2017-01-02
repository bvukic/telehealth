/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package si.bvukic.telehealth.core.model;

/**
 *
 * @author bostjanvukic
 */
public enum MedicalDataType {
    
    BOOLEAN("boolean.grxml"),
    NUMERIC("numeric.grxml"),
    SCALE_CONDITION("condition.grxml"),
    SCALE_ARGUMENT("argument.grxml"),
    SCALE_QUANTITY("quantity.grxml");
    
    private final String grammar;
    
    MedicalDataType(String grammar) {
        this.grammar = grammar;
    }
    
    public String getGrammar() {
        return grammar;
    }
    

}
