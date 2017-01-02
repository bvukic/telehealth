/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package si.bvukic.telehealth.web.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import si.bvukic.telehealth.core.model.MedicalData;
import si.bvukic.telehealth.core.service.UserService;
import si.bvukic.telehealth.web.model.ChartData;

/**
 *
 * @author vukicb
 */
@Controller
public class GenericChartController extends GenericController {
    
    @Autowired
    public GenericChartController(UserService userService) {
        super(userService);
    }
    
    
    public ChartData genChartData(List<MedicalData> dataList) {
        
        if (dataList.isEmpty()) {
            return new ChartData();
        }
        
        SimpleDateFormat df = new SimpleDateFormat("dd.MM.");
         List<String> labels = new ArrayList<String>();
         List<Float> values = new ArrayList<Float>();
         
         for (MedicalData data : dataList) {
             labels.add(df.format(data.getDataValueDate()));
             values.add(data.getDataValue());
        }
        
        List<List> series = new ArrayList();
        series.add(new ArrayList(values));
        
        
        return new ChartData(labels, series);
        
    }
    
}
