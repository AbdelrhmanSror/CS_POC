package com.example.content_server.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Node {

    private Integer id = null;
    private String name = "";


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
        if (!results.isEmpty())
            unpackObject(results.get(0));
    }

    @SuppressWarnings("unchecked")
    //mapping to the main json object and then continue to dive into deeper level
    private void unpackObject(Map<String, Object> results) {
        try {
            Map<String, Object> data = (Map<String, Object>) results.get("data");
            List<Map<String, Object>> category = ((ArrayList<Map<String, Object>>) data.get("categories"));
            Map<String, Object> properties = (Map<String, Object>) data.get("properties");
            this.id = (Integer) properties.get("id");
            this.name = (String.valueOf(properties.get("name")));

        } catch (NullPointerException e) {
            id = null;
        }
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Node{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }


}
