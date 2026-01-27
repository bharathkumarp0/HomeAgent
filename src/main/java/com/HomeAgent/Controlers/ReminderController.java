package com.HomeAgent.Controlers;

import com.HomeAgent.DTO.ReminderDTO;
import com.HomeAgent.Model.Reminders;
import com.HomeAgent.Model.User;
import com.HomeAgent.Services.RemindersService;
import com.HomeAgent.Services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
@Controller
@RequestMapping("/reminders")
public class ReminderController {

    private final RemindersService remindersService;
    private final UserService userService;

    public ReminderController(RemindersService remindersService,
                              UserService userService) {
        this.remindersService = remindersService;
        this.userService = userService;
    }

    // 🔹 SAFE logged-in user
    private User getLoggedInUser(Authentication authentication) {

        if (authentication == null) {
            throw new RuntimeException("No authenticated user");
        }

        Object principal = authentication.getPrincipal();
        String email;
        String name = "User";
        String provider = "LOCAL";

        if (principal instanceof OAuth2User oauthUser) {
            email = oauthUser.getAttribute("email");
            name = oauthUser.getAttribute("name");
            provider = "GOOGLE";
        } else {
            email = authentication.getName();
        }

        return userService.getOrCreateUserByEmail(email, name, provider);
    }

    @GetMapping
    public String remindersPage(Model model, Authentication authentication) {
        User user = getLoggedInUser(authentication);

        model.addAttribute("reminderList",
                remindersService.getRemindersByUser(user));
        model.addAttribute("reminderDTO", new ReminderDTO());

        return "Reminders";
    }

    @PostMapping("/add")
    public String addReminder(@ModelAttribute ReminderDTO dto,
                              Authentication authentication) {

        User user = getLoggedInUser(authentication);

        Reminders reminder = new Reminders();
        reminder.setTitle(dto.getTitle());
        reminder.setTaskType(dto.getTaskType());
        reminder.setReminderDate(dto.getReminderDate());
        reminder.setUser(user);

        remindersService.saveReminder(reminder);
        return "redirect:/reminders";
    }

    @PostMapping("/delete")
    public String deleteReminder(@RequestParam int id) {
        remindersService.deleteReminder(id);
        return "redirect:/reminders";
    }
}
