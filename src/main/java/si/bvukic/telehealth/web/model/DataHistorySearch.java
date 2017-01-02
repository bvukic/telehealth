/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package si.bvukic.telehealth.web.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import si.bvukic.telehealth.core.model.MedicalParameter;

/**
 *
 * @author vukicb
 */
public class DataHistorySearch implements Serializable {
    
    private long parameterId;
    private String from;
    private String to;

    public DataHistorySearch() {
    }

    public DataHistorySearch(long parameterId, String from, String to) {
        this.parameterId = parameterId;
        this.from = from;
        this.to = to;
    }

    public long getParameterId() {
        return parameterId;
    }

    public void setParameterId(long parameterId) {
        this.parameterId = parameterId;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    
    
}
