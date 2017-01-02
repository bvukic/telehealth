/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package si.bvukic.telehealth.core.security;

import org.springframework.security.core.GrantedAuthority;

/**
 *
 * @author bostjanvukic
 */
public class AppAuthority implements GrantedAuthority {
 
    private String authority;
 
    public AppAuthority(String authority) {
        this.authority = authority;
    }
 
    @Override
    public String getAuthority() {
        return authority;
    }
 
    @Override
    public int hashCode() {
        return authority.hashCode();
    }
 
    @Override
    public boolean equals(Object obj) {
        if(obj == null) return false;
        if(!(obj instanceof AppAuthority)) return false;
        return ((AppAuthority) obj).getAuthority().equals(authority);
    }

    @Override
    public String toString() {
        return authority;
    }
    
    
}