/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package si.bvukic.telehealth.core.dao;

import java.util.List;
import si.bvukic.telehealth.core.model.User;

/**
 *
 * @author bostjanvukic
 */
public interface UserDAO {
 
    public void addUser(User user);
    public void updateUser(User user);
    public List<User> listUsers();
    public User getUserById(Long id);
    public void removeUser(Long id);
    public User getUserByUsername(String username);
    public User getUserByPhone(String phone);
}
