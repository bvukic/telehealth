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
public enum Gender {
    
    MALE("moški"),
    FEMALE("ženski");
    
    private final String displayName;
    
    Gender(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
    
}
