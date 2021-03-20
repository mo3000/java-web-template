package com.toy.artifact.security.admin;

import com.toy.artifact.db.service.AdminService;
import com.toy.artifact.db.vo.AdminVo;
import com.toy.artifact.security.AbstractCurrentUserService;
import com.toy.artifact.utils.jwt.JwtWrapper;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class CurrentAdmin implements AbstractCurrentUserService {

    public CurrentAdmin(JwtWrapper credentialHolder, AdminService adminService) {
        this.credentialHolder = credentialHolder;
        this.adminService = adminService;
    }

    private final JwtWrapper credentialHolder;
    private AdminVo admin;
    private final AdminService adminService;
    private Set<String> roles;


    @Override
    public boolean hasRole(String role) {
        setUserIfEmpty();
        return roles.contains(role);
    }

    @Override
    public boolean hasOneOfRoles(List<String> rolesTarget) {
        setUserIfEmpty();
        for (String role : rolesTarget) {
            if (roles.contains(role)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean hasAllRoles(List<String> rolesTarget) {
        setUserIfEmpty();
        return roles.containsAll(rolesTarget);
    }

    @Override
    public String getUsername() {
        setUserIfEmpty();
        return admin.getUsername();
    }

    @Override
    public Long getId() {
        setUserIfEmpty();
        return admin.getId();
    }

    public void setUserIfEmpty() {
        if (admin == null) {
            if (! credentialHolder.isTokenSet()) {
                throw new RuntimeException("user token not set in credential");
            }
            admin = adminService.findById(credentialHolder.getUserid());
            roles = new HashSet<>();
            admin.getRoles().forEach(role -> {
                roles.add(role.getName());
            });
        }
    }
}
