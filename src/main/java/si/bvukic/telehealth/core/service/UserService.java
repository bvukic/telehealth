/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package si.bvukic.telehealth.core.service;

import java.util.List;
import si.bvukic.telehealth.core.model.User;

/**
 *
 * @author bostjanvukic
 */
public interface UserService {

    public void addUser(User u);
    public void updateUser(User u);
    public List<User> listUsers();
    public User getUserById(Long id);
    public User getUserByUsername(String username);
    public void removeUser(Long id);
    public boolean isUsernameTaken(String username);
    public User getUserByPhone(String phone);
    public boolean isPhoneTaken(String phone);
}
