/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package si.bvukic.telehealth.web.controller;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author vukicb
 */
@Controller
public class IndexController {
  
  @RequestMapping("/")
  public String indexRedirect() {
      
    if (SecurityContextHolder.getContext().getAuthentication() != null 
            && SecurityContextHolder.getContext().getAuthentication().isAuthenticated() 
            && !(SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken)) {
        return "redirect:/dashboard";
    } else {
        return "redirect:/login";
    }
    
  }
  
  
}
