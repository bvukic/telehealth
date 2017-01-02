/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package si.bvukic.telehealth.core.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import si.bvukic.telehealth.core.dao.UserDAO;
import si.bvukic.telehealth.core.model.User;

/**
 *
 * @author bostjanvukic
 */
@Service
public class UserServiceImpl implements UserService {
    
    private UserDAO userDAO;
    
    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }
    
    @Override
    @Transactional
    public void addUser(User u) {
        this.userDAO.addUser(u);
    }
 
    @Override
    @Transactional
    public void updateUser(User u) {
        this.userDAO.updateUser(u);
    }
 
    @Override
    @Transactional
    public List<User> listUsers() {
        return this.userDAO.listUsers();
    }
 
    @Override
    @Transactional
    public User getUserById(Long id) {
        return this.userDAO.getUserById(id);
    }
 
    @Override
    @Transactional
    public void removeUser(Long id) {
        this.userDAO.removeUser(id);
    }

    @Override
    @Transactional
    public User getUserByUsername(String username) {
        return this.userDAO.getUserByUsername(username);
    }

    @Override
    @Transactional
    public boolean isUsernameTaken(String username) {
        User user = this.userDAO.getUserByUsername(username);
        
        return (user != null);
    }

    @Override
    @Transactional
    public User getUserByPhone(String phone) {
        return this.userDAO.getUserByPhone(phone);
    }

    @Override
    @Transactional
    public boolean isPhoneTaken(String phone) {
        User user = this.userDAO.getUserByPhone(phone);
        
        return (user != null);
    }


    
}
