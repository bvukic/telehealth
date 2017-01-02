/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package si.bvukic.telehealth.ivr.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
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
public class IvrLoginController {
    private static final Logger LOG = LoggerFactory.getLogger(IvrLoginController.class);
    private UserService userService;
    
    @Autowired
    public IvrLoginController(UserService userService) {
        this.userService = userService;
    }

    // Login form
    @RequestMapping("/api/vxml/login")
    public String login(@RequestHeader HttpHeaders headers,
            Model model) throws MalformedSipAddressException {

        String callerSip = headers.getFirst("telephone-ani");
        String callerTel = SipAddressParser.getTel(callerSip, TelType.NATIONAL);
        
        LOG.info("Logging in {}", callerTel);
        
        User user = userService.getUserByPhone(callerTel);
        
        if (user != null) {
            LOG.info("Found user {} with phone {}", user.getUsername(), user.getPhone());
            model.addAttribute("username", user.getUsername());
            return "vxml/login.xml";
        } else {
            LOG.info("User with phone {} does not exists. Login failed.", callerTel
            );
            return "redirect:/api/vxml/login/userNotFound";
            //return "redirect:/api/vxml/input";
        }

    }

    // Login form with error
    @RequestMapping("/api/vxml/login/userNotFound")
    public String userNotFound(Model model) {

        return "vxml/userNotFound.xml";
    }

}
