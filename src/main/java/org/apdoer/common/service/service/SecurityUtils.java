package org.apdoer.common.service.service;

import org.apdoer.common.service.model.po.UserPo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author apdoer
 */
@Service
public class SecurityUtils {

    @Autowired
    private UserService userService;

    public Integer getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (null == authentication) {
            return null;
        }
        String currentUserName = null;
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            currentUserName = authentication.getName();
            UserPo user = userService.getUserByUsername(currentUserName);
            return user.getId();
        }
        return null;
    }

    public UserPo getUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (null == authentication) {
            return null;
        }
        String currentUserName = null;
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            currentUserName = authentication.getName();
            return userService.getUserByUsername(currentUserName);
        } else {
            return null;
        }
    }

    public String getCurrentUserName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (null == authentication) {
            return null;
        }
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            return authentication.getName();

        }
        return null;
    }

    public boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (null == authentication) {
            return false;
        }
        if (authentication.isAuthenticated()) {
            return true;
        } else {
            return false;
        }
    }

    public List<String> getPermissions() {
        List<String> permissions = new ArrayList<>();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (null == authentication) {
            return permissions;
        }
        List<GrantedAuthority> authorities = (List<GrantedAuthority>) authentication.getAuthorities();
        for (GrantedAuthority auth : authorities) {
            permissions.add(auth.getAuthority());
        }
        return permissions;
    }
}
