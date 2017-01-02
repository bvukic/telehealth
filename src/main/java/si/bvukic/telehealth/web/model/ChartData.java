/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package si.bvukic.telehealth.web.model;

import java.util.List;

/**
 *
 * @author vukicb
 */
public class ChartData {
    
    private List<String> labels;
    private List<List> series;

    public ChartData() {
    }

    public ChartData(List<String> labels, List<List> series) {
        this.labels = labels;
        this.series = series;
    }

    public List<String> getLabels() {
        return labels;
    }

    public void setLabels(List<String> labels) {
        this.labels = labels;
    }

    public List<List> getSeries() {
        return series;
    }

    public void setSeries(List<List> series) {
        this.series = series;
    }
    
    
    
}
