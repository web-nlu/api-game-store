package vn.edu.hcmaf.apigamestore.config.authSecurity;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;
import vn.edu.hcmaf.apigamestore.common.util.JwtUtil;

import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private  JwtUtil jwtUtil;
    @Autowired
    private CustomUserDetailService customUserDetailService;

    /**
     * This filter intercepts incoming requests to check for a valid JWT token.
     * If the token is valid, it sets the authentication in the SecurityContext.
     *
     * @param request  The HTTP request
     * @param response The HTTP response
     * @param filterChain The filter chain to continue processing the request
     * @throws ServletException If an error occurs during filtering
     * @throws IOException If an I/O error occurs
     */
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        // Extract the JWT token from the request header
        String jwtToken = getTokenFromRequest(request);

        if (jwtToken != null && jwtUtil.validateToken(jwtToken) ) {
            String username = jwtUtil.getEmailFromToken(jwtToken);
            // Set user in requset context
            UserDetails userDetails = customUserDetailService.loadUserByUsername(username);
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.getAuthorities()
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);


        }
        // Continue with the filter chain
        filterChain.doFilter(request, response);
    }
    /**
     * Extracts the JWT token from the Authorization header of the request.
     *
     * @param request The HTTP request
     * @return The JWT token if present, otherwise null
     */
    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
