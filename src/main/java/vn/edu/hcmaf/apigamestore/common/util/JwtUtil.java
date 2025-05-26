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
    /* * Generates a JWT token for the given email.
     * The token will be valid for one month.
     *
     * @param email the email to include in the token
     * @return the generated JWT token
     */
    public  String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + ONE_MONTH))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
    /**
     * Generates a refresh token for the given email.
     * The refresh token will be valid for one year.
     *
     * @param email the email to include in the refresh token
     * @return the generated refresh token
     */
    public  String generateRefreshToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + ONE_YEAR))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
    /**
     * Extracts the email from the given JWT token.
     *
     * @param token the JWT token
     * @return the email extracted from the token
     */
    public  String getEmailFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
    /**
     * Validates the given JWT token.
     *
     * @param token the JWT token to validate
     * @return true if the token is valid, false otherwise
     * @throws IllegalArgumentException if the token is invalid
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
          throw new IllegalArgumentException("Invalid token");
        }
    }
}