package com.example.content_server.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class WorkFlow {
    private Integer processId = null;

    //mapping to the main json object and then continue to dive into deeper level
    @SuppressWarnings("unchecked")
    @JsonProperty("results")
    private void unpackObject(Map<String, Object> results) {
        try {
            this.processId = (Integer) results.get("process_id");
        } catch (NullPointerException e) {
            processId = null;
        }
    }

    public Integer getProcessId() {
        return processId;
    }
}
