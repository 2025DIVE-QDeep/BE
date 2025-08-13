package org.dive2025.qdeep.common.security.auth;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.dive2025.qdeep.domain.user.entity.User;

import java.util.Collection;
import java.util.List;

public class UserDetailsImpl implements UserDetails {

    private final User user;

    public UserDetailsImpl(User user){
        this.user = user;
    }

    // User 객체에서 Role 필드를 Authority로 넣어주기
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(
                ()->user.getRole().name());
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }
}
