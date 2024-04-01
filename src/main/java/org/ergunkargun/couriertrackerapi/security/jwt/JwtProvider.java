package org.ergunkargun.couriertrackerapi.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.ergunkargun.couriertrackerapi.configuration.security.JwtProperties;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;

import static java.util.stream.Collectors.joining;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtProvider {

    private static final String AUTHORITIES_KEY = "roles";

    private final JwtProperties jwtProperties;

    private SecretKey secretKey;

    @PostConstruct
    public void init() {
        var secret = Base64.getEncoder().encodeToString(jwtProperties.getSecretKey().getBytes());
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String createToken(Authentication authentication) {

        var claimsBuilder = Jwts.claims().subject(authentication.getName());
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        if (!authorities.isEmpty()) {
            claimsBuilder.add(AUTHORITIES_KEY, authorities.stream().map(GrantedAuthority::getAuthority).collect(joining(",")));
        }

        var claims = claimsBuilder.build();
        Date issuedAt = new Date();
        Date expiration = new Date(issuedAt.getTime() + jwtProperties.getExpirationDurationInSeconds());

        return Jwts.builder()
                .claims(claims)
                .issuedAt(issuedAt)
                .expiration(expiration)
                .signWith(this.secretKey, Jwts.SIG.HS256)
                .compact();
    }

    public Authentication getAuthentication(String token) {

        Claims claims = Jwts.parser().verifyWith(this.secretKey).build().parseSignedClaims(token).getPayload();

        Object authoritiesClaim = claims.get(AUTHORITIES_KEY);

        Collection<? extends GrantedAuthority> authorities = authoritiesClaim == null
                ? AuthorityUtils.NO_AUTHORITIES
                : AuthorityUtils.commaSeparatedStringToAuthorityList(authoritiesClaim.toString());

        User principal = new User(claims.getSubject(), StringUtils.EMPTY, authorities);

        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser().verifyWith(this.secretKey).build().parseSignedClaims(token);
            log.info("expiration date: {}", claims.getPayload().getExpiration());
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
        }
        return false;
    }
}
