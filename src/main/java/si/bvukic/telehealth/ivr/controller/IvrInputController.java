/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package si.bvukic.telehealth.ivr.controller;

import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import si.bvukic.telehealth.core.model.MedicalData;
import si.bvukic.telehealth.core.model.User;
import si.bvukic.telehealth.core.service.MedicalDataService;
import si.bvukic.telehealth.core.service.MedicalParameterService;
import si.bvukic.telehealth.core.service.UserService;

/**
 *
 * @author bostjanvukic
 */
@Controller
@PropertySource("classpath:application.properties")
public class IvrInputController {
    private static final Logger LOG = LoggerFactory.getLogger(IvrInputController.class);
    private final UserService userService;
    private final MedicalDataService medicalDataService;
    private final MedicalParameterService medicalParameterService;
    private final Environment environment;
    
    @Autowired
    public IvrInputController(UserService userService, 
            MedicalDataService medicalDataService,
            MedicalParameterService medicalParameterService,
            Environment environment) {
        this.userService = userService;
        this.medicalDataService = medicalDataService;
        this.medicalParameterService = medicalParameterService;
        this.environment = environment;
    }

    @PreAuthorize("hasRole('ROLE_PERMISSION_data_input')")
    @RequestMapping(value = "/api/vxml/input", method = RequestMethod.GET )
    public String initData(Model model, HttpServletRequest request) {
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication(); 
        User user = userService.getUserByUsername(auth.getName());
        LOG.info("User {} logged in sucessfully", user.getUsername());
        
        
        List<MedicalData> dataList = medicalDataService.missingMedicalDataList(user);
        
        if (dataList.isEmpty()) { 
            LOG.info("No data to input. Data for user {} is up to date.", user.getUsername());
            return "vxml/input-uptodate.xml";
        }
        
        LOG.debug("User {} needs to enter data for {} parameter/s", user.getUsername(), dataList.size());
        request.getSession().setAttribute("IvrInputController_dataList", dataList);
        
        //Index is used to navigate the dataList. Starting with 0
        return "vxml/input-init.xml";
    }
    
    @PreAuthorize("hasRole('ROLE_PERMISSION_data_input')")
    @RequestMapping(value = "/api/vxml/input/load/{index}", 
            method = RequestMethod.GET )
    public String loadData(@PathVariable("index") int index, 
            Model model, HttpServletRequest request) { 
        
        List<MedicalData> dataList = (List<MedicalData>) request.getSession().getAttribute("IvrInputController_dataList");
        if (index >= dataList.size()) { 
            return "vxml/input-complete.xml";
        }
        
        LOG.debug("Loading data {}, index {}", dataList.get(index).getMedicalParameter().getName(), index);
        model.addAttribute("index", index);
        model.addAttribute("parameter", dataList.get(index).getMedicalParameter());
        model.addAttribute("grxmlServer", environment.getRequiredProperty("grxml.server"));
        //model.addAttribute("grxmlServer", "http://www.bosti.org:80");
        
        return "vxml/input-form.xml";
        
    }

    @PreAuthorize("hasRole('ROLE_PERMISSION_data_input')")
    @RequestMapping(value = "/api/vxml/input/store/{index}", 
            method = RequestMethod.GET )
    public String storeData(@PathVariable("index") int index, 
            @RequestParam(value = "value", required = true) float  value,
            Model model, HttpServletRequest request) { 
        
        List<MedicalData> dataList = (List<MedicalData>) request.getSession().getAttribute("IvrInputController_dataList");
        if (index >= dataList.size()) { 
            return "redirect:/api/vxml/input-complete.xml";
        }
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication(); 
        User user = userService.getUserByUsername(auth.getName());
        
        MedicalData data = dataList.get(index);
        Date now = new Date();
        
        data.setDataValue(value);
        if (data.getId() == null) {
            data.setCreatedAt(now);
            data.setCreatedBy(user.getUsername());
            data.setDataValueDate(now);
            data.setUser(user);
            
            Long id = medicalDataService.saveMedicalData(data);
            data.setId(id);
            dataList.set(index, data);
            request.getSession().setAttribute("IvrInputController_dataList", dataList);
            LOG.info("Stored data! id: {}, parameter: {}, value {}, user: {}",
                    data.getId(), data.getMedicalParameter().getName(), data.getDataValue(), 
                    data.getUser().getUsername());
        } else {
            data.setUpdatedAt(now);
            data.setUpdatedBy(user.getUsername());
            medicalDataService.updateMedicalData(data);
            request.getSession().setAttribute("IvrInputController_dataList", dataList);
            LOG.info("Updated data! id: {}, parameter: {}, value {}, user: {}",
                    data.getId(), data.getMedicalParameter().getName(), data.getDataValue(), 
                    data.getUser().getUsername());            
        }
        
        index++;
        return "redirect:/api/vxml/input/load/" + index;   
    }
}
