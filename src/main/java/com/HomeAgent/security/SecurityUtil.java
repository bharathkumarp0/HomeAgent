package com.HomeAgent.security;

import com.HomeAgent.Model.User;
import com.HomeAgent.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtil {

    @Autowired
    private UserRepository userRepository;

    // 🔥 REMOVED 'static' - NOW INSTANCE METHOD
    public User getCurrentUser() {  // ❌ No more 'static'
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()) {
            throw new RuntimeException("User not authenticated");
        }

        Object principal = auth.getPrincipal();
        String email;

        if (principal instanceof UserDetails userDetails) {
            email = userDetails.getUsername();
        } else if (principal instanceof OAuth2User oAuth2User) {
            email = oAuth2User.getAttribute("email");
            if (email == null) email = oAuth2User.getName();
        } else {
            email = principal.toString();
        }

        String finalEmail = email;
        return userRepository.findByEmail(email)
                .orElseGet(() -> {
                    User newUser = new User();
                    newUser.setEmail(finalEmail);
                    newUser.setName(principal.toString());
                    newUser.setProvider("GOOGLE");
                    newUser.setRole("USER");
                    return userRepository.save(newUser);
                });
    }
}
