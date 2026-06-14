package com.leandro.identityAccessService.security.service;

import com.leandro.identityAccessService.security.dto.LoginDTO;
import com.leandro.identityAccessService.security.dto.LoginRequestDTO;
import com.leandro.identityAccessService.user.model.User;
import com.leandro.identityAccessService.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthService {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;

    public LoginDTO login(LoginRequestDTO loginRequestDTO){

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequestDTO.getEmail(),
                        loginRequestDTO.getPassword()
                )
        );

        User user = userRepository.findByEmailAndDeletedAtIsNull(loginRequestDTO.getEmail()).orElseThrow();

        String token = jwtService.generateToken(user);

        return new LoginDTO(token);
    }
}
