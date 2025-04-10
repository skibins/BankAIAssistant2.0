package com.bankaiassistant.service;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class GeminiAPI {

    private final String apiKey;
    private final String model = "gemini-2.0-flash";
    private final String customInstruction;
    private final int HISTORY_LIMIT = 10; // Chat history limit
    private final int INSTRUCTION_RESEND_INTERVAL = 5; // Custom instructions interval

    private List<String> chatHistory;

    private final RestTemplate restTemplate;

    public GeminiAPI(
            @Value("${app.api-key-file}") String apiKeyFilePath,
            @Value("${app.custom-instructions-file}") String customInstructionFilePath
    ) {
        this.apiKey = readFileContent(apiKeyFilePath);
        this.customInstruction = readFileContent(customInstructionFilePath);
        this.chatHistory = new ArrayList<>();
        this.restTemplate = new RestTemplate();
    }

    public String getResponse(String userInput) {
        if (apiKey == null || apiKey.isEmpty()) {
            return "Error: API key is missing.";
        }

        if (chatHistory.size() % INSTRUCTION_RESEND_INTERVAL == 0) {
            userInput = customInstruction + " " + userInput;
        }

        // Add input to the history
        chatHistory.add(userInput);

        // Limit the chat history
        if (chatHistory.size() > HISTORY_LIMIT) {
            chatHistory.remove(0); // delete the oldest message
            chatHistory.remove(0); // delete the oldest ai message
        }

        // Prepare the API request body
        String requestBody = createRequestBody(userInput);

        // Set the headers
        HttpHeaders headers = new HttpHeaders();
        headers.set("x-goog-api-key", apiKey);
        headers.set("Content-Type", "application/json");

        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        try {
            // Send the request
            ResponseEntity<String> response = restTemplate.exchange(
                    "https://generativelanguage.googleapis.com/v1beta/models/" + model + ":generateContent",
                    HttpMethod.POST, entity, String.class);

            // Parse and return the response
            String outputText = parseResponse(response.getBody());

            if (!outputText.isEmpty()) {
                chatHistory.add(outputText);  // Store the model's response
                return outputText;
            } else {
                return "Error: No response from AI.";
            }
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    public void clearHistory() {
        chatHistory.clear();
    }

    private String createRequestBody(String userInput) {
        StringBuilder contentBuilder = new StringBuilder();
        contentBuilder.append("{\n  \"contents\": [\n");

        // Add the chat history
        for (int i = 0; i < chatHistory.size(); i++) {
            String role = (i % 2 == 0) ? "model" : "user"; // Model answers, user asks
            String escapedText = chatHistory.get(i).replace("\"", "\\\"");

            contentBuilder.append("    {\n");
            contentBuilder.append("      \"role\": \"").append(role).append("\",\n");
            contentBuilder.append("      \"parts\": [\n");
            contentBuilder.append("        { \"text\": \"").append(escapedText).append("\" }\n");
            contentBuilder.append("      ]\n");
            contentBuilder.append("    },\n");
        }

        // Add newest user query
        String escapedInput = userInput.replace("\"", "\\\"");
        contentBuilder.append("    {\n");
        contentBuilder.append("      \"role\": \"user\",\n");
        contentBuilder.append("      \"parts\": [\n");
        contentBuilder.append("        { \"text\": \"").append(escapedInput).append("\" }\n");
        contentBuilder.append("      ]\n");
        contentBuilder.append("    }\n");

        contentBuilder.append("  ]\n}");
        return contentBuilder.toString();
    }



    private String parseResponse(String responseBody) {
        Gson gson = new Gson();
        GeminiResponse geminiResponse = gson.fromJson(responseBody, GeminiResponse.class);

        if (geminiResponse != null &&
                geminiResponse.candidates != null &&
                !geminiResponse.candidates.isEmpty()) {

            Candidate candidate = geminiResponse.candidates.get(0);

            if (candidate.content != null &&
                    candidate.content.parts != null &&
                    !candidate.content.parts.isEmpty()) {

                return candidate.content.parts.get(0).text;
            }
        }
        return "";
    }

    private String readFileContent(String filePath) {
        try {
            return new String(java.nio.file.Files.readAllBytes(java.nio.file.Paths.get(filePath)));
        } catch (java.io.IOException e) {
            return null;
        }
    }
}



