/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package si.bvukic.telehealth.core.dao;

import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import si.bvukic.telehealth.core.model.User;

/**
 *
 * @author bostjanvukic
 */
public class UserDAOImpl implements UserDAO {
    
    private static final Logger LOG = LoggerFactory.getLogger(UserDAOImpl.class);
    
        private SessionFactory sessionFactory;
     
    public void setSessionFactory(SessionFactory sessionFactory){
        this.sessionFactory = sessionFactory;
    }
 
    @Override
    public void addUser(User user) {
        Session session = this.sessionFactory.getCurrentSession();
        session.persist(user);
        LOG.info("User " + user.getUsername() +  " added successfully");
        LOG.debug("User details [" + user + "]");
    }
 
    @Override
    public void updateUser(User user) {
        Session session = this.sessionFactory.getCurrentSession();
        session.update(user);
        LOG.info("User " + user.getUsername() + " updated successfully");
        LOG.debug("User details [" + user + "]");
    }
 
    @SuppressWarnings("unchecked")
    @Override
    public List<User> listUsers() {
        Session session = this.sessionFactory.getCurrentSession();
        List<User> usersList = session.createQuery("from User").list();
        for(User user : usersList){
            LOG.debug("User List::"+user);
        }
        return usersList;
    }
 
    @Override
    public User getUserById(Long id) {
        Session session = this.sessionFactory.getCurrentSession();      
        User user = (User) session.load(User.class, new Long(id));
        LOG.info("User " + user.getUsername() + " loaded successfully");
        LOG.debug("User details [" + user + "]");
        return user;
    }
    
    @Override
    public User getUserByUsername(String username) {
        Session session = this.sessionFactory.getCurrentSession();      
        Criteria criteria = session.createCriteria(User.class);
        User user = new User();
        try {
            user = (User) criteria.add(Restrictions.eqOrIsNull("username", username)).uniqueResult();
            LOG.info("Successfully loaded user by username: {}", user.getUsername());
            LOG.debug("User details [ {} ]", user);
        } catch (NullPointerException ex) {
            user = null;
            LOG.info("No user found by username: {}", username);
        }
        return user;
    }
 
    @Override
    public void removeUser(Long id) {
        Session session = this.sessionFactory.getCurrentSession();
        User user = (User) session.load(User.class, new Long(id));
        if(null != user){
            session.delete(user);
        }
        LOG.info("User " + user.getUsername() + " deleted successfully");
        LOG.debug("User details [" + user + "]");
    }

    @Override
    public User getUserByPhone(String phone) {
        Session session = this.sessionFactory.getCurrentSession();      
        Criteria criteria = session.createCriteria(User.class);
        User user = new User();
        try {
            user = (User) criteria.add(Restrictions.eqOrIsNull("phone", phone)).uniqueResult();
            LOG.info("Successfully loaded user by phone: {}", user.getPhone());
            LOG.debug("User details [ {} ]", user);
        } catch (NullPointerException ex) {
            user = null;
            LOG.info("No user found by phone: {}", phone);
        }
        return user;
    }
    
}
