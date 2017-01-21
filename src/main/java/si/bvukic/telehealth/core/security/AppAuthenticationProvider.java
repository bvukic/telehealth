/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package si.bvukic.telehealth.core.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import si.bvukic.telehealth.core.model.User;
import si.bvukic.telehealth.core.service.UserService;

/**
 *
 * @author bostjanvukic
 */
@Component("authenticationProvider")
public class AppAuthenticationProvider implements AuthenticationProvider {
 
    private static final Logger LOG = LoggerFactory.getLogger(AppAuthenticationProvider.class);
    private UserService userService;
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired(required=true)
    @Qualifier(value="userService")
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
    
    @Autowired(required=true)
    @Qualifier(value="passwordEncoder")
    public void setPasswordEncoder(BCryptPasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }
 
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        LOG.debug("Processing authentication request for: {}", authentication.getPrincipal().toString());
        
        User user = userService.getUserByUsername(authentication.getPrincipal().toString());       
        
        if(user == null){
            throw new UsernameNotFoundException(String.format("Invalid credentials", authentication.getPrincipal()));
        }
 
        String suppliedPassword = authentication.getCredentials().toString();
 
        if(!passwordEncoder.matches(suppliedPassword, user.getPassword())){
            throw new BadCredentialsException("Invalid credentials");
        }
        
        if(!user.isEnabled()){
            throw new AccountStatusException("Account is disabled") {}; 
        }
 
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        LOG.debug("Successfully authenticated user: {}", user.getUsername());
        LOG.debug("User {} has authorities: {}", user.getUsername(), user.getAuthorities());
        return token;
    }
 
    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(UsernamePasswordAuthenticationToken.class);
    }
}