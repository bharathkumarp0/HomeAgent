package com.HomeAgent.repository;

import com.HomeAgent.Model.Inventory;
import com.HomeAgent.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory,Integer> {
    List<Inventory> findByUser(User user);

    long countByUser(User user);

    List<Inventory> findTop5ByUserOrderByIdDesc(User user);



    List<Inventory> findByUserAndExpiryDateBefore(User user, LocalDate date);
    List<Inventory> findByUserUserId(Long userId);


}
