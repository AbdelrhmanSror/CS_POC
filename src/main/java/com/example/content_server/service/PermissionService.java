package com.example.content_server.service;

import org.springframework.beans.factory.annotation.Autowired;
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

public class PermissionService {
    private final String secondaryPath = "api/v2/nodes";

    @Autowired
    private RestTemplate restTemplate;

    public void removeOwner(String OTCSTicket, String nodeId) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(OTCS_TICKET, OTCSTicket);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(headers);
        Objects.requireNonNull(restTemplate.exchange(main_path + secondaryPath + "/" + nodeId + "/permissions/owner", HttpMethod.DELETE, httpEntity, String.class));

    }

    public void modifyOwnerGroup(String OTCSTicket, String nodeId, String permissions) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(OTCS_TICKET, OTCSTicket);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> bodyParams = new LinkedMultiValueMap<>();
        bodyParams.add("body", permissions);
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(bodyParams, headers);
        Objects.requireNonNull(restTemplate.exchange(main_path + secondaryPath + "/" + nodeId + "/permissions/group", HttpMethod.PUT, httpEntity, String.class));

    }

    //body = {"permissions":["see","see_contents"],"right_id":15234}
    public void addAssignedAccess(String OTCSTicket, String parentNodeId, String permissions) {
        System.out.println(parentNodeId + "   " + permissions);
        HttpHeaders headers = new HttpHeaders();
        headers.add(OTCS_TICKET, OTCSTicket);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> bodyParams = new LinkedMultiValueMap<>();
        bodyParams.add("body", permissions);
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(bodyParams, headers);
        Objects.requireNonNull(restTemplate.exchange(main_path + secondaryPath + "/" + parentNodeId + "/permissions/custom", HttpMethod.POST, httpEntity, String.class));

    }

    //body = {"permissions":["see","see_contents"],"right_id":15234}
    public void UpdatePublicAccess(String OTCSTicket, String parentNodeId, String permissions) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(OTCS_TICKET, OTCSTicket);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> bodyParams = new LinkedMultiValueMap<>();
        bodyParams.add("body", permissions);
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(bodyParams, headers);
        Objects.requireNonNull(restTemplate.exchange(main_path + secondaryPath + "/" + parentNodeId + "/permissions/public", HttpMethod.PUT, httpEntity, String.class));

    }
}
