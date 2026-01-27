package com.HomeAgent.Services;

import com.HomeAgent.Model.Reminders;
import com.HomeAgent.Model.User;
import com.HomeAgent.repository.ReminderRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class RemindersService {

    private final ReminderRepository reminderRepository;

    public RemindersService(ReminderRepository reminderRepository) {
        this.reminderRepository = reminderRepository;
    }

    // Add or Update
    public Reminders saveReminder(Reminders reminder) {
        return reminderRepository.save(reminder);
    }

    public void deleteReminder(int id) {
        reminderRepository.deleteById(id);
    }

    public List<Reminders> getRemindersByUser(User user) {
        return reminderRepository.findByUser(user);
    }

    public long countUpcoming(User user) {
        return reminderRepository.countByUserAndReminderDateAfter(user, LocalDate.now());
    }

    public List<Reminders> getUpcomingReminders(User user, int limit) {
        return reminderRepository
                .findTop5ByUserAndReminderDateAfterOrderByReminderDateAsc(
                        user,
                        LocalDate.now()
                );
    }


    public List<Reminders> getUpcomingRemindersForUser(Long userId) {
        return reminderRepository.findByUserIdAndReminderDateAfter(userId, LocalDate.now());
    }

}
