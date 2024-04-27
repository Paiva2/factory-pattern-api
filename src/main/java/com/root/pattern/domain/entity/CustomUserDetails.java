package com.root.pattern.domain.entity;

import com.root.pattern.domain.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@AllArgsConstructor
@Builder
public class CustomUserDetails implements UserDetails {
    private String id;
    private String password;
    private Role role;

    private static final Role[] AVAILABLE_ROLES = Role.values();

    private void insertRoleOnAuthorities(String role, Set<SimpleGrantedAuthority> authorities) {
        StringBuilder roleStringBuilder = new StringBuilder();

        roleStringBuilder.append("ROLE_");
        roleStringBuilder.append(role.toUpperCase());

        authorities.add(new SimpleGrantedAuthority(roleStringBuilder.toString()));
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();

        if (this.role.equals(Role.ADMIN)) {
            for (Role availableRole : AVAILABLE_ROLES) {
                this.insertRoleOnAuthorities(availableRole.toString(), authorities);
            }
        } else {
            this.insertRoleOnAuthorities(this.role.toString(), authorities);
        }

        return authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.id;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
