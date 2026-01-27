package com.HomeAgent.Services;

import com.HomeAgent.Ai.AiAction;
import com.HomeAgent.Model.Documents;
import com.HomeAgent.Model.Inventory;
import com.HomeAgent.Model.Reminders;
import com.HomeAgent.security.SecurityUtil;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AiOrchestratorService {

    private final RemindersService remindersService;
    private final DocumentService documentService;
    private final InventoryService inventoryService;
    private final OllamaAiService aiService;
    private final SecurityUtil securityUtil;  // ✅ FIXED: Removed extra spaces

    public AiOrchestratorService(
            RemindersService remindersService,
            DocumentService documentService,
            InventoryService inventoryService,
            OllamaAiService aiService,
            SecurityUtil securityUtil) {  // ✅ Proper constructor param

        this.remindersService = remindersService;
        this.documentService = documentService;
        this.inventoryService = inventoryService;
        this.aiService = aiService;
        this.securityUtil = securityUtil;  // ✅ Now properly injected
    }

    // 🔥 MAIN ENTRY POINT - Handles ALL AI queries
    public String handle(String question, Long userId) {
        String userData = getUserData(userId);
        return aiService.ask(question, userData);
    }

    // ✅ FETCH REAL DATABASE DATA
    private String getUserData(Long userId) {
        StringBuilder data = new StringBuilder();

        List<Reminders> reminders = remindersService.getUpcomingRemindersForUser(userId);
        List<Inventory> inventory = inventoryService.getItemsByUserId(userId);
        List<Documents> documents = documentService.getDocumentsByUserId(userId);

        if (!reminders.isEmpty()) {
            data.append("REMINDERS:\n");
            reminders.forEach(r -> data.append("- ")
                    .append(r.getTitle()).append(" | ")
                    .append(r.getReminderDate()).append(" | ")
                    .append(r.getTaskType()).append("\n"));
        }

        if (!inventory.isEmpty()) {
            data.append("INVENTORY:\n");
            inventory.forEach(i -> data.append("- ")
                    .append(i.getItemName()).append(" | Category: ")
                    .append(i.getCategory()).append(" | Expiry: ")
                    .append(i.getExpiryDate()).append("\n"));
        }

        if (!documents.isEmpty()) {
            data.append("DOCUMENTS:\n");
            documents.forEach(d -> data.append("- ")
                    .append(d.getDocumentName()).append(" | Location: ")
                    .append(d.getPhysicalLocation()).append("\n"));
        }

        return data.length() > 0 ? data.toString() : "NO DATA FOUND";
    }

    // ✅ UPDATED METHODS - Use userId parameter (matches AiController)
    public String buildReminderResponse(Long userId) {
        List<Reminders> reminders = remindersService.getUpcomingRemindersForUser(userId);
        if (reminders.isEmpty()) {
            return "📭 No upcoming reminders found.";
        }
        String data = reminders.stream()
                .map(r -> "- " + r.getTitle() + " | " + r.getReminderDate())
                .collect(Collectors.joining("\n"));
        return "Your reminders:\n" + data;
    }

    public String buildDocumentResponse(Long userId) {
        List<Documents> documents = documentService.getDocumentsByUserId(userId);
        if (documents.isEmpty()) {
            return "📂 No documents found.";
        }
        String data = documents.stream()
                .map(d -> "- " + d.getDocumentName() + " | " + d.getPhysicalLocation())
                .collect(Collectors.joining("\n"));
        return "Your documents:\n" + data;
    }

    public String buildInventoryResponse(Long userId) {
        List<Inventory> items = inventoryService.getItemsByUserId(userId);
        if (items.isEmpty()) {
            return "📦 No inventory items found.";
        }
        String data = items.stream()
                .map(i -> "- " + i.getItemName() + " | " + i.getCategory())
                .collect(Collectors.joining("\n"));
        return "Your inventory:\n" + data;
    }
}
