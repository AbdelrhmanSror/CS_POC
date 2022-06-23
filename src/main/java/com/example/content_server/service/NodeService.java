package com.example.content_server.service;

import com.example.content_server.models.Node;
import com.example.content_server.models.SubNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

import static com.example.content_server.constant.Constants.OTCS_TICKET;
import static com.example.content_server.constant.Constants.main_path;

public class NodeService {

    private final String secondaryPath = "api/v2/nodes";

    @Autowired
    private RestTemplate restTemplate;

    public void createNode(String otcsticket, String nodeType, String parent_id, String name) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(OTCS_TICKET, otcsticket);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> bodyParams = new LinkedMultiValueMap<>();
        bodyParams.add("type", nodeType);
        bodyParams.add("parent_id", parent_id);
        bodyParams.add("name", name);
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(bodyParams, headers);
        restTemplate.postForEntity(main_path + secondaryPath, httpEntity, Node.class);
    }

    public String moveNode(String otcsticket, String nodeId, String parent_id) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(OTCS_TICKET, otcsticket);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> bodyParams = new LinkedMultiValueMap<>();
        bodyParams.add("parent_id", parent_id);
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(bodyParams, headers);
        return Objects.requireNonNull(restTemplate.exchange(main_path + secondaryPath + "/" + nodeId, HttpMethod.PUT, httpEntity, Node.class).getBody()).getId();
    }


    //check if node with name exist under the parent ,will check only level one nodes
    public Node getNodeWithName(String otcsticket, String parentNodeId, String nodeName) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(OTCS_TICKET, otcsticket);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(headers);

        return restTemplate.exchange(main_path + secondaryPath + "/" + parentNodeId + "/nodes?where_name=" + nodeName, HttpMethod.GET, httpEntity, Node.class).getBody();
    }

    //check if node with name exist under the parent ,will check only level one nodes
    public Node getNodeWitID(String otcsticket, String nodeId) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(OTCS_TICKET, otcsticket);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(headers);
        return restTemplate.exchange(main_path + secondaryPath + "/" + nodeId, HttpMethod.GET, httpEntity, Node.class).getBody();
    }

    //http://ot2016/otcs/cs.exe/api/v2/nodes/813632/content
    //check if node with name exist under the parent ,will check only level one nodes
    public String getXMLNode(String otcsticket, String xmlNode) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(OTCS_TICKET, otcsticket);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(headers);
        return restTemplate.exchange(main_path + secondaryPath + "/" + xmlNode + "/content", HttpMethod.GET, httpEntity, String.class).getBody();
    }

    //check if node with name exist under the parent ,will check only level one nodes
    public SubNode getSubNode(String otcsticket, String parentNodeId) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(OTCS_TICKET, otcsticket);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(headers);
        return restTemplate.exchange(main_path + secondaryPath + "/" + parentNodeId + "/nodes", HttpMethod.GET, httpEntity, SubNode.class).getBody();
    }

    public void renameNode(String otcsticket, String nodeId, String newName) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(OTCS_TICKET, otcsticket);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> bodyParams = new LinkedMultiValueMap<>();
        bodyParams.add("body", " { \"name\":\"" + newName + "\" }");
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(bodyParams, headers);
        Objects.requireNonNull(restTemplate.exchange(main_path + "api/v1/nodes" + "/" + nodeId, HttpMethod.PUT, httpEntity, String.class).getBody());
    }

    public ByteArrayResource downloadNode(String otcsticket, Long docID) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(OTCS_TICKET, otcsticket);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(headers);

        return restTemplate.exchange(main_path + secondaryPath + "/" + docID + "/content", HttpMethod.GET, httpEntity, ByteArrayResource.class).getBody();
    }
}
