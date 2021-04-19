package com.okdev.ems.config.jwt;

import com.okdev.ems.models.Users;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtProvider {

    @Value("$(jwt.secretcode)")
    private String jwtSecretCode;

    public Map<String, String> generateToken(Users user) {
        Date date = Date.from(LocalDate.now().plusDays(30).atStartOfDay(ZoneId.systemDefault()).toInstant());
        String token = Jwts.builder()
                .setSubject(user.getEmail())
                .setExpiration(date)
                .signWith(SignatureAlgorithm.HS512, jwtSecretCode)
                .claim("userId", user.getUserId())
                .claim("firstName", user.getFirstName())
                .claim("lastName", user.getLastName())
                .compact();
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        return map;
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(jwtSecretCode)
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException expEx) {
            System.out.println("Token expired");
        } catch (UnsupportedJwtException unsEx) {
            System.out.println("Unsupported jwt");
        } catch (MalformedJwtException mjEx) {
            System.out.println("Malformed jwt");
        } catch (SignatureException sigEx) {
            System.out.println("Invalid signature");
        } catch (Exception ex) {
            System.out.println("Invalid token");
        }
        return false;
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
