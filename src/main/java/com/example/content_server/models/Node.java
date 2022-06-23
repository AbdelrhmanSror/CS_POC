package com.example.content_server.models;

import com.example.content_server.models.poc.WorkFlowPocAttribute;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Node {

    private String id = null;
    private String name = "";
    private WorkFlowPocAttribute workFlowPocAttribute = null;


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
            this.id = (String.valueOf(((int) properties.get("id"))));
            this.name = (String.valueOf(properties.get("name")));

            if (!category.isEmpty()) {
                workFlowPocAttribute = new WorkFlowPocAttribute();
                workFlowPocAttribute.setCustomerName((String) category.get(0).get("811201_2"));
                workFlowPocAttribute.setIdNumber((String) category.get(0).get("811201_3"));
                workFlowPocAttribute.setResidence((String) category.get(0).get("811201_5"));
            }

        } catch (NullPointerException e) {
            id = null;
        }
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public WorkFlowPocAttribute getWorkFlowPocAttribute() {
        return workFlowPocAttribute;
    }

    public Boolean isExist() {
        return id != null;
    }
}
