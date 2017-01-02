/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package si.bvukic.telehealth.ivr.security;

import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.switchuser.SwitchUserFilter;
import si.bvukic.telehealth.core.service.UserService;
import si.bvukic.telehealth.ivr.util.SipAddressParser;
import si.bvukic.telehealth.ivr.util.model.MalformedSipAddressException;
import si.bvukic.telehealth.ivr.util.model.TelType;

/**
 *
 * @author vukicb
 */
public class IvrLoginFilter extends SwitchUserFilter {

    private static final Logger LOG = LoggerFactory.getLogger(IvrLoginFilter.class);
    private UserDetailsService userDetailsService;

    @Autowired
    public IvrLoginFilter(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }


    @Override
    protected Authentication attemptSwitchUser(HttpServletRequest request) throws AuthenticationException {
        Authentication current = SecurityContextHolder.getContext().getAuthentication();
        LOG.debug("Current user is {}", current.getName());
        
        //TODO check API key. VXML browser must send the API key in order to login/switch user.
        
        String callerSip = request.getHeader("telephone-ani");
        if (callerSip == null) {
            LOG.error("Login failed! Missing HTTP header telephone-ani.");
            throw new AuthenticationCredentialsNotFoundException("Missing HTTP header telephone-ani.");
        }
        
        try {
            String callerTel = SipAddressParser.getTel(callerSip, TelType.NATIONAL);
            LOG.info("Logging in user with phone number: {}", callerTel);
        } catch (MalformedSipAddressException ex) {
            LOG.error("Login failed! Can't parse telephone-ani HTTP header.");
            throw new AuthenticationCredentialsNotFoundException("Can't parse sip addres from telephone-ani HTTP header.");
        }

        
        return super.attemptSwitchUser(request);
    }

    @Override
    protected Authentication attemptExitUser(HttpServletRequest request) throws AuthenticationCredentialsNotFoundException {
        Authentication current = SecurityContextHolder.getContext().getAuthentication();
        
        LOG.info("Logging out user {}", current.getName());
        
        // TODO pre-logout stuff
        return super.attemptExitUser(request);
    }
}
