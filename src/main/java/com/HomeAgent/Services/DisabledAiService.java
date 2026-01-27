package com.HomeAgent.Services;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(
        name = "ai.mode",
        havingValue = "DISABLED",
        matchIfMissing = true
)
public class DisabledAiService implements AiService {


    @Override
    public String ask(String prompt) {
        return "🤖 AI assistant is available only in local demo mode.";
    }
}

