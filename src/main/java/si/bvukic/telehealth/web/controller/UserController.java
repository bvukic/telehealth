/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package si.bvukic.telehealth.web.controller;

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
import si.bvukic.telehealth.core.model.User;
import si.bvukic.telehealth.core.service.RoleService;
import si.bvukic.telehealth.core.service.UserService;

/**
 *
 * @author bostjanvukic
 */
@Controller
public class UserController {
    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);
    
    private UserService userService;
    private RoleService roleService;
     
    @Autowired(required=true)
    @Qualifier(value="userService")
    public void setUserService(UserService userService){
        this.userService = userService;
    }
    
    @Autowired(required=true)
    @Qualifier(value="roleService")
    public void setRoleService(RoleService roleService){
        this.roleService = roleService;
    }
    
    @PreAuthorize("hasRole('ROLE_PERMISSION_administration_users')")
    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public String listUsers(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication(); 
        User user = new User();
        user.setEnabled(true);
        
        model.addAttribute("user", user);
        model.addAttribute("listUsers", this.userService.listUsers());
        model.addAttribute("allRoles", this.roleService.listRoles());
        model.addAttribute("me", userService.getUserByUsername(auth.getName()));
        model.addAttribute("classActiveUsers","active");
        return "users.html";
    }
     
    //For add and update user
    @PreAuthorize("hasRole('ROLE_PERMISSION_administration_users')")
    @RequestMapping(value= "/users/add", method = RequestMethod.POST)
    public String addUser(@ModelAttribute("user") User user){
         LOG.debug("Received add/update request for user: {}", user);
         
        if(user.getId() == null) {
            this.userService.addUser(user);
            LOG.info("Added user: {}", user.getUsername());
        } else {
            this.userService.updateUser(user);
            LOG.info("Updated user: {}", user.getUsername());
        }
         
        return "redirect:/users";
         
    }
    
    @PreAuthorize("hasRole('ROLE_PERMISSION_administration_users')") 
    @RequestMapping("/users/remove/{id}")
    public String removeUser(@PathVariable("id") Long id){
         
        this.userService.removeUser(id);
        return "redirect:/users";
    }
    
    @PreAuthorize("hasRole('ROLE_PERMISSION_administration_users')")
    @RequestMapping("/users/edit/{id}")
    public String editUser(@PathVariable("id") Long id, Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication(); 
        User user = userService.getUserById(id);
        
        model.addAttribute("user", user);
        model.addAttribute("listUsers", this.userService.listUsers());
        model.addAttribute("allRoles", this.roleService.listRoles());
        
        LOG.info("Loaded user: {}", user.getUsername());
        LOG.debug("User details: {}", user);
        
        model.addAttribute("me", userService.getUserByUsername(auth.getName()));
        model.addAttribute("classActiveUsers","active");
        
        return "users.html";
    }
   
}
