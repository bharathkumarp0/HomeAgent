package com.HomeAgent.Services;

import com.HomeAgent.Model.Inventory;
import com.HomeAgent.Model.User;
import com.HomeAgent.repository.InventoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    public InventoryService(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    public void AddInventory(Inventory inventory) {
        inventoryRepository.save(inventory);
    }

    public List<Inventory> getAllInventorys() {
        return inventoryRepository.findAll();
    }

    public void deleteInventory(int id) {
        inventoryRepository.deleteById(id);
    }

    public Inventory getById(int id) {
        return inventoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inventory not found with id: " + id));
    }

    public List<Inventory> getItemsByUser(User user) {
        return inventoryRepository.findByUser(user);
    }


    public long countByUser(User user) {
        return inventoryRepository.countByUser(user);
    }

    public List<Inventory> getRecentInventory(User user, int limit) {
        // You can customize limit dynamically if repository supports
        return inventoryRepository.findTop5ByUserOrderByIdDesc(user);
    }


    public List<Inventory> getItemsByUserId(Long userId) {
        return inventoryRepository.findByUserUserId(userId);
    }
}
