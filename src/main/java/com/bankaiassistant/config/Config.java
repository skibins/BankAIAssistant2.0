package com.bankaiassistant.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;

@Component
public class Config {

    private final String secretKey;
    private final String dataFilePath;
    private final String customInstructionsFilePath;
    private final String apiKeyFilePath;

    private String googleGeminiApiKey;
    private String customInstructions;

    // Constructor Injection
    public Config(
            @Value("${app.secret-key}") String secretKey,
            @Value("${app.data-file}") String dataFilePath,
            @Value("${app.custom-instructions-file}") String customInstructionsFilePath,
            @Value("${app.api-key-file}") String apiKeyFilePath) {
        this.secretKey = secretKey;
        this.dataFilePath = dataFilePath;
        this.customInstructionsFilePath = customInstructionsFilePath;
        this.apiKeyFilePath = apiKeyFilePath;

        // Load API key and custom instructions
        this.googleGeminiApiKey = loadApiKey();
        this.customInstructions = loadCustomInstructions();
    }

    public String getSecretKey() {
        return secretKey;
    }

    public String getDataFilePath() {
        return dataFilePath;
    }

    public String getCustomInstructionsFilePath() {
        return customInstructionsFilePath;
    }

    public String getApiKeyFilePath() {
        return apiKeyFilePath;
    }

    public String getGoogleGeminiApiKey() {
        return googleGeminiApiKey;
    }

    public String getCustomInstructions() {
        return customInstructions;
    }

    private String loadApiKey() {
        try {
            return new String(Files.readAllBytes(Paths.get(apiKeyFilePath))).trim();
        } catch (IOException e) {
            System.out.println("Error: API key file not found.");
            return null;
        }
    }

    private String loadCustomInstructions() {
        try {
            return new String(Files.readAllBytes(Paths.get(customInstructionsFilePath)), "UTF-8").trim();
        } catch (IOException e) {
            System.out.println("Error: Custom instructions file not found.");
            return "";
        }
    }
}
