package com.HomeAgent.Controlers;

import com.HomeAgent.Model.Documents;
import com.HomeAgent.Model.User;
import com.HomeAgent.Services.DocumentService;
import com.HomeAgent.Services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
@Controller
@RequestMapping("/document")
public class DocumentController {

    private final DocumentService documentService;
    private final UserService userService;

    public DocumentController(DocumentService documentService,
                              UserService userService) {
        this.documentService = documentService;
        this.userService = userService;
    }

    private User getLoggedInUser(Authentication authentication) {

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
    public String documentsPage(Model model, Authentication authentication) {

        User user = getLoggedInUser(authentication);

        model.addAttribute("documents",
                documentService.getDocumentsByUser(user));
        model.addAttribute("userEmail", user.getEmail());

        return "documents";
    }

    @PostMapping("/add")
    public String addDocument(
            @RequestParam String documentName,
            @RequestParam String physicalLocation,
            Authentication authentication
    ) {

        User user = getLoggedInUser(authentication);

        Documents doc = new Documents();
        doc.setDocumentName(documentName);
        doc.setPhysicalLocation(physicalLocation);
        doc.setUser(user);

        documentService.AddDocument(doc);
        return "redirect:/document";
    }

    @PostMapping("/delete")
    public String deleteDocument(@RequestParam int id,
                                 Authentication authentication) {

        User user = getLoggedInUser(authentication);
        Documents doc = documentService.getById(id);

        // ✅ Ownership check
        if (doc == null || doc.getUser().getUserId() != user.getUserId()) {
            throw new RuntimeException("Unauthorized");
        }

        documentService.delete(id);
        return "redirect:/document";
    }

    @PostMapping("/update")
    public String updateDocument(@RequestParam int id,
                                 @RequestParam String documentName,
                                 @RequestParam String physicalLocation,
                                 Authentication authentication) {

        User user = getLoggedInUser(authentication);

        Documents doc = documentService.getById(id);

        // 🔐 Ownership check
        if (doc == null || doc.getUser().getUserId() != user.getUserId()) {
            throw new RuntimeException("Unauthorized update attempt");
        }

        doc.setDocumentName(documentName);
        doc.setPhysicalLocation(physicalLocation);

        documentService.AddDocument(doc); // save = update

        return "redirect:/document";
    }


}
