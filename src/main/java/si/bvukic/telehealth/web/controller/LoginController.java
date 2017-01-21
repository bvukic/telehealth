/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package si.bvukic.telehealth.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author bostjanvukic
 */
@Controller
public class LoginController {
 
  // Login form
  @RequestMapping("/login")
  public String login() {
    return "login.html";
  }
 
  // Login form with error
  @RequestMapping("/login/error")
  public String loginError(Model model) {
    model.addAttribute("loginError", true);
    return "login.html";
  }
  
  // Login help
  @RequestMapping("/login/help")
  public String loginHelp() {
    return "loginHelp.html";
  }

    
}
