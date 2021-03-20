package com.toy.artifact.security;

import java.util.List;

public interface AbstractCurrentUserService {
    public boolean hasRole(String role);

    public boolean hasAllRoles(List<String> roles);

    public boolean hasOneOfRoles(List<String> roles);

    public String getUsername();

    public Long getId();
}
