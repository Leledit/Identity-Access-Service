package com.leandro.identityAccessService.security.service;

import com.leandro.identityAccessService.security.model.SecurityUser;
import com.leandro.identityAccessService.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import com.leandro.identityAccessService.user.model.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmailAndDeletedAtIsNull(email).orElseThrow(() -> new UsernameNotFoundException(email));
        return new SecurityUser(user);
    }
}
