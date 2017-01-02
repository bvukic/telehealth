/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package si.bvukic.telehealth.web.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import si.bvukic.telehealth.core.model.MedicalData;
import si.bvukic.telehealth.core.model.MedicalParameter;
import si.bvukic.telehealth.core.model.User;
import si.bvukic.telehealth.core.service.MedicalDataService;
import si.bvukic.telehealth.core.service.UserService;

/**
 *
 * @author bostjanvukic
 */
@Controller
public class MedicalDataController {
    
    private static final Logger LOG = LoggerFactory.getLogger(MedicalDataController.class);
    
    private MedicalDataService medicalDataService;
    private UserService userService;
    
    @Autowired(required=true)
    @Qualifier(value="medicalDataService")
    public void setMedicalDataService(MedicalDataService medicalDataService) {
        this.medicalDataService = medicalDataService;
    }
    
    @Autowired(required=true)
    @Qualifier(value="userService")
    public void setUserService(UserService userService){
        this.userService = userService;
    }
    
    @PreAuthorize("hasRole('ROLE_PERMISSION_administration_users')")
    @RequestMapping(value = "/data/input", method = RequestMethod.GET)
    public String datInputInit(HttpServletRequest request, HttpServletResponse response, Model model)
            throws Exception {
        model.addAttribute("classActiveDataInput","active");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication(); 
        User user = userService.getUserByUsername(auth.getName());
        model.addAttribute("me", user);
        
        //Initialize medicalDataList for this user and store it into the session
        LOG.debug("Initializing data input for user: {}", user.getUsername());
        List<MedicalData> medicalDataList = medicalDataService.missingMedicalDataList(user);
        //If medicalDataList is empty than display "All parameters are up to date"
        if (medicalDataList.isEmpty()) { 
            LOG.info("No data to input. Data for user {} is up to date.", user.getUsername());
            model.addAttribute("upToDate", true);
            return "medicalDataInput.html";
        }
        LOG.debug("User {} needs to enter data for {} parameters", user.getUsername(), medicalDataList.size());
        request.getSession().setAttribute("MedicalDataController_medicalDataList", medicalDataList);
        
        //Index is used to navigate the medicalDataList
        int index = 0;
        model.addAttribute("index", index);
        //Init first MedicalData object for model/view
        MedicalData data = medicalDataList.get(index);
        model.addAttribute("data", data);
        
        return "medicalDataInput.html";
    }
    
    @PreAuthorize("hasRole('ROLE_PERMISSION_administration_users')")
    @RequestMapping(value = "/data/input/{index}/{command}", method = RequestMethod.POST)
    public String dataInputSave(@PathVariable("index") int index, @ModelAttribute("data") MedicalData data,
            @PathVariable("command") String command, HttpServletRequest request, HttpServletResponse response, 
            Model model) throws Exception {
        
        model.addAttribute("classActiveDataInput","active");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication(); 
        User user = userService.getUserByUsername(auth.getName());
        model.addAttribute("me", user);
        

        List<MedicalData> medicalDataList = (List<MedicalData>) request.getSession().
                getAttribute("MedicalDataController_medicalDataList");
        //Reinitialize data input if the list is empty
        if (medicalDataList.isEmpty()) return "redirect:/data/input";
        
        float dataValue = data.getDataValue();
        data = medicalDataList.get(index);
        data.setDataValue(dataValue);
        
        
        LOG.info("Processing data input from user={}, index={}, command={} for user={}, value={}", 
                user.getUsername(), index, command, data.getUser().getUsername(), data.getDataValue());
        
        //Validate input dataValue
        if (!this.isDataValid(data)) {
            model.addAttribute("error", "Napaka: Vnešeni podatki niso veljavni!");
            model.addAttribute("data", data);
            model.addAttribute("index", index);
        
            return "medicalDataInput.html";
        }
        Date now = new Date();
        data.setDataValueDate(now);
        data.setUpdatedAt(now);
        data.setUpdatedBy(user.getUsername());
        
        if (data.getId() == null ) {
            Long id = this.medicalDataService.saveMedicalData(data);
            data.setId(id);
            LOG.debug("Saved new medical data: {}", data);
        } else {
            this.medicalDataService.updateMedicalData(data);
            LOG.debug("Updated medical data: {}", data);
        }
        
        medicalDataList.set(index, data);
        request.getSession().setAttribute("MedicalDataController_medicalDataList", medicalDataList);
        
        //If command is next and index is on the last item then data entry is complete
        if ("next".equals(command) && index == medicalDataList.size() -1) {
            LOG.debug("User {} completed data input", user.getUsername());
            model.addAttribute("complete", true);
        }
        if ("next".equals(command) && index < medicalDataList.size() -1) index++;
        if ("previous".equals(command) && index > 0 && index < medicalDataList.size()) index--;

        model.addAttribute("index", index);
        model.addAttribute("data", medicalDataList.get(index));
        
        return "medicalDataInput.html";
    }
    
    //Load data for last parameter
    @PreAuthorize("hasRole('ROLE_PERMISSION_administration_users')")
    @RequestMapping(value = "/data/input/last", method = RequestMethod.POST)
    public String dataInputLoadLast(HttpServletRequest request, HttpServletResponse response, 
            Model model) throws Exception {
        
        model.addAttribute("classActiveDataInput","active");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication(); 
        User user = userService.getUserByUsername(auth.getName());
        model.addAttribute("me", user);
        

        List<MedicalData> medicalDataList = (List<MedicalData>) request.getSession().
                getAttribute("MedicalDataController_medicalDataList");
        //Reinitialize data input if the list is empty
        if (medicalDataList.isEmpty()) return "redirect:/data/input";
        
        int index = medicalDataList.size() -1;
        model.addAttribute("index", index);
        model.addAttribute("data", medicalDataList.get(index));
        
        return "medicalDataInput.html";
    }
    
    private boolean isDataValid(MedicalData data) {
        float min = data.getMedicalParameter().getDataValueMin();
        float max = data.getMedicalParameter().getDataValueMax();
        
        return (data.getDataValue() >= min && data.getDataValue() <= max);
    }
    
    
    
    
    
}
