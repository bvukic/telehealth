/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package si.bvukic.telehealth.ivr.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import si.bvukic.telehealth.web.controller.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import si.bvukic.telehealth.core.model.User;
import si.bvukic.telehealth.core.service.UserService;
import si.bvukic.telehealth.ivr.util.SipAddressParser;
import si.bvukic.telehealth.ivr.util.model.MalformedSipAddressException;
import si.bvukic.telehealth.ivr.util.model.TelType;

/**
 *
 * @author bostjanvukic
 */
@Controller
public class IvrLogoutController {

    private static final Logger LOG = LoggerFactory.getLogger(IvrLogoutController.class);
    private final UserService userService;

    @Autowired
    public IvrLogoutController(UserService userService) {
        this.userService = userService;
    }

    // Logout form
    @PreAuthorize("isAuthenticated()")
    @RequestMapping("/api/vxml/logout")
    public String logout(HttpServletRequest request, Model model) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
            LOG.info("User {} logedout from IVR session.", username);
        } else {
            LOG.warn("Can' find IVR HTTP session for user {}.", username);
        }
        model.addAttribute("username", username);
        return "vxml/logout.xml";
    }

}
