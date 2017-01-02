/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package si.bvukic.telehealth.core.dao;

import java.util.HashMap;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import si.bvukic.telehealth.core.model.Role;

/**
 *
 * @author bostjanvukic
 */
public class RoleDAOImpl implements RoleDAO {

    private static final Logger LOG = LoggerFactory.getLogger(RoleDAOImpl.class);
    
    private SessionFactory sessionFactory;
     
    public void setSessionFactory(SessionFactory sessionFactory){
        this.sessionFactory = sessionFactory;
    }
    
    @Override
    public Role getRoleById(Long id) {
        Session session = this.sessionFactory.getCurrentSession();      
        Role role = (Role) session.load(Role.class, new Long(id));
        LOG.info("Role " + role.getName() + " loaded successfully");
        LOG.debug("Role details [" + role + "]");
        return role;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Role> listRoles() {
        Session session = this.sessionFactory.getCurrentSession();
        List<Role> rolesList = session.createQuery("from Role").list();
        for(Role role : rolesList){
            LOG.debug("Role List::"+role);
        }
        return rolesList;
    }
    
    @Override
    public void addRole(Role role) {
        Session session = this.sessionFactory.getCurrentSession();
        session.persist(role);
        LOG.info("Role " + role.getName() +  " added successfully");
        LOG.debug("Role details [" + role + "]");
    }
 
    @Override
    public void updateRole(Role role) {
        Session session = this.sessionFactory.getCurrentSession();
        session.update(role);
        LOG.info("Role " + role.getName() + " updated successfully");
        LOG.debug("Role details [" + role + "]");
    }

    @Override
    public void removeRole(Long id) {
        Session session = this.sessionFactory.getCurrentSession();
        Role role = (Role) session.load(Role.class, new Long(id));
        if(null != role){
            session.delete(role);
        }
        LOG.info("Role " + role.getName() + " deleted successfully");
        LOG.debug("Role details [" + role + "]");
    }

    @Override
    public Role getRoleById(String id) {
        
        return this.getRoleById(new Long(id));
    }

    @Override
    public HashMap<String, Role> listRolesl() {
        List<Role> rolesList = this.listRoles();
        HashMap<String, Role> rolesHashMap = new HashMap<String, Role>();
        for(Role role : rolesList){
            rolesHashMap.put(role.getId().toString(), role);
            LOG.debug("Role HashMap::"+role.getId().toString() + "::" +role);
        }
        
        return rolesHashMap;
    }
    
}
