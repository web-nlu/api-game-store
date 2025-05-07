package vn.edu.hcmaf.apigamestore.common.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import vn.edu.hcmaf.apigamestore.common.constants.SecurityConstants;

import java.security.Key;
import java.util.Date;

import static vn.edu.hcmaf.apigamestore.common.constants.SecurityConstants.*;

@Component
public class JwtUtil {



    private  final Key key = Keys.hmacShaKeyFor(JWT_SECRET_KEY.getBytes());

    public  String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + ONE_MONTH))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
    public  String generateRefreshToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + ONE_YEAR))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public  String getUsernameFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public  boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            e.printStackTrace();
            return false;
        }
    }
}