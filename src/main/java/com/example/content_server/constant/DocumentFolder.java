package com.example.content_server.constant;

public enum DocumentFolder {
    Approved("805128"),
    Rejected("805129");
    private final String folderId;

    DocumentFolder(String folderId) {
        this.folderId = folderId;

    }

    public String getFolderId() {
        return folderId;
    }
}
