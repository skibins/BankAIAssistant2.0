package com.bankaiassistant.service;

public class Topic {
    private String title;
    private String text;

    public Topic(String title, String text) {
        this.title = title;
        this.text = text;
    }

    public Topic() {}

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}


