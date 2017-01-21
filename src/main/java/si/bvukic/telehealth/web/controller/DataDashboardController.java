/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package si.bvukic.telehealth.web.controller;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import si.bvukic.telehealth.core.model.MedicalData;
import si.bvukic.telehealth.core.model.MedicalParameter;
import si.bvukic.telehealth.core.model.User;
import si.bvukic.telehealth.core.service.MedicalDataService;
import si.bvukic.telehealth.core.service.UserService;
import si.bvukic.telehealth.web.model.ChartData;
import si.bvukic.telehealth.web.model.DashboardChartPanel;

/**
 *
 * @author bostjanvukic
 */
@Controller
@SessionAttributes("classActiveDashboard")
public class DataDashboardController extends GenericChartController {
     
    private static final Logger LOG = LoggerFactory.getLogger(DataDashboardController.class);
    private static final String TIMEZONE = "Europe/Ljubljana";
    private final MedicalDataService medicalDataService;
    

    @Autowired
    public DataDashboardController(MedicalDataService medicalDataService, UserService userService) {
        super(userService);
        this.medicalDataService = medicalDataService;
    }
    
    
    @ModelAttribute("classActiveDashboard")
    public String iniCclassActiveDashboard() {
        return "active";
    }
    
    
    @PreAuthorize("hasRole('ROLE_PERMISSION_data_dashboard')")
    @RequestMapping(value = "/dashboard", method = RequestMethod.GET)
    public String loadDashboard(Locale locale, Model model) {
        User user = super.getAuthenticatedUser();
        
        LocalDate today = LocalDate.now(ZoneId.of(TIMEZONE));
        LocalDateTime todayMidnight = LocalDateTime.of(today, LocalTime.MIDNIGHT);
        //Get data for last 7 days
        Instant start = todayMidnight.minusDays(7).toInstant(ZoneOffset.ofHours(1));
        
        LOG.debug("Getting dashboard data from data: {}", Date.from(start));
        List<DashboardChartPanel> dashbordChartPanels = new ArrayList();
        for (MedicalParameter parameter : user.getMedicalParameters()) {
            List<MedicalData> dataList = medicalDataService.listMedicalData(user, parameter, Date.from(start), new Date());
            ChartData data = super.genChartData(dataList);
            DashboardChartPanel dcp = new DashboardChartPanel(parameter, data);
            LOG.debug("Adding dashboard chart pannel: {}", dcp);
            dashbordChartPanels.add(dcp);
        }
        LOG.debug("Total chart panels: {}", dashbordChartPanels.size());
        model.addAttribute("dashbordChartPanels", dashbordChartPanels);
        
        return "dashboard.html";
    }
     

}
