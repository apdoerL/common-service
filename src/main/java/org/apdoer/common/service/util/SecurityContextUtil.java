package org.apdoer.common.service.util;

import org.apdoer.common.service.model.po.UserPo;
import org.apdoer.common.service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class SecurityContextUtil {
    @Autowired
    private UserService userDbService;

    public SecurityContextUtil() {
    }

    public Integer getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (null == authentication) {
            return null;
        } else if (!(authentication instanceof AnonymousAuthenticationToken)) {
            UserPo user = this.userDbService.getUserByUsername(authentication.getName());
            return user.getId();
        } else {
            return null;
        }
    }

    public String getCurrentUserName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (null == authentication) {
            return null;
        } else {
            return !(authentication instanceof AnonymousAuthenticationToken) ? authentication.getName() : null;
        }
    }

    public UserPo getUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (null == authentication) {
            return null;
        } else {
            return !(authentication instanceof AnonymousAuthenticationToken) ? this.userDbService.getUserByUsername(authentication.getName()) : null;
        }
    }

    public boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (null == authentication) {
            return false;
        } else {
            return authentication.isAuthenticated();
        }
    }

    public List<String> getPermissions() {
        List<String> permissions = new ArrayList();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (null == authentication) {
            return permissions;
        } else {
            List<GrantedAuthority> authorities = (List)authentication.getAuthorities();
            Iterator var4 = authorities.iterator();

            while(var4.hasNext()) {
                GrantedAuthority auth = (GrantedAuthority)var4.next();
                permissions.add(auth.getAuthority());
            }

            return permissions;
        }
    }
}
