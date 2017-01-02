/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package si.bvukic.telehealth.core.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import si.bvukic.telehealth.core.dao.UserDAO;
import si.bvukic.telehealth.core.model.User;

/**
 *
 * @author vukicb
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService{
    
    private UserDAO userDAO;
    
    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }
    

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String string) throws UsernameNotFoundException {
        User user = userDAO.getUserByUsername(string);
        return user;
        
    }
    
}
