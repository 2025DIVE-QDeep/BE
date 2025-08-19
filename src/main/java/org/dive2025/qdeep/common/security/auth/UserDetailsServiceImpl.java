package org.dive2025.qdeep.common.security.auth;

import org.dive2025.qdeep.domain.user.entity.User;
import org.dive2025.qdeep.domain.user.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;

@Service
public class UserDetailsServiceImpl implements UserDetailsService{

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByUsername(username)
                .orElseThrow(()-> new RuntimeException()); // 커스텀 예외 처리 필요

        return new UserDetailsImpl(user);
    }
}
