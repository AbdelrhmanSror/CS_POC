package com.example.content_server.service;

import com.example.content_server.models.WorkFlow;
import com.example.content_server.models.WorkFlowAttachment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import static com.example.content_server.constant.Constants.*;

public class WorkflowService {
    private final String secondaryPath = "api/v2/processes";

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private AuthenticationService authenticationService;

    // function responsible for get the content server token that will allow us to access content server and do whatever we want.
    public Integer initiate(Integer workflowId) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(OTCS_TICKET, authenticationService.getToken(ADMIN_USER_NAME, ADMIN_PASSWORD));
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> bodyParams = new LinkedMultiValueMap<>();
        bodyParams.add("body", "{\n" +
                "    \"definition\": {\n" +
                "        \"workflow_id\":" + workflowId +
                "    }\n" +
                "}");

        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(bodyParams, headers);
        return restTemplate.postForEntity(main_path + secondaryPath, httpEntity, WorkFlow.class).getBody().getProcessId();
    }

    // function responsible for get the content server token that will allow us to access content server and do whatever we want.
    public Integer getWorkFlowAttachmentFolderId(Integer processIDWorkFlowId) {
        HttpHeaders headers = new HttpHeaders();
        String token = authenticationService.getToken(ABE_BRANCH_MAKER_USER_NAME, ABE_BRANCH_MAKER_PASSWORD);
        headers.add(OTCS_TICKET, token);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        ResponseEntity<WorkFlowAttachment> entity = restTemplate.exchange(
                main_path + "api/v1/forms/processes/tasks/update?process_id=" + processIDWorkFlowId + "&subprocess_id=" + processIDWorkFlowId + "&task_id=1", HttpMethod.GET, new HttpEntity(headers),
                WorkFlowAttachment.class);
        System.out.println(entity);

        System.out.println("response entity " + entity.getBody().getId());
        System.out.println("processsFolder====" + processIDWorkFlowId);

        return entity.getBody().getId();

    }
}


