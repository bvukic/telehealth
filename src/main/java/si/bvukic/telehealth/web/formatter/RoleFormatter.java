/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package si.bvukic.telehealth.web.formatter;

import java.text.ParseException;
import java.util.Locale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.format.Formatter;
import si.bvukic.telehealth.core.model.Role;
import si.bvukic.telehealth.core.service.RoleService;

/**
 *
 * @author bostjanvukic
 */
public class RoleFormatter implements Formatter<Role> {
    
    private static final Logger logger = LoggerFactory.getLogger(RoleFormatter.class);
    private RoleService roleService;
        
    @Autowired(required=true)
    @Qualifier(value="roleService")
    public void setRoleService(RoleService roleService){
        this.roleService = roleService;
    }
        
        
	@Override
	public String print(Role role, Locale locale) {
		return "role_" + role.getId().toString();
	}

	@Override
	public Role parse(String id, Locale locale) throws ParseException {
            id = id.replace("role_", "");
            try {
		Role role = roleService.getRoleById(Long.parseLong(id));
		return role;
            } catch (NumberFormatException ex) {
                logger.error("Can't convert string " + id + " to the id of a Role object!");
            }
        return null;
	}
}
