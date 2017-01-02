/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package si.bvukic.telehealth.web.controller;

import java.util.List;
import java.util.Set;
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
import si.bvukic.telehealth.core.model.MedicalCondition;
import si.bvukic.telehealth.core.model.Role;
import si.bvukic.telehealth.core.model.User;
import si.bvukic.telehealth.core.service.MedicalConditionService;
import si.bvukic.telehealth.core.service.UserService;
import si.bvukic.telehealth.web.model.ProfileSettings;

/**
 *
 * @author vukicb
 */
@Controller
@SessionAttributes("classActiveSettings,allMedicalConditions")
public class SettingsController extends GenericController {
    
    private static final Logger LOG = LoggerFactory.getLogger(SettingsController.class);
    private MedicalConditionService medicalCondtiConditionService;
    
    @Autowired
    public SettingsController(UserService userService, 
            MedicalConditionService medicalConditionService) {
        super(userService);
        this.medicalCondtiConditionService = medicalConditionService;
    }
    
    @ModelAttribute("classActiveSettings")
    public String iniCclassActiveSettings() {
        return "active";
    }
    
    @ModelAttribute("allMedicalConditions") 
    public List<MedicalCondition> initAllMedicalConditions () {
        return medicalCondtiConditionService.listMedicalConditions();
    }
    
    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/settings", method = RequestMethod.GET)
    public String loadSettings(Model model) {
        
        ProfileSettings profile = new ProfileSettings(super.getAuthenticatedUser());
        model.addAttribute("profile", profile);
        
        return "settings.html";
    }
    
    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/settings", method = RequestMethod.POST)
    public String loadSettings(ProfileSettings profile, Model model) {
        
        User user = super.getAuthenticatedUser();
        //TODO field validation
        
        user.setFirstName(profile.getFirstName());
        user.setLastName(profile.getLastName());
        user.setEmail(profile.getEmail());
        user.setPhone(profile.getPhone());
        user.setBirthDate(profile.getBirthDate());
        user.setGender(profile.getGender());
        user.setMedicalConditions(profile.getMedicalConditions());
        

        super.getUserService().updateUser(user);
        
        return "redirect:/settings";
    }
    
    
}
