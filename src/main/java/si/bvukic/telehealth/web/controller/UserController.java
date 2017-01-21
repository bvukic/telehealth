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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
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
 * @author bostjanvukic
 */
@Controller
@PropertySource("classpath:application.properties")
@SessionAttributes("classActiveUsers,allMedicalConditions,allRoles,listUsers")
public class UserController extends GenericController {

    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);
    private MedicalConditionService medicalCondtiConditionService;
    private final RoleService roleService;
    private final Environment environment;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserController(UserService userService,
            MedicalConditionService medicalConditionService,
            RoleService roleService, Environment environment,
            BCryptPasswordEncoder passwordEncoder) {
        super(userService);
        this.medicalCondtiConditionService = medicalConditionService;
        this.roleService = roleService;
        this.environment = environment;
        this.passwordEncoder = passwordEncoder;
    }

    @ModelAttribute("classActiveUsers")
    public String initCclassActiveUsers() {
        return "active";
    }

    @ModelAttribute("allMedicalConditions")
    public List<MedicalCondition> initAllMedicalConditions() {
        return medicalCondtiConditionService.listMedicalConditions();
    }

    @ModelAttribute("allRoles")
    public List<Role> initAllRoles() {
        return roleService.listRoles();
    }
    
    @ModelAttribute("listUsers")
    public List<User> initListUsers(){
        return super.getUserService().listUsers();
    }

    @PreAuthorize("hasRole('ROLE_PERMISSION_administration_users')")
    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public String listUsers(Model model) {

        User user = new User();
        user.setEnabled(true);
        model.addAttribute("user", user);
        return "users.html";
    }

    //For add and update user
    @PreAuthorize("hasRole('ROLE_PERMISSION_administration_users')")
    @RequestMapping(value = "/users/add", method = RequestMethod.POST)
    public String addUser(@ModelAttribute("user") User user, Model model) {
        LOG.debug("Received add/update request for user: {}", user);
   
        //Validation expressions
        Pattern usernamePattern = Pattern.compile(environment.getRequiredProperty("validation.username"));
        Pattern emailPattern = Pattern.compile(environment.getRequiredProperty("validation.email"));
        Pattern phonePattern = Pattern.compile(environment.getRequiredProperty("validation.phone"));
        
        
        boolean isError = false;
        boolean isPasswordEncrypted = false;
        if (user.getId() == null) {
            if(!user.getUsername().matches(usernamePattern.pattern())){
                LOG.debug("Requested registation for username {} is invalid. Username format is invalid.", user.getUsername());
                model.addAttribute("errorUsernameFormat", true);
                isError = true;
            }
            if(super.getUserService().isUsernameTaken(user.getUsername())){
                LOG.debug("Requested registation for username {} is invalid. Username is taken.", user.getUsername());
                model.addAttribute("errorUsernameTaken", true);
                isError = true;
            }
        } else {
            user.setUsername(super.getUserService().getUserById(user.getId()).getUsername());
            //If user did not chacge the password we need to set the old password before we store this user instance
            if (user.getPassword() == null) {
                user.setPassword(super.getUserService().getUserById(user.getId()).getPassword());
                isPasswordEncrypted = true;
            } else if (user.getPassword().isEmpty()) {
                user.setPassword(super.getUserService().getUserById(user.getId()).getPassword());
                isPasswordEncrypted = true;
            }
        }
        
        
        
        if (super.getUserService().isPhoneTaken(user.getPhone()) && 
                !user.equals(super.getUserService().getUserByPhone(user.getPhone()))) {
            LOG.debug("Requested registration for phone number {} is invalid. Phone number is taken.", user.getPhone());
            model.addAttribute("errorPhoneTaken", true);
            isError = true;
        }
        if (!user.getPhone().matches(phonePattern.pattern())) {
            LOG.debug("Requested registration for phone number {} is invalid. Phone number format is invalid.", user.getPhone());
            model.addAttribute("errorPhoneFormat", true);
            isError = true;
        }
        if (!user.getEmail().matches(emailPattern.pattern())){
            LOG.debug("Requested registration for email address {} is invalid. E-Mail address format is invalid.", user.getEmail());
            model.addAttribute("errorEmailFormat", true);
            isError = true;
        }
        if (user.getMedicalConditions().isEmpty()){
            LOG.debug("Requested registration for {} is invalid. No medical conditions selected.", user.getUsername());
            model.addAttribute("errorMedicalConditionsEmpty", true);
            isError = true;
        }
        
        
        
        if (!isError) {
            if (!isPasswordEncrypted) {
                user.setPassword(passwordEncoder.encode(user.getPassword()));
                isPasswordEncrypted = true;
            }
            if (user.getId() == null) {
                super.getUserService().addUser(user);
                LOG.info("Added user: {}", user.getUsername());
            } else {
                super.getUserService().updateUser(user);
                LOG.info("Updated user: {}", user.getUsername());
            }
            return "redirect:/users";
        }
        model.addAttribute("isError", isError);
        return "users.html";

    }

    @PreAuthorize("hasRole('ROLE_PERMISSION_administration_users')")
    @RequestMapping("/users/remove/{id}")
    public String removeUser(@PathVariable("id") Long id) {

        super.getUserService().removeUser(id);
        return "redirect:/users";
    }

    @PreAuthorize("hasRole('ROLE_PERMISSION_administration_users')")
    @RequestMapping("/users/edit/{id}")
    public String editUser(@PathVariable("id") Long id, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = super.getUserService().getUserById(id);
        user.setPassword(null);

        model.addAttribute("user", user);

        return "users.html";
    }

}
