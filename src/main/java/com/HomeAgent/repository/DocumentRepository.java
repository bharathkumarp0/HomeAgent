package com.HomeAgent.repository;

import com.HomeAgent.Model.Documents;
import com.HomeAgent.Model.Inventory;
import com.HomeAgent.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository

public interface DocumentRepository extends JpaRepository<Documents,Integer> {
    List<Documents> findByUser(User user);
    long countByUser(User user);

    List<Documents> findTop5ByUserOrderByDocumentIdDesc(User user);



    List<Documents> findByUserUserId(Long userId);
    // DocumentRepository.java



}
