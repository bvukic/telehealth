/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package si.bvukic.telehealth.web.model;

import si.bvukic.telehealth.core.model.MedicalParameter;

/**
 *
 * @author vukicb
 */
public class DashboardChartPanel {
    
    private MedicalParameter parameter;
    private ChartData data;

    public DashboardChartPanel(MedicalParameter parameter, ChartData data) {
        this.parameter = parameter;
        this.data = data;
    }

    public DashboardChartPanel() {
    }

    public MedicalParameter getParameter() {
        return parameter;
    }

    public void setParameter(MedicalParameter parameter) {
        this.parameter = parameter;
    }

    public ChartData getData() {
        return data;
    }

    public void setData(ChartData data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "DashboardCharPanel{" + "parameter=" + parameter + ", data=" + data + '}';
    }
    
    
    
}
