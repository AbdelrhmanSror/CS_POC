package com.example.content_server.constant;

public enum PocCategory {
    POC("811201");

    private final String categoryId;

    PocCategory(String categoryId) {
        this.categoryId = categoryId;

    }


    public String getCategoryId() {
        return categoryId;
    }
}
