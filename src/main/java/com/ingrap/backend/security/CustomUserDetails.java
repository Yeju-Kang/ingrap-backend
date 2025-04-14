package com.ingrap.backend.security;

import lombok.Getter;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;

@Getter
public class CustomUserDetails implements UserDetails {
    private final Long id;
    private final String email;

    public CustomUserDetails(Long id, String email) {
        this.id = id;
        this.email = email;
    }

    @Override public Collection<? extends GrantedAuthority> getAuthorities() { return null; }
    @Override public String getPassword() { return null; }
    @Override public String getUsername() { return email; }
    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }
}
