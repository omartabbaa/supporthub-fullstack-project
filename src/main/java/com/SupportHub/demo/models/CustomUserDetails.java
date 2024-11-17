package com.SupportHub.demo.models;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.SupportHub.demo.repositories.AdminRepository;

public class CustomUserDetails implements UserDetails {
    private String name;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;
    private final User user;
    
    public CustomUserDetails(User user) {
        this.name = user.getEmail();
        this.password = user.getPassword();
        this.authorities = List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole()));
        this.user = user;
    }
@Autowired
private AdminRepository adminRepository;
    
    public Long getBusinessId() {
        if ("ADMIN".equalsIgnoreCase(user.getRole())) {
            return Optional.ofNullable(getAdminForUser(user))
                    .map(Admin::getBusiness)
                    .map(Business::getBusinessId)
                    .orElse(null);
        }
        return null;
    }
        private Admin getAdminForUser(User user) {
        // Assuming AdminRepository is injected or instantiated correctly
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

    // Other methods remain unchanged
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

    // Setters (if needed)
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
