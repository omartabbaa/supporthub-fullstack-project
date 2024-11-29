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

@Service
@Primary
public class AuthenticationService implements UserDetailsService {

    private final com.SupportHub.demo.services.UserService userService;

    public AuthenticationService(com.SupportHub.demo.services.UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> userOptional = userService.findUserEntityByEmail(email);
        User user = userOptional.orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        List<GrantedAuthority> grantedAuthorities = List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole()));

        CustomUserDetails customUserDetails = new CustomUserDetails(user);
        customUserDetails.setAuthorities(grantedAuthorities);

        return customUserDetails;
    }
}
