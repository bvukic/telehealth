/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package si.bvukic.telehealth.core.dao;

import java.util.HashMap;
import java.util.List;
import si.bvukic.telehealth.core.model.Role;

/**
 *
 * @author bostjanvukic
 */
public interface RoleDAO {
 
    public Role getRoleById(Long id);
    public Role getRoleById(String id);
    public List<Role> listRoles();
    public HashMap<String,Role> listRolesl();
    public void addRole(Role role);
    public void updateRole(Role role);
    public void removeRole(Long id);

}
