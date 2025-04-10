package com.bankaiassistant.model;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.*;

public class User {
    private String email;
    private String password;
    private List<String> topics;

    public User(String email, String password, List<String> topics) {
        this.email = email;
        this.password = password;
        this.topics = topics == null ? new ArrayList<>() : topics;
    }

    // Getters and setters

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<String> getTopics() {
        return topics;
    }

    public void setTopics(List<String> topics) {
        this.topics = topics;
    }

    // Static Methods

    public static List<Map<String, Object>> loadData(String dataFilePath) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(new File(dataFilePath));
            JsonNode usersNode = root.get("users");

            return mapper.convertValue(usersNode, new com.fasterxml.jackson.core.type.TypeReference<List<Map<String, Object>>>() {});
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
            // If file exists - get data
            if (file.exists()) {
                Map<String, Object> data = mapper.readValue(file, new TypeReference<>() {});
                Object usersObj = data.get("users");

                if (usersObj instanceof List<?>) {
                    users = (List<Map<String, Object>>) usersObj;
                }
            }

            // create a new user
            Map<String, Object> newUser = new HashMap<>();
            newUser.put("email", email);
            newUser.put("password", password);
            newUser.put("topics", new ArrayList<>()); // empty topics list

            users.add(newUser); // add to the list

            // jsonify
            Map<String, Object> updatedData = new HashMap<>();
            updatedData.put("users", users);

            // save to the file
            mapper.writerWithDefaultPrettyPrinter().writeValue(file, updatedData);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static boolean isEmailTaken(String email, List<Map<String, Object>> users) {
        return users.stream().anyMatch(user -> user.get("email").equals(email));
    }

    public static boolean authenticate(String email, String password, List<Map<String, Object>> users) {
        return users.stream()
                .anyMatch(user -> user.get("email").equals(email) && user.get("password").equals(password));
    }


    // Not static methods

    public static boolean register(String email, String password, String confirmPassword, List<Map<String, Object>> users, String dataFilePath) {
        if (isEmailTaken(email, users)) {
            // Flash an error or handle accordingly
            return false;
        }

        if (!password.equals(confirmPassword)) {
            // Flash an error or handle accordingly
            return false;
        }

        saveData(email, password, dataFilePath);
        return true;
    }
}