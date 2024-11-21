package com.SupportHub.demo.services;

import java.util.List;
import java.util.Optional;

import org.springframework.context.annotation.Primary;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.SupportHub.demo.models.CustomUserDetails;
import com.SupportHub.demo.models.User;
import com.SupportHub.demo.repositories.AdminRepository;

@Service
@Primary
public class AuthenticationService implements UserDetailsService {

    private final com.SupportHub.demo.services.UserService userService;
    private final AdminRepository adminRepository;

    public AuthenticationService(com.SupportHub.demo.services.UserService userService, AdminRepository adminRepository) {
        this.userService = userService;
        this.adminRepository = adminRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> userOptional = userService.findUserEntityByEmail(email);
        User user = userOptional.orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        List<GrantedAuthority> grantedAuthorities = List.of(new SimpleGrantedAuthority(user.getRole()));

        CustomUserDetails customUserDetails = new CustomUserDetails(user, adminRepository);
        customUserDetails.setAuthorities(grantedAuthorities);

        return customUserDetails;
    }
}
