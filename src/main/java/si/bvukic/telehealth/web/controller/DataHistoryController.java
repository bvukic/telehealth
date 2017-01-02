/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package si.bvukic.telehealth.web.controller;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import si.bvukic.telehealth.core.model.MedicalData;
import si.bvukic.telehealth.core.model.MedicalParameter;
import si.bvukic.telehealth.core.model.User;
import si.bvukic.telehealth.core.service.MedicalDataService;
import si.bvukic.telehealth.core.service.MedicalParameterService;
import si.bvukic.telehealth.core.service.UserService;
import si.bvukic.telehealth.web.formatter.DateFormatter;
import si.bvukic.telehealth.web.model.DataHistorySearch;


/**
 *
 * @author bostjanvukic
 */
@Controller
@SessionAttributes("classActiveDataHistory")
public class DataHistoryController extends GenericChartController {
    
    private static final Logger LOG = LoggerFactory.getLogger(DataHistoryController.class);
    private final MedicalDataService medicalDataService;    
    private final MedicalParameterService medicalParameterService;

    @Autowired
    public DataHistoryController(MedicalDataService medicalDataService, 
            MedicalParameterService medicalParameterService, UserService userService) {
        super(userService);
        this.medicalDataService = medicalDataService;
        this.medicalParameterService = medicalParameterService;
    }


    
    @PreAuthorize("hasRole('ROLE_PERMISSION_administration_users')")
    @RequestMapping(value = "/data/history", method = RequestMethod.GET)
    public String initDataHistorySearch (Model model, Locale locale, HttpServletRequest request) {
        
        User user = super.getAuthenticatedUser();
        DataHistorySearch search = new DataHistorySearch();
        if(user.getMedicalParameters().size() > 0) {
            search.setParameterId(user.getMedicalParameters().get(0).getId());
        }
        DateFormatter dateFormatter = new DateFormatter();
        String now = dateFormatter.print(new Date(), locale);
        search.setFrom(now);
        search.setTo(now);
        
        request.getSession().setAttribute("DataHistoryController_search", search);
        model.addAttribute("search", search);
        
        return "redirect:/data/history/result";
    }
    
    
    @PreAuthorize("hasRole('ROLE_PERMISSION_administration_users')")
    @RequestMapping(value = "/data/history", method = RequestMethod.POST)
    public String updateDataHistorySearch (
            @ModelAttribute("search") DataHistorySearch search,
            HttpServletRequest request,
            Model model, Locale locale) {
        
        LOG.debug("Updated data history search. Details: {}", search);
        request.getSession().setAttribute("DataHistoryController_search", search);
        model.addAttribute("search", search);
        
        return "redirect:/data/history/result";
    }
    
    
    @PreAuthorize("hasRole('ROLE_PERMISSION_administration_users')")
    @RequestMapping(value = "/data/history/result", method = RequestMethod.GET)
    public String loadDataHistory (
            HttpServletRequest request,
            Model model, Locale locale) {
    
        User user = super.getAuthenticatedUser();
        
        DataHistorySearch search = (DataHistorySearch) request.getSession()
                .getAttribute("DataHistoryController_search");
        
        model.addAttribute("search", search);
        
        DateFormatter dateFormatter = new DateFormatter();
        Date fromDate;
        try {
            fromDate = dateFormatter.parse(search.getFrom(), locale);
        } catch (ParseException ex) {
            LOG.warn("FromDate format error. Exception: {}", ex);
            model.addAttribute("errorMsg", "Napačna oblika datuma. Datum mora ustrezati vzorcu dd.mm.llll. Kliknite na polje in s koledarjem izbreite željen datum.");
            model.addAttribute("errorClass", "background-danger");
            return "dataHistory.html";
        }
        Date toDate;
        try {
            toDate = dateFormatter.parse(search.getTo(), locale);
            toDate.setHours(23);
            toDate.setMinutes(59);
            toDate.setSeconds(59);
            if (toDate.before(fromDate)) {
                model.addAttribute("errorMsg", "Datum Do ne sme biti manjši od datuma Od.");
                model.addAttribute("errorClass", "background-danger");
                return "dataHistory.html";
            }
        } catch (ParseException ex) {
            LOG.warn("FromDate format error. Exception: {}", ex);
            model.addAttribute("errorMsg", "Napačna oblika datuma. Datum mora ustrezati vzorcu dd.mm.llll. Kliknite na polje in s koledarjem izbreite željen datum.");
            model.addAttribute("errorClass", "background-danger");
            return "dataHistory.html";
        }
        
        MedicalParameter parameter = medicalParameterService.getMedicalParameterById(search.getParameterId());
        LOG.info("Data history search for user: {}, parameter.name: {}, startDate: {}, stopDate: {}",
                user.getUsername(), parameter.getName(), fromDate, toDate);
        List<MedicalData> dataList = this.medicalDataService.listMedicalData(user, parameter, fromDate, toDate);
        LOG.info("Data history liset size is: {} for user: {}, parameter.name: {}, startDate: {}, stopDate: {}",
                dataList.size(), user.getUsername(), parameter.getName(), fromDate, toDate);
        model.addAttribute("dataList", dataList);
        model.addAttribute("chartData", genChartData(dataList));
        
        return "dataHistory.html";
    }
    
    
    
    @PreAuthorize("hasRole('ROLE_PERMISSION_administration_users')") 
    @RequestMapping("/data/history/remove/{id}")
    public String removeData(@PathVariable("id") Long id){
         
        medicalDataService.removeMedicalData(id);
        return "redirect:/data/history/result";
    }
    
    @PreAuthorize("hasRole('ROLE_PERMISSION_administration_users')") 
    @RequestMapping(value = "/data/history/edit/{id}", method = RequestMethod.GET)
    public String loadEditData(@PathVariable("id") Long id, Model model){
         
        MedicalData data = medicalDataService.getMedicalDataById(id);
        model.addAttribute("data", data);
        return "dataHistoryEdit.html";
    }
    
    @PreAuthorize("hasRole('ROLE_PERMISSION_administration_users')") 
    @RequestMapping(value = "/data/history/edit", method = RequestMethod.POST)
    public String editData(@ModelAttribute MedicalData data, Model model) {
        
        MedicalData storedData = medicalDataService.getMedicalDataById(data.getId());
        storedData.setDataValue(data.getDataValue());
        storedData.setUpdatedAt(new Date());
        storedData.setUpdatedBy(super.getAuthenticatedUser().getUsername());
        medicalDataService.updateMedicalData(storedData);
        //TODO Validaton and error handling
        return "redirect:/data/history/result";
    }
        
    
    @ModelAttribute("classActiveDataHistory")
    public String initClassActiveDataHistory() {
        return "active";
    }
    
    

}
