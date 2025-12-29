package com.foodflow.security.handler;

import com.foodflow.security.jwt.JwtService;
import com.foodflow.user.entity.User;
import com.foodflow.user.enums.UserRole;
import com.foodflow.user.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final UserService userService;
    private final JwtService jwtService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken)  authentication;
        DefaultOAuth2User oAuthUser = (DefaultOAuth2User) token.getPrincipal();
        String email = oAuthUser.getAttribute("email");
        User user = userService.getUserByEmail(email).orElse(null);

        if(user == null){
            User newUser = User.builder()
                    .email(email)
                    .name(oAuthUser.getAttribute("name"))
                    .profileImageUrl(oAuthUser.getAttribute("picture"))
                    .roles(Set.of(UserRole.USER))
                    .build();

            user = userService.saveUser(newUser);
        }

        String accessToken = jwtService.generateToken(user);
        log.info("Token: {}",accessToken);
        String frontEndUrl = "http://localhost:8080/home.html?token="+accessToken;
        getRedirectStrategy().sendRedirect(request, response, frontEndUrl);
    }
}
