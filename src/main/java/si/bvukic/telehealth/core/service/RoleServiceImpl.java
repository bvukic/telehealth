/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package si.bvukic.telehealth.core.service;

import java.util.HashMap;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import si.bvukic.telehealth.core.dao.RoleDAO;
import si.bvukic.telehealth.core.model.Role;

/**
 *
 * @author bostjanvukic
 */
@Service
public class RoleServiceImpl implements RoleService {
    
    private RoleDAO roleDAO;
    
    public void setRoleDAO(RoleDAO roleDAO) {
        this.roleDAO = roleDAO;
    }
    
    @Override
    @Transactional
    public Role getRoleById(Long id) {
        return this.roleDAO.getRoleById(id);
    }
 
    @Override
    @Transactional
    public List<Role> listRoles() {
        return this.roleDAO.listRoles();
    }

    @Override
    @Transactional
    public Role getRoleById(String id) {
        return this.roleDAO.getRoleById(id);
    }

    @Override
    @Transactional
    public HashMap<String, Role> listRolesl() {
        return this.roleDAO.listRolesl();
    }
   
}
