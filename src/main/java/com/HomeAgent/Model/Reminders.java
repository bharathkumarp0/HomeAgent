package com.HomeAgent.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "reminders")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Reminders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    @Column(name = "title")
    private String title;               // ✅ camelCase

    @Column(name = "reminder_date")
    private LocalDate reminderDate;     // ✅ camelCase

    @Column(name = "task_type")
    private String taskType;


    public void setTitle(String title) {
        this.title = title;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public void setReminderDate(LocalDate reminderDate) {
        this.reminderDate = reminderDate;
    }

    public void setUser(User user) {
        this.user = user;
    }
// ✅ camelCase
}
