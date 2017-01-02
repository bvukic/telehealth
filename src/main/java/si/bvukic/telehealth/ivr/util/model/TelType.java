/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package si.bvukic.telehealth.ivr.util.model;

/**
 * Enumeration for telephone number type. 
 * International type starts with 00 + CountyCode + LOCAL.
 * Nattional type starts with 0 + LOCAL
 * @author VukicB
 */
public enum TelType {
    
    NATIONAL,
    INTERNATIONAL
    
}
