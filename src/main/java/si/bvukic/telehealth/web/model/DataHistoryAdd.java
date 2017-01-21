/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package si.bvukic.telehealth.web.model;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author vukicb
 */
public class DataHistoryAdd implements Serializable {
    
    private long parameterId;
    private Date date;
    private int hour;
    private int minute;

    public DataHistoryAdd() {
    }
    
    

    public DataHistoryAdd(long parameterId, Date date, int hour, int minute) {
        this.parameterId = parameterId;
        this.date = date;
        this.hour = hour;
        this.minute = minute;
        this.date.setHours(0);
        this.date.setMinutes(0);
        this.date.setSeconds(0);
        this.date.setMinutes(0);
    }
    
    /**
     * Setes otuher fields for current time.
     * @param parameterId 
     */
    public DataHistoryAdd(long parameterId) {
        this.date = new Date();
        this.minute = date.getMinutes();
        this.hour = date.getHours();
        this.date.setHours(0);
        this.date.setMinutes(0);
        this.date.setSeconds(0);
        this.date.setMinutes(0);
    }

    public long getParameterId() {
        return parameterId;
    }

    public void setParameterId(long parameterId) {
        this.parameterId = parameterId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }
    
    public Date getTimestamp(){
        Date timestamp = date;
        timestamp.setHours(hour);
        timestamp.setMinutes(minute);
        return timestamp;
    }

    @Override
    public String toString() {
        return "DataHistoryAdd{" + "parameterId=" + parameterId + ", date=" + date + ", hour=" + hour + ", minute=" + minute + '}';
    }
    
    
}
