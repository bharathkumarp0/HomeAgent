package com.HomeAgent.DTO;

import lombok.*;

@Data
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChatRequest {
    private String message;
    private int userId;

    // getters & setters
}
