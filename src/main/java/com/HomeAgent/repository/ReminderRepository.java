package com.HomeAgent.repository;

import com.HomeAgent.Model.Reminders;
import com.HomeAgent.Model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReminderRepository extends JpaRepository<Reminders,Integer> {
    List<Reminders> findByUser(User user);
    long countByUserAndReminderDateAfter(User user, LocalDate date);




    List<Reminders> findTop5ByUserAndReminderDateAfterOrderByReminderDateAsc(
            User user,
            LocalDate date
    );

    @Query("SELECT r FROM Reminders r WHERE r.user.id = :userId AND r.reminderDate > :date ORDER BY r.reminderDate")
    List<Reminders> findByUserIdAndReminderDateAfter(@Param("userId") Long userId, @Param("date") LocalDate date);

}
