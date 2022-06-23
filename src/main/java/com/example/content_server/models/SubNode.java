package com.example.content_server.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SubNode {

    private List<String> id = new ArrayList<>();


    @SuppressWarnings("unchecked")
    //mapping to the main json object and then continue to dive into deeper level
    @JsonProperty("results")
    private void unpackNested(Object results) {
        System.out.println(results);
        if (results.getClass() == ArrayList.class)
            unpackArray((ArrayList<Map<String, Object>>) results);
        else
            unpackObject((Map<String, Object>) results);

    }


    //mapping to the main json object and then continue to dive into deeper level
    private void unpackArray(List<Map<String, Object>> results) {
        if (!results.isEmpty()) {
            for (Map<String, Object> result : results) {
                unpackObject(result);
            }
        }
    }

    @SuppressWarnings("unchecked")
    //mapping to the main json object and then continue to dive into deeper level
    private void unpackObject(Map<String, Object> results) {
        try {
            Map<String, Object> data = (Map<String, Object>) results.get("data");
            Map<String, Object> nicknames = (Map<String, Object>) data.get("nicknames");
            this.id.add((String) nicknames.get("nickname"));

        } catch (NullPointerException e) {
            id = null;
        }
    }

    public List<String> getIds() {
        return id;
    }


}
