/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package si.bvukic.telehealth.core.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AbstractAuthenticationEvent;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

/**
 *
 * @author bostjanvukic
 */
@Component
public class AuthenticationEventListener implements ApplicationListener<AbstractAuthenticationEvent> {

   private static final Logger logger = LoggerFactory.getLogger(AuthenticationEventListener.class);

   @Override
   public void onApplicationEvent(AbstractAuthenticationEvent authenticationEvent) {
      if (authenticationEvent instanceof InteractiveAuthenticationSuccessEvent) {
         // ignores to prevent duplicate logging with AuthenticationSuccessEvent
         return;
      }
      Authentication authentication = authenticationEvent.getAuthentication();
      logger.info("Login attempt [username: " + authentication.getName() + "] [success: " + authentication.isAuthenticated() + "]");
      //TODO: Disable account on to many failed login attempts
   }
}
