package com.bankaiassistant.controller;

import com.bankaiassistant.service.GeminiAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/assistant")
public class AssistantController {

    @Autowired
    private GeminiAPI geminiAPI;

    @GetMapping
    public String assistant(Model model) {
        geminiAPI.clearHistory();
        return "assistant";
    }

    @PostMapping("/get_response")
    @ResponseBody
    public Map<String, String> getResponse(@RequestBody Map<String, String> requestData) {
        String userInput = requestData.get("message");

        if (userInput == null || userInput.isEmpty()) {
            return Map.of("error", "No input provided");
        }

        String aiResponse = geminiAPI.getResponse(userInput);
        return Map.of("response", aiResponse);
    }
}