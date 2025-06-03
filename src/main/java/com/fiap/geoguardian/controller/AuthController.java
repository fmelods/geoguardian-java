package com.fiap.geoguardian.controller;

import com.fiap.geoguardian.dto.LoginRequest;
import com.fiap.geoguardian.dto.LoginResponse;
import com.fiap.geoguardian.security.JwtTokenProvider;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")  // Libera acesso para o Swagger/localhost
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getSenha()
                )
        );

        String token = jwtTokenProvider.generateToken(authentication);

        return ResponseEntity.ok(new LoginResponse(token, "Bearer"));
    }
}
