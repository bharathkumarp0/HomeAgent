package com.HomeAgent.DTO;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ReminderDTO {
    private String title;
    private String taskType;
    private LocalDate reminderDate;

    // optional email if needed
    private String userEmail;

    // Getters and Setters
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getTaskType() { return taskType; }
    public void setTaskType(String taskType) { this.taskType = taskType; }

    public LocalDate getReminderDate() { return reminderDate; }
    public void setReminderDate(LocalDate reminderDate) { this.reminderDate = reminderDate; }

    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }
}
