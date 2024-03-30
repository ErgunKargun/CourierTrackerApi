package org.ergunkargun.couriertrackerapi.controller;

import org.ergunkargun.couriertrackerapi.hateoas.ApiUserModelAssembler;
import org.ergunkargun.couriertrackerapi.jpa.entity.ApiUser;
import org.ergunkargun.couriertrackerapi.jpa.entity.enumaration.Role;
import org.ergunkargun.couriertrackerapi.model.Auth;
import org.ergunkargun.couriertrackerapi.security.jwt.JwtProvider;
import org.ergunkargun.couriertrackerapi.service.ApiUserService;
import org.springframework.hateoas.IanaLinkRelations;
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

    private final AuthenticationManager authenticationManager;

    private  final PasswordEncoder passwordEncoder;

    private final JwtProvider jwtProvider;

    private final ApiUserService apiUserService;

    private final ApiUserModelAssembler apiUserModelAssembler;

    public AuthController(AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder, JwtProvider jwtProvider, ApiUserService apiUserService, ApiUserModelAssembler apiUserModelAssembler) {
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
        this.apiUserService = apiUserService;
        this.apiUserModelAssembler = apiUserModelAssembler;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Auth auth) {

        if (apiUserService.isExists(auth.getUsername())) {
            return ResponseEntity.badRequest().body("Username is taken");
        }

        try {
            var createdUser = apiUserService.create(ApiUser.builder()
                    .username(auth.getUsername())
                    .password(passwordEncoder.encode(auth.getPassword()))
                    .role(Role.USER)
                    .build()
            );

            var entityModel = apiUserModelAssembler.toModel(createdUser);

            return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(String.format("Failed Registration. %s", e.getLocalizedMessage()));
        }
    }

    @PostMapping("/sign-in")
    public ResponseEntity<?> signIn(@RequestBody Auth auth){

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
