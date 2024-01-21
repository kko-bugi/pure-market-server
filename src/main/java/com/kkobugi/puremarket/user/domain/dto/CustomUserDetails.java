package com.kkobugi.puremarket.user.domain.dto;

import com.kkobugi.puremarket.user.domain.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class CustomUserDetails implements UserDetails {
    private final User user;

    public CustomUserDetails(User user) {
        this.user = user;
    }

    public String getLoginId() {
        return user.getLoginId();
    }

    public Long getUserIdx() {
        return user.getUserIdx();
    }

    public String getContact() {
        return user.getContact();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null; // 추후 인가 코드 필요 시 수정
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return null;
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
