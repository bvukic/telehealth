/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package si.bvukic.telehealth.core.service;

import si.bvukic.telehealth.core.model.User;

/**
 *
 * @author vukicb
 */
public interface EmailNotificationService {
    
    void sendRegistrationNotification(User user);
    void sendPasswordResetNotification(User user);
    
}
