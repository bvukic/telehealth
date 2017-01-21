/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package si.bvukic.telehealth.web.controller;

import java.util.List;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
import si.bvukic.telehealth.core.service.RoleService;
import si.bvukic.telehealth.core.service.UserService;

/**
 *
 * @author vukicb
 */
@Controller
@PropertySource("classpath:application.properties")
@SessionAttributes("classActiveSettings,allMedicalConditions,allRoles")
public class SettingsController extends GenericController {
    
    private static final Logger LOG = LoggerFactory.getLogger(SettingsController.class);
    private MedicalConditionService medicalCondtiConditionService;
    private final RoleService roleService;
    private final Environment environment;
    private final BCryptPasswordEncoder passwordEncoder;
    
    @Autowired
    public SettingsController(UserService userService, 
            MedicalConditionService medicalConditionService,
            RoleService roleService, Environment environment,
            BCryptPasswordEncoder passwordEncoder) {
        super(userService);
        this.medicalCondtiConditionService = medicalConditionService;
        this.roleService = roleService;
        this.environment = environment;
        this.passwordEncoder = passwordEncoder;
    }
    
    @ModelAttribute("classActiveSettings")
    public String iniCclassActiveSettings() {
        return "active";
    }
    
    @ModelAttribute("allMedicalConditions") 
    public List<MedicalCondition> initAllMedicalConditions () {
        return medicalCondtiConditionService.listMedicalConditions();
    }
    
    @ModelAttribute("allRoles") 
    public List<Role> initAllRoles () {
        return roleService.listRoles();
    }
    
    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/settings", method = RequestMethod.GET)
    public String loadSettings(Model model) {
        
        User user = super.getAuthenticatedUser();
        user.setPassword(null);
        model.addAttribute("user", user);
        
        return "settings.html";
    }
    
    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/settings", method = RequestMethod.POST)
    public String loadSettings(@ModelAttribute User user, Model model) {
        
        User authUser = super.getAuthenticatedUser();
        user.setEnabled(authUser.isEnabled());
        user.setUsername(authUser.getUsername());
        
        if (user.getRoles() == null) {
            user.setRoles(authUser.getRoles());
        } else if (user.getRoles().isEmpty()) {
            user.setRoles(authUser.getRoles());
        }
        
        boolean isPasswordEncrypted = false;
        if (user.getPassword() == null) {
            user.setPassword(authUser.getPassword());
            isPasswordEncrypted = true;
        } else if (user.getPassword().isEmpty()) {
            user.setPassword(authUser.getPassword());
            isPasswordEncrypted = true;
        }
        
        //Validation expressions
        Pattern usernamePattern = Pattern.compile(environment.getRequiredProperty("validation.username"));
        Pattern emailPattern = Pattern.compile(environment.getRequiredProperty("validation.email"));
        Pattern phonePattern = Pattern.compile(environment.getRequiredProperty("validation.phone"));
        
        boolean isError = false;
        if(super.getUserService().isPhoneTaken(user.getPhone()) && 
                !user.equals(super.getUserService().getUserByPhone(user.getPhone()))) {
            
            LOG.debug("Requested registration for phone number {} is invalid. Phone number is taken.", user.getPhone());
            model.addAttribute("errorPhoneTaken", true);
            isError = true;
        }
        if(!user.getPhone().matches(phonePattern.pattern())) {
            LOG.debug("Requested setting for phone number {} is invalid. Phone number format is invalid.", user.getPhone());
            model.addAttribute("errorPhoneFormat", true);
            isError = true;
        }
        if(!user.getEmail().matches(emailPattern.pattern())){
            LOG.debug("Requested setting for email address {} is invalid. E-Mail address format is invalid.", user.getEmail());
            model.addAttribute("errorEmailFormat", true);
            isError = true;
        }
        if(user.getMedicalConditions().isEmpty()){
            LOG.debug("Requested setting for {} is invalid. No medical conditions selected.", user.getUsername());
            model.addAttribute("errorMedicalConditionsEmpty", true);
            isError = true;
        }
        if(user.getPassword().length() < 6) {
            LOG.debug("Requested registration for {} is invalid. Password is too short!", user.getUsername());
            model.addAttribute("errorPasswordTooShort", true);
            isError = true;
        }
        
        if(!isError) {
            if (!isPasswordEncrypted) {
                user.setPassword(passwordEncoder.encode(user.getPassword()));
                isPasswordEncrypted = true;
            }
            super.getUserService().updateUser(user);
            
            LOG.info("Stored new settings for user  {}", user.getUsername());
            LOG.debug("Updated {}", user);
            return "redirect:/settings";
        }
        model.addAttribute("isError", isError);
        return "settings.html";
    }
    
    
}
