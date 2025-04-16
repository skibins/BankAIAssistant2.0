package com.bankaiassistant.model;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class User {
    private String email;
    private String password;
    private List<Map<String, String>> topics;

    public User(String email, String password, List<Map<String, String>> topics) {
        this.email = email;
        this.password = password;
        this.topics = topics == null ? new ArrayList<>() : topics;
    }

    // Static methods
    public static List<Map<String, Object>> loadData(String dataFilePath) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(new File(dataFilePath));
            JsonNode usersNode = root.get("users");

            return mapper.convertValue(usersNode, new TypeReference<>() {});
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public static void saveData(String email, String password, String dataFilePath) {
        ObjectMapper mapper = new ObjectMapper();
        File file = new File(dataFilePath);
        List<Map<String, Object>> users = new ArrayList<>();

        try {
            if (file.exists()) {
                Map<String, Object> data = mapper.readValue(file, new TypeReference<>() {});
                Object usersObj = data.get("users");

                if (usersObj instanceof List<?>) {
                    users = (List<Map<String, Object>>) usersObj;
                }
            }

            // Default topics
            List<Map<String, String>> defaultTopics = new ArrayList<>();
            defaultTopics.add(Map.of(
                    "title", "Lost Credit Card",
                    "description", "Immediately block your card if lost or stolen. Request replacement online."
            ));
            defaultTopics.add(Map.of(
                    "title", "Forgot Password",
                    "description", "Reset your online banking password securely using SMS verification."
            ));
            defaultTopics.add(Map.of(
                    "title", "Fraud Alert",
                    "description", "Learn how to recognize and report suspicious transactions in your account."
            ));

            // New user
            Map<String, Object> newUser = new HashMap<>();
            newUser.put("email", email);
            newUser.put("password", password);
            newUser.put("topics", defaultTopics);

            users.add(newUser);

            // save to the file
            Map<String, Object> updatedData = new HashMap<>();
            updatedData.put("users", users);
            mapper.writerWithDefaultPrettyPrinter().writeValue(file, updatedData);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean isEmailTaken(String email, List<Map<String, Object>> users) {
        return users.stream().anyMatch(user -> user.get("email").equals(email));
    }

    public static boolean authenticate(String email, String password, List<Map<String, Object>> users) {
        return users.stream().anyMatch(user ->
                user.get("email").equals(email) && user.get("password").equals(password)
        );
    }

    public static boolean register(String email, String password, String confirmPassword,
                                   List<Map<String, Object>> users, String dataFilePath) {
        if (isEmailTaken(email, users)) return false;
        if (!password.equals(confirmPassword)) return false;

        saveData(email, password, dataFilePath);
        return true;
    }
}
