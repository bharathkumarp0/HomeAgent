package com.HomeAgent.Controlers;

import com.HomeAgent.Model.User;
import com.HomeAgent.Services.DocumentService;
import com.HomeAgent.Services.InventoryService;
import com.HomeAgent.Services.RemindersService;
import com.HomeAgent.Services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    private final InventoryService inventoryService;
    private final DocumentService documentService;
    private final RemindersService remindersService;
    private final UserService userService;

    public DashboardController(
            InventoryService inventoryService,
            DocumentService documentService,
            RemindersService remindersService,
            UserService userService
    ) {
        this.inventoryService = inventoryService;
        this.documentService = documentService;
        this.remindersService = remindersService;
        this.userService = userService;
    }

    @GetMapping
    public String dashboard(Model model, Authentication authentication) {

        if (authentication == null) {
            return "redirect:/login";
        }

        String email;
        String name = "User";
        String provider = "LOCAL";

        Object principal = authentication.getPrincipal();

        if (principal instanceof OAuth2User oauthUser) {
            email = oauthUser.getAttribute("email");
            name = oauthUser.getAttribute("name");
            provider = "GOOGLE";

        } else if (principal instanceof UserDetails userDetails) {
            email = userDetails.getUsername();

        } else {
            return "redirect:/login";
        }

        // ✅ THIS IS THE KEY LINE
        User user = userService.getOrCreateUserByEmail(email, name, provider);

        // 📊 COUNTS
        model.addAttribute("totalItems", inventoryService.countByUser(user));
        model.addAttribute("totalDocuments", documentService.countByUser(user));
        model.addAttribute("upcomingRemindersCount", remindersService.countUpcoming(user));

        // 📋 RECENT DATA
        model.addAttribute("recentInventory", inventoryService.getRecentInventory(user, 5));
        model.addAttribute("recentDocuments", documentService.getRecentDocuments(user, 5));
        model.addAttribute("upcomingReminders", remindersService.getUpcomingReminders(user, 5));

        // 👤 USER INFO
        model.addAttribute("userName", user.getName());
        model.addAttribute("userEmail", user.getEmail());

        return "dashboard";
    }


}
