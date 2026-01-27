package com.HomeAgent.Controlers;

import com.HomeAgent.DTO.InventoryDTO;
import com.HomeAgent.Model.Inventory;
import com.HomeAgent.Model.User;
import com.HomeAgent.Services.InventoryService;
import com.HomeAgent.Services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/inventorys")
public class InventoryController {

    private final InventoryService inventoryService;
    private final UserService userService;

    public InventoryController(InventoryService inventoryService,
                               UserService userService) {
        this.inventoryService = inventoryService;
        this.userService = userService;
    }

    /* 🔹 GET LOGGED-IN USER */
    private User getLoggedInUser(Authentication authentication) {

        if (authentication == null) {
            throw new RuntimeException("No authenticated user found");
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
            throw new RuntimeException("Unsupported authentication type");
        }

        return userService.getOrCreateUserByEmail(email, name, provider);
    }

    /* 📦 INVENTORY PAGE */
    @GetMapping
    public String inventoryPage(Model model, Authentication authentication) {

        User user = getLoggedInUser(authentication);

        List<Inventory> inventoryList = inventoryService.getItemsByUser(user);

        model.addAttribute("inventoryList", inventoryList);
        model.addAttribute("inventoryDTO", new InventoryDTO());
        model.addAttribute("userEmail", user.getEmail());

        return "inventory";
    }

    /* ➕ ADD INVENTORY */
    @PostMapping("/add")
    public String addInventory(@ModelAttribute InventoryDTO dto,
                               Authentication authentication) {

        User user = getLoggedInUser(authentication);

        Inventory inventory = new Inventory();
        inventory.setItemName(dto.getItemName());
        inventory.setCategory(dto.getCategory());
        inventory.setPurchaseDate(dto.getPurchaseDate());
        inventory.setExpiryDate(dto.getExpiryDate());
        inventory.setUser(user);

        inventoryService.AddInventory(inventory);

        return "redirect:/inventorys";
    }

    /* ❌ DELETE INVENTORY (ADDED) */
    @PostMapping("/delete")
    public String deleteInventory(@RequestParam("id") int id,
                                  Authentication authentication) {

        User user = getLoggedInUser(authentication);

        Inventory inventory = inventoryService.getById(id);

        // 🔐 Allow delete only if item belongs to logged-in user
        if (inventory.getUser().getUserId() != user.getUserId()) {
            throw new RuntimeException("Unauthorized delete attempt");
        }

        inventoryService.deleteInventory(id);

        return "redirect:/inventorys";
    }

}
