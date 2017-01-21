/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package si.bvukic.telehealth.web.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
import si.bvukic.telehealth.core.service.EmailNotificationService;
import si.bvukic.telehealth.core.service.MedicalConditionService;
import si.bvukic.telehealth.core.service.RoleService;
import si.bvukic.telehealth.core.service.UserService;

/**
 *
 * @author vukicb
 */
@Controller
@PropertySource("classpath:application.properties")
@SessionAttributes("user,allMedicalConditions")
public class RegisterController {
    
    private static final Logger LOG = LoggerFactory.getLogger(RegisterController.class);
    private final RoleService roleService;
    private final UserService userService;
    private final MedicalConditionService medicalCondtiConditionService;
    private final EmailNotificationService emailNotifiactionService;
    private final Environment environment;
    private final BCryptPasswordEncoder passwordEncoder;
    
    @Autowired
    public RegisterController(UserService userService, 
            MedicalConditionService medicalConditionService,
            RoleService roleService, 
            EmailNotificationService emailNotifiacationService,
            Environment environment, 
            BCryptPasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.medicalCondtiConditionService = medicalConditionService;
        this.roleService = roleService;
        this.emailNotifiactionService = emailNotifiacationService;
        this.environment = environment;
        this.passwordEncoder = passwordEncoder;
    }
    
    
    @ModelAttribute("allMedicalConditions") 
    public List<MedicalCondition> initAllMedicalConditions () {
        return medicalCondtiConditionService.listMedicalConditions();
    }
    
    @PreAuthorize("!isAuthenticated()")
    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String loadRegistration(Model model) {
        
        model.addAttribute("user", new User());
        
        return "register.html";
    }
    
    @PreAuthorize("!isAuthenticated()")
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String addUser(@ModelAttribute User user, Model model) {
        
        //New user is enabled by default
        user.setEnabled(true);
        
        //New user gets a default role
        Role role = roleService.getRoleById(environment.getRequiredProperty("register.role"));
        Set<Role> roles = new HashSet();
        roles.add(role);
        user.setRoles(roles);
        
        //Validation expressions
        Pattern usernamePattern = Pattern.compile(environment.getRequiredProperty("validation.username"));
        Pattern emailPattern = Pattern.compile(environment.getRequiredProperty("validation.email"));
        Pattern phonePattern = Pattern.compile(environment.getRequiredProperty("validation.phone"));
        
        
        boolean isError = false;
        if(!user.getUsername().matches(usernamePattern.pattern())){
            LOG.debug("Requested registation for username {} is invalid. Username format is invalid.", user.getUsername());
            model.addAttribute("errorUsernameFormat", true);
            isError = true;
        }
        if(userService.isUsernameTaken(user.getUsername())){
            LOG.debug("Requested registation for username {} is invalid. Username is taken.", user.getUsername());
            model.addAttribute("errorUsernameTaken", true);
            isError = true;
        }
        if(userService.isPhoneTaken(user.getPhone())) {
            LOG.debug("Requested registration for phone number {} is invalid. Phone number is taken.", user.getPhone());
            model.addAttribute("errorPhoneTaken", true);
            isError = true;
        }
        if(!user.getPhone().matches(phonePattern.pattern())) {
            LOG.debug("Requested registration for phone number {} is invalid. Phone number format is invalid.", user.getPhone());
            model.addAttribute("errorPhoneFormat", true);
            isError = true;
        }
        if(!user.getEmail().matches(emailPattern.pattern())){
            LOG.debug("Requested registration for email address {} is invalid. E-Mail address format is invalid.", user.getEmail());
            model.addAttribute("errorEmailFormat", true);
            isError = true;
        }
        if(user.getMedicalConditions().isEmpty()){
            LOG.debug("Requested registration for {} is invalid. No medical conditions selected.", user.getUsername());
            model.addAttribute("errorMedicalConditionsEmpty", true);
            isError = true;
        }
        if(user.getPassword().length() < 6) {
            LOG.debug("Requested registration for {} is invalid. Password is too short!", user.getUsername());
            model.addAttribute("errorPasswordTooShort", true);
            isError = true;
        }
        
        if(!isError) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userService.addUser(user);
            emailNotifiactionService.sendRegistrationNotification(user);
            LOG.info("New user {} has sucessfully registered.", user.getUsername());
            LOG.debug("New {}", user);
            model.addAttribute("user",user);
            return "registerSucess.html";
        }
        model.addAttribute("isError", isError);
        return "register.html";
    }
    
    
}
