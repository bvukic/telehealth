/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package si.bvukic.telehealth.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import si.bvukic.telehealth.core.model.User;
import si.bvukic.telehealth.core.service.UserService;

/**
 *
 * @author vukicb
 */
@Controller
@SessionAttributes("me")
public class GenericController {
     
     private UserService userService;

     @Autowired
    public GenericController(UserService userService) {
        this.userService = userService;
    }

    protected UserService getUserService() {
        return userService;
    }
    
    protected User getAuthenticatedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication(); 
        return userService.getUserByUsername(auth.getName());
    }
     
    @ModelAttribute("me")
    public User initMe() {
        return getAuthenticatedUser();
    }
    
}


