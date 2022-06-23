package com.example.content_server.service;

import com.example.content_server.constant.PocCategory;
import com.example.content_server.models.poc.WorkFlowPocAttribute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import static com.example.content_server.constant.Constants.OTCS_TICKET;
import static com.example.content_server.constant.Constants.main_path;

public class CategoryService {
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private PermissionService permissionService;
    @Autowired
    private NodeService nodeService;
    @Autowired
    private RestTemplate restTemplate;


    public void applyPocCategory(String otcsticket, String nodeId, WorkFlowPocAttribute workFlowPocAttribute) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(OTCS_TICKET, otcsticket);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, Object> bodyParams = new LinkedMultiValueMap<>();
       /* "811201_10": "10000",
                "811201_11": "notes",
                "811201_12": "new",
                "811201_2": "hjfhjf",
                "811201_3": "554564",
                "811201_4": "2022-06-12",
                "811201_5": "cairo",
                "811201_6": "branch",
                "811201_7": "2022-06-13",
                "811201_8": "5545454",
                "811201_9": "500000"*/
        bodyParams.add("category_id", Integer.valueOf(PocCategory.POC.getCategoryId()));
        bodyParams.add(PocCategory.POC.getCategoryId() + "_2", workFlowPocAttribute.getCustomerName());
        bodyParams.add(PocCategory.POC.getCategoryId() + "_3", workFlowPocAttribute.getIdNumber());
        bodyParams.add(PocCategory.POC.getCategoryId() + "_4", workFlowPocAttribute.getBirthDate());
        bodyParams.add(PocCategory.POC.getCategoryId() + "_5", workFlowPocAttribute.getResidence());
        bodyParams.add(PocCategory.POC.getCategoryId() + "_6", workFlowPocAttribute.getAccountCreationBranch());
        bodyParams.add(PocCategory.POC.getCategoryId() + "_7", workFlowPocAttribute.getRequestReceivedDate());
        bodyParams.add(PocCategory.POC.getCategoryId() + "_8", workFlowPocAttribute.getOrderSerialNumber());
        bodyParams.add(PocCategory.POC.getCategoryId() + "_9", workFlowPocAttribute.getCustomerAnnualIncome());
        bodyParams.add(PocCategory.POC.getCategoryId() + "_10", workFlowPocAttribute.getApproximateMonthlyDeposit());
        bodyParams.add(PocCategory.POC.getCategoryId() + "_11", workFlowPocAttribute.getNotes());
        bodyParams.add(PocCategory.POC.getCategoryId() + "_12", workFlowPocAttribute.getStatus());


        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(bodyParams, headers);
        System.out.println(workFlowPocAttribute);

        //applying category to a node
        restTemplate.exchange(main_path + "api/v2/nodes/" + nodeId + "/categories", HttpMethod.POST, httpEntity, String.class);

    }


    public void updateCategory(String otcsticket, String nodeId, WorkFlowPocAttribute workFlowPocAttribute) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(OTCS_TICKET, otcsticket);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, Object> bodyParams = new LinkedMultiValueMap<>();
        bodyParams.add(PocCategory.POC.getCategoryId() + "_2", workFlowPocAttribute.getCustomerName());
        bodyParams.add(PocCategory.POC.getCategoryId() + "_3", workFlowPocAttribute.getIdNumber());
        bodyParams.add(PocCategory.POC.getCategoryId() + "_4", workFlowPocAttribute.getBirthDate());
        bodyParams.add(PocCategory.POC.getCategoryId() + "_5", workFlowPocAttribute.getResidence());
        bodyParams.add(PocCategory.POC.getCategoryId() + "_6", workFlowPocAttribute.getAccountCreationBranch());
        bodyParams.add(PocCategory.POC.getCategoryId() + "_7", workFlowPocAttribute.getRequestReceivedDate());
        bodyParams.add(PocCategory.POC.getCategoryId() + "_8", workFlowPocAttribute.getOrderSerialNumber());
        bodyParams.add(PocCategory.POC.getCategoryId() + "_9", workFlowPocAttribute.getCustomerAnnualIncome());
        bodyParams.add(PocCategory.POC.getCategoryId() + "_10", workFlowPocAttribute.getApproximateMonthlyDeposit());
        bodyParams.add(PocCategory.POC.getCategoryId() + "_11", workFlowPocAttribute.getNotes());
        bodyParams.add(PocCategory.POC.getCategoryId() + "_12", workFlowPocAttribute.getStatus());


        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(bodyParams, headers);
        //applying category to a node
        restTemplate.exchange(main_path + "api/v2/nodes/" + nodeId + "/categories/" + Integer.valueOf(PocCategory.POC.getCategoryId()), HttpMethod.PUT, httpEntity, String.class);

    }

}
