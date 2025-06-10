package vn.edu.hcmaf.apigamestore.config.authSecurity;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.OncePerRequestFilter;
import vn.edu.hcmaf.apigamestore.auth.AuthService;
import vn.edu.hcmaf.apigamestore.auth.dto.LoginResponseDto;
import vn.edu.hcmaf.apigamestore.auth.dto.request.RegisterRequestDto;
import vn.edu.hcmaf.apigamestore.role.RoleEntity;
import vn.edu.hcmaf.apigamestore.role.RoleService;
import vn.edu.hcmaf.apigamestore.role.UserRole.UserRoleEntity;
import vn.edu.hcmaf.apigamestore.user.UserEntity;
import vn.edu.hcmaf.apigamestore.user.UserRepository;
import vn.edu.hcmaf.apigamestore.user.UserService;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class OAuth2Filter extends OncePerRequestFilter {
  @Autowired
  private CustomUserDetailService customUserDetailService;
  @Autowired
  private RoleService roleService;
  @Autowired
  private UserRepository userRepository;


  @Value("${spring.security.oauth2.client.provider.google.user-info-uri}")
  private String userInfoUri;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    String jwtToken = getTokenFromRequest(request);
    RestTemplate restTemplate = new RestTemplate();
    if (jwtToken == null || jwtToken.isEmpty()) {
      filterChain.doFilter(request, response);
      return;
    }

    try {
      HttpHeaders userInfoHeaders = new HttpHeaders();
      userInfoHeaders.setBearerAuth(jwtToken);

      HttpEntity<?> userInfoRequest = new HttpEntity<>(userInfoHeaders);

      ResponseEntity<Map> userInfoResponse = restTemplate.exchange(
              userInfoUri, HttpMethod.GET, userInfoRequest, Map.class
      );

      Map<String, Object> userInfo = userInfoResponse.getBody();

      String email = (String) userInfo.get("email");

      UserDetails userDetails = customUserDetailService.getUserByEmail(email);

      if (userDetails == null) {
        UserEntity entity = new UserEntity();
        entity.setEmail(email);
        entity.setPassword("1");
        RoleEntity roleEntity = roleService.getByName("USER");
        if (roleEntity == null) {
          roleEntity = roleService.save("USER");
        }

        UserRoleEntity userRoleEntity = new UserRoleEntity();
        userRoleEntity.setUser(entity);
        userRoleEntity.setRole(roleEntity);

        entity.setUserRoles(Collections.singletonList(userRoleEntity));

        UserEntity saved = userRepository.save(entity);
        userDetails = new User(saved.getEmail(), saved.getPassword(), customUserDetailService.getAuthorities(saved.getActiveRoles()));
      }

      Authentication authentication = new UsernamePasswordAuthenticationToken(
              userDetails,
              null,
              userDetails.getAuthorities()
      );
//      authentication.setAuthenticated(true);
      SecurityContextHolder.getContext().setAuthentication(authentication);

      // Continue with the filter chain
      filterChain.doFilter(request, response);
    } catch (HttpClientErrorException e) {
      filterChain.doFilter(request, response);
    }
  }

  private String getTokenFromRequest(HttpServletRequest request) {
    String bearerToken = request.getHeader("Authorization");
    if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
      return bearerToken.substring(7);
    }
    log.info(bearerToken);
    return null;
  }
}
