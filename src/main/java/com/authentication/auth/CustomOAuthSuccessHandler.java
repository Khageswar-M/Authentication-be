package com.authentication.auth;

import com.authentication.auth.model.OAutheUser;
import com.authentication.auth.service.OAuthUserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.util.Map;


public class CustomOAuthSuccessHandler implements AuthenticationSuccessHandler {

    @Value("${FRONT_END_URL}")
    private String frontendUrl;

    private final OAuthUserService userService;

    public CustomOAuthSuccessHandler(OAuthUserService userService) {
        this.userService = userService;
    }

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException, ServletException {

        DefaultOAuth2User oauthUser = (DefaultOAuth2User) authentication.getPrincipal();
        Map<String, Object> attributes = oauthUser.getAttributes();

        String provider;
        String providerId;
        String name;
        String email = null;
        String picture = null;

        // ⭐ GOOGLE LOGIN ⭐
        if (attributes.containsKey("sub")) {
            provider = "GOOGLE";
            providerId = attributes.get("sub").toString();
            name = (String) attributes.get("name");
            email = (String) attributes.get("email");
            picture = (String) attributes.get("picture");
        }

        // ⭐ GITHUB LOGIN ⭐
        else {
            provider = "GITHUB";
            providerId = attributes.get("id").toString();

            // GitHub user display name or fallback to login
            name = attributes.get("name") != null
                    ? attributes.get("name").toString()
                    : attributes.get("login").toString();

            // GitHub often returns null email -> fallback to login
            String githubLogin = attributes.get("login").toString();
            email = (String) attributes.get("email");

            // ✅ If GitHub didn't provide email, use login as pseudo-email
            if (email == null || email.isEmpty()) {
                email = githubLogin;
                // optional suffix helps distinguish source
            }

            picture = (String) attributes.get("avatar_url");
        }

        // Save or update user into DB
        OAutheUser savedUser = userService.processOAuthLogin(
                provider, providerId, name, email, picture
        );

        System.out.println("✔ User saved to DB: " + savedUser.getEmail());

        // Redirect to frontend
        response.sendRedirect("https://authentication-fe-sigma.vercel.app/logged-users");
    }
}
