package com.HomeAgent.Controlers;

import com.HomeAgent.DTO.UserDTO;
import com.HomeAgent.Model.User;
import com.HomeAgent.Services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/settings")
public class SettingsController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public SettingsController(UserService userService,
                              PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    private User loggedInUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        Object principal = auth.getPrincipal();
        String email;
        String name = "User";
        String provider = "LOCAL";

        if (principal instanceof OAuth2User oauthUser) {
            email = oauthUser.getAttribute("email");
            name = oauthUser.getAttribute("name");
            provider = "GOOGLE";
        } else {
            email = auth.getName();
        }

        return userService.getOrCreateUserByEmail(email, name, provider);
    }

    @GetMapping
    public String settings(Model model) {
        model.addAttribute("user", loggedInUser());
        return "settings";
    }

    @PostMapping("/change-password")
    public String changePassword(
            @RequestParam String currentPassword,
            @RequestParam String newPassword,
            @RequestParam String confirmPassword,
            Model model) {

        User user = loggedInUser();

        // ✅ Google users can't change password
        if ("GOOGLE".equals(user.getProvider())) {
            model.addAttribute("error", "Google users cannot change password");
            model.addAttribute("user", user);
            return "settings";
        }

        // ✅ Check current password
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            model.addAttribute("error", "Current password incorrect");
            model.addAttribute("user", user);
            return "settings";
        }

        // ✅ Check passwords match
        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("error", "Passwords do not match");
            model.addAttribute("user", user);
            return "settings";
        }

        // 🔥 FIXED: UPDATE EXISTING USER, don't create new one
        user.setPassword(passwordEncoder.encode(newPassword));
        userService.updateUserPassword((long) user.getUserId(), newPassword);  // ✅ UPDATE method

        // OR if no updateUserPassword method exists, use this:
        // userService.save(user);  // ✅ Save updated user directly

        model.addAttribute("success", "Password updated successfully!");
        model.addAttribute("user", user);
        return "redirect:/settings";
    }
}
