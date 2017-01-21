/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package si.bvukic.telehealth.web.controller;

import javax.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
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
    public String indexRedirect(HttpServletRequest request) {

        SecurityContext securityCtx = SecurityContextHolder.getContext();

        if (securityCtx.getAuthentication() != null
                && securityCtx.getAuthentication().isAuthenticated()
                && !(securityCtx.getAuthentication() instanceof AnonymousAuthenticationToken)) {
            if (request.isUserInRole("ROLE_PERMISSION_data_dashboard")) {
                return "redirect:/dashboard";
            } else if (request.isUserInRole("ROLE_PERMISSION_administration_users")) {
                return "redirect:/users";
            } else {
                return "redirect:/login/help";
            }
        }
        return "redirect:/login";

    }

}
