package com.leandro.identityAccessService.security.controller;

import com.leandro.identityAccessService.security.dto.LoginDTO;
import com.leandro.identityAccessService.security.dto.LoginRequestDTO;
import com.leandro.identityAccessService.security.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LoginDTO> login(@Valid @RequestBody LoginRequestDTO loginRequestDTO) {
        LoginDTO loginDTO = authService.login(loginRequestDTO);
        return ResponseEntity.status(HttpStatus.OK).body(loginDTO);
    }
}
