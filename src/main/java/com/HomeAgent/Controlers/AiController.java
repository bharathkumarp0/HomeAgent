package com.HomeAgent.Controlers;

import com.HomeAgent.DTO.ChatRequest;
import com.HomeAgent.Model.User;
import com.HomeAgent.security.SecurityUtil;
import com.HomeAgent.Services.AiOrchestratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
public class AiController {

    private final AiOrchestratorService orchestrator;

    // ✅ FIXED: Inject BOTH in constructor
    private final SecurityUtil securityUtil;

    // 🔥 PROPER CONSTRUCTOR INJECTION
    public AiController(AiOrchestratorService orchestrator, SecurityUtil securityUtil) {
        this.orchestrator = orchestrator;
        this.securityUtil = securityUtil;  // ✅ Now accessible everywhere
    }

    @PostMapping("/chat")
    public ResponseEntity<String> chat(@RequestBody ChatRequest request) {
        try {
            String message = request.getMessage().trim().toLowerCase();

            if (message.isEmpty()
                    || message.equals("hi")
                    || message.equals("hello")
                    || message.equals("hey")) {

                return ResponseEntity.ok("""
        Hello 👋 I'm your SafeGhar AI Assistant.

        You can ask me about:
        • Reminders
        • Inventory
        • Documents
        • Usage tips
        """);
            }

            // ✅ FIXED: Use INSTANCE method (no static!)
            User currentUser = securityUtil.getCurrentUser();  // ← WORKS NOW
            int userId = currentUser.getUserId();

            String response = orchestrator.handle(message, (long) userId);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok("⚠️ Sorry, I'm having trouble right now. Try again!");
        }
    }

    @GetMapping("/reminders")
    public ResponseEntity<String> getReminders() {
        try {
            User user = securityUtil.getCurrentUser();  // ✅ FIXED
            String response = orchestrator.buildReminderResponse((long) user.getUserId());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.ok("📭 No reminders found.");
        }
    }

    @GetMapping("/documents")
    public ResponseEntity<String> getDocuments() {
        try {
            User user = securityUtil.getCurrentUser();  // ✅ FIXED
            String response = orchestrator.buildDocumentResponse((long) user.getUserId());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.ok("📂 No documents found.");
        }
    }

    @GetMapping("/inventory")
    public ResponseEntity<String> getInventory() {
        try {
            User user = securityUtil.getCurrentUser();  // ✅ FIXED
            String response = orchestrator.buildInventoryResponse((long) user.getUserId());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.ok("📦 No inventory items found.");
        }
    }
}
