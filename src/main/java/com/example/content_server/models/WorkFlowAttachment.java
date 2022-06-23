package com.example.content_server.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.Map;

public class WorkFlowAttachment {

    private String id = null;

    @SuppressWarnings("unchecked")
    //mapping to the main json object and then continue to dive into deeper level
    @JsonProperty("data")
    private void unpackObject(Map<String, Object> data) {
        try {
            Map<String, Object> data_package = ((ArrayList<Map<String, Object>>) data.get("data_packages")).get(0);
            Map<String, Object> properties = (Map<String, Object>) data_package.get("data");
            this.id = (String.valueOf(((int) properties.get("attachment_folder_id"))));
            System.out.println("id  " + id);

        } catch (NullPointerException e) {
            id = null;
        }
    }

    public String getId() {
        return id;
    }
}
