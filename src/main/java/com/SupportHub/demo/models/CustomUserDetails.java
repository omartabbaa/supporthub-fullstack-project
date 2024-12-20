package com.SupportHub.demo.models;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.SupportHub.demo.repositories.AdminRepository;

public class CustomUserDetails implements UserDetails {
    private String name;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;
    private final User user;
    private final AdminRepository adminRepository;
    
    public CustomUserDetails(User user, AdminRepository adminRepository) {
        this.name = user.getEmail();
        this.password = user.getPassword();
        this.authorities = List.of(new SimpleGrantedAuthority(user.getRole()));
        this.user = user;
        this.adminRepository = adminRepository;
    }
    
    public Long getBusinessId() {
        String role = user.getRole();
        System.out.println("User role: " + role);
        
        if ("ROLE_ADMIN".equals(role)) {
            Admin admin = adminRepository.findByUser_UserId(user.getUserId());
            System.out.println("Found admin: " + (admin != null));
            if (admin != null && admin.getBusiness() != null) {
                System.out.println("Found business: " + admin.getBusiness().getBusinessId());
                return admin.getBusiness().getBusinessId();
            }
        }
        return null;
    }


    private Admin getAdminForUser(User user) {
        return adminRepository.findByUser_UserId(user.getUserId());
    }

    @Override
    public String getUsername() {
        return name;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
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

    public User getUser() {
        return user;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
        this.authorities = authorities;
    }
}
