package com.HomeAgent.DTO;

import lombok.Data;

@Data
public class AIResponse {
    private String reply;

    public AIResponse(String reply) {
        this.reply = reply;
    }

    // getter
}
