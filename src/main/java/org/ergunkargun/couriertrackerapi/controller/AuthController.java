package org.ergunkargun.couriertrackerapi.controller;

import org.ergunkargun.couriertrackerapi.jpa.entity.ApiUser;
import org.ergunkargun.couriertrackerapi.jpa.entity.enumaration.Role;
import org.ergunkargun.couriertrackerapi.model.Auth;
import org.ergunkargun.couriertrackerapi.security.jwt.JwtProvider;
import org.ergunkargun.couriertrackerapi.service.ApiUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final ApiUserService apiUserService;

    private final AuthenticationManager authenticationManager;

    private  final PasswordEncoder passwordEncoder;

    private final JwtProvider jwtProvider;

    public AuthController(ApiUserService apiUserService, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder, JwtProvider jwtProvider) {
        this.apiUserService = apiUserService;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody Auth auth) {

        if (apiUserService.isExists(auth.getUsername())) {
            return ResponseEntity.badRequest().body("Username is taken");
        }

        apiUserService.create(ApiUser.builder()
                .username(auth.getUsername())
                .password(passwordEncoder.encode(auth.getPassword()))
                .role(Role.USER)
                .build()
        );

        return ResponseEntity.ok("Successful Registration");
    }

    @PostMapping("/sign-in")
    public ResponseEntity<?> login(@RequestBody Auth auth){

        try {
            var authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(auth.getUsername(), auth.getPassword()));

            String token = jwtProvider.createToken(authentication);

            Map<Object, Object> model = new HashMap<>();
            model.put("username", auth.getUsername());
            model.put("token", token);

            return ok(model);
        } catch (AuthenticationException e) {
            throw new BadCredentialsException(String.format("Failed Authentication. %s", e.getLocalizedMessage()));
        }
    }
}
