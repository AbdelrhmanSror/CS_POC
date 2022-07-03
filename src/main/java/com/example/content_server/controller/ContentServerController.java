package com.example.content_server.controller;

import com.example.content_server.service.POCService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ContentServerController {

    @Autowired
    private POCService pocService;

    @PostMapping("/InitiatePocWorkflow")
    public void InitiatePocWorkflow(@RequestBody String json) {

        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonObj = null;
        try {
            jsonObj = mapper.readTree(json.replace('=', ':'));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        assert jsonObj != null;
        pocService.InitiatePocWorkflow(jsonObj.get("docId").asInt(), jsonObj.get("workFlowId").asInt(), jsonObj.get("attachmentFolderId").asInt());
    }


    @PostMapping("/PocApproved")
    public void createNodeAndApplyCategory(@RequestBody String json) {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonObj = null;
        try {
            jsonObj = mapper.readTree(json.replace('=', ':'));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        assert jsonObj != null;
        pocService.saveID(jsonObj.get("docId").asInt(), jsonObj.get("workFlowId").asInt());

    }


    @PostMapping("/PocRejected")
    public void archiveDocument(@RequestBody String json) {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonObj = null;
        try {
            jsonObj = mapper.readTree(json.replace('=', ':'));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        assert jsonObj != null;
        pocService.archiveID(jsonObj.get("docId").asInt(), jsonObj.get("workFlowId").asInt());

    }

}
