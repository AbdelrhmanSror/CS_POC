package com.example.content_server.constant;

public enum NodeType {
    FOLDER("0"),
    SHORTCUT("1"),
    WORKFLOW_MAP("128"),
    CATEGORY("131"),
    CATEGORY_FOLDER("132"),
    DOCUMENT("144"),
    TEXT_DOCUMENT("145");
    private final String nodeTypeId;

    NodeType(String nodeTypeId) {
        this.nodeTypeId = nodeTypeId;

    }

    public String getNodeTypeId() {
        return nodeTypeId;
    }
}
