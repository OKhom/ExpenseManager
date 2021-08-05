package com.okdev.ems.config.jwt;

import com.okdev.ems.dto.TokenDTO;
import com.okdev.ems.dto.TokenValidateDTO;
import com.okdev.ems.exceptions.EmsAuthException;
import com.okdev.ems.models.Users;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@Component
public class JwtProvider {

    @Value("${jwt.secretcode}")
    private String jwtSecretCode;

    private final Logger log = LoggerFactory.getLogger(JwtProvider.class);

    public TokenDTO generateToken(Users user) {
        Date date = Date.from(LocalDate.now().plusDays(15).atStartOfDay(ZoneId.systemDefault()).toInstant());
        String token = Jwts.builder()
                .setSubject(user.getEmail())
                .setExpiration(date)
                .signWith(SignatureAlgorithm.HS512, jwtSecretCode)
                .claim("userId", user.getUserId())
                .claim("firstName", user.getFirstName())
                .claim("lastName", user.getLastName())
                .compact();
        return new TokenDTO(token);
    }

    public TokenValidateDTO validateToken(String token) throws EmsAuthException {
        try {
            Jwts.parser()
                    .setSigningKey(jwtSecretCode)
                    .parseClaimsJws(token);
            return TokenValidateDTO.of(true, "Token valid");
        } catch (ExpiredJwtException expEx) {
            log.info("Token expired");
            return TokenValidateDTO.of(false, "Token expired");
        } catch (UnsupportedJwtException unsEx) {
            log.info("Unsupported jwt");
            return TokenValidateDTO.of(false, "Unsupported jwt");
        } catch (MalformedJwtException mjEx) {
            log.info("Malformed jwt");
            return TokenValidateDTO.of(false, "Malformed jwt");
        } catch (SignatureException sigEx) {
            log.info("Token invalid signature");
            return TokenValidateDTO.of(false, "Token invalid signature");
        } catch (Exception ex) {
            log.info("Invalid token");
            return TokenValidateDTO.of(false, "Invalid token");
        }
    }

    public String getEmailFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecretCode)
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    public Long getIdFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecretCode)
                .parseClaimsJws(token)
                .getBody();
        return Long.parseLong(String.valueOf(claims.get("userId")));
    }
}
