/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package si.bvukic.telehealth.web.controller;

import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author bostjanvukic
 */
@Controller
public class ErrorController {

// Error page
  @RequestMapping("/error")
  public String error(HttpServletRequest request, Model model) {
    model.addAttribute("errorCode", request.getAttribute("javax.servlet.error.status_code"));
    Throwable throwable = (Throwable) request.getAttribute("javax.servlet.error.exception");
    String errorMessage = null;
    if (throwable != null) {
      errorMessage = throwable.getMessage();
    }
    model.addAttribute("errorMessage", errorMessage);
    return "error.html";
  }    

}
