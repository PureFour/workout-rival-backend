package com.ruczajsoftware.workoutrival.service.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.ruczajsoftware.workoutrival.model.database.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtUtil {

    private static final long EXPIRATION_TIME = 1000 * 60 * 60;

    public String extractLogin(String token) {
        return JWT.decode(token).getSubject();
    }

    private Date extractExpiration(String token) {
        return JWT.decode(token).getExpiresAt();
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String generateToken(User user) {
        return createToken(user.getEmail());
    }

    private String createToken(String subject) {
        return JWT.create()
                .withSubject(subject)
                .withIssuedAt(new Date(System.currentTimeMillis()))
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(Algorithm.HMAC256("secret"));
    }

    public Boolean validateToken(String token, UserDetails user) {
        final String login = extractLogin(token);
        return login.equals(user.getUsername()) && !isTokenExpired(token);
    }
}
