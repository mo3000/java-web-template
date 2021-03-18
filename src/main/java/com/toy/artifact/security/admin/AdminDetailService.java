package com.toy.artifact.security.admin;

import com.toy.artifact.db.entity.Admins;
import com.toy.artifact.db.service.AdminService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AdminDetailService implements UserDetailsService {

    private final AdminService adminService;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Admins admin = adminService.findByUsername(s);
        return new AdminDetails(admin);
    }

    public AdminDetailService(AdminService adminService) {
        this.adminService = adminService;
    }
}
