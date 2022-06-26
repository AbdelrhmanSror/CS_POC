package com.example.content_server.service;

import com.example.content_server.models.Node;
import com.example.content_server.models.SubNode;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

import static com.example.content_server.constant.Constants.OTCS_TICKET;
import static com.example.content_server.constant.Constants.main_path;

public class NodeService {

    private final String secondaryPath = "api/v2/nodes";

    @Autowired
    private RestTemplate restTemplate;

    public void createNode(String otcsticket, String nodeType, Integer parent_id, String name) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(OTCS_TICKET, otcsticket);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, Object> bodyParams = new LinkedMultiValueMap<>();
        bodyParams.add("type", nodeType);
        bodyParams.add("parent_id", parent_id);
        bodyParams.add("name", name);
        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(bodyParams, headers);
        restTemplate.postForEntity(main_path + secondaryPath, httpEntity, Node.class);
    }

/*

    public void upload(String token) {
       String FILENAME = "F:\\abdelrhman\\ocr\\output\\sample.xml";
        HttpHeaders headers = new HttpHeaders();
        headers.add(OTCS_TICKET, token);
        MultiValueMap<String, Object> bodyParams = new LinkedMultiValueMap<>();
        bodyParams.add("type", NodeType.DOCUMENT.getNodeTypeId());
        bodyParams.add("parent_id", 2000);
        bodyParams.add("name", "xyz");


        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(bodyParams, headers);
        restTemplate.postForEntity(main_path+"api/2/nodes", httpEntity, Node.class);
        CloseableHttpClient client = HttpClients.createDefault();
        try {
            try {
                // Initialize an authenticated request to POST /api/v1/nodes
                HttpPost request = new HttpPost(main_path+"api/2/nodes");
                request.addHeader(OTCS_TICKET,token );

                // Provide all input parameters as JSON
                JSONObject input = new JSONObject();
                try {
                    input.put("parent_id", 2000);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    input.put("type", 144);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    input.put("name", "xyz");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // Format body and file form fields to multipart/Form-data
                StringBody body = new StringBody(input.toString(),
                        ContentType.TEXT_PLAIN);
                FileBody file = new FileBody(new File(FILENAME));
                org.apache.http.HttpEntity inputEntity = MultipartEntityBuilder
                        .create()
                        .addPart("body", body)
                        .addPart("file", file)
                        .build();
                request.setEntity(inputEntity);

                // Send the request and parse the JSON response
                System.out.println(request.getRequestLine());
                client.execute(request);

            } finally {
                client.close();
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
*/

    public void copyNode(String otcsticket, Integer parent_id, Integer child_id) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(OTCS_TICKET, otcsticket);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, Object> bodyParams = new LinkedMultiValueMap<>();
        bodyParams.add("parent_id", parent_id);
        bodyParams.add("original_id", child_id);
        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(bodyParams, headers);
        restTemplate.postForEntity(main_path + secondaryPath, httpEntity, Node.class);
    }


    public void moveNode(String otcsticket, Integer nodeId, Integer parent_id) {
        ClientHttpRequestFactory requestFactory = new
                HttpComponentsClientHttpRequestFactory(HttpClients.createDefault());
        HttpHeaders headers = new HttpHeaders();
        headers.add(OTCS_TICKET, otcsticket);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("User-Agent", "Microsoft-IIS/10.0");
        headers.add("Expires", "0");
        headers.add("Content-Length", "0");
        headers.add(" Accept-Encoding", " gzip, deflate, br");
        headers.add("Accept", "*/*");
        MultiValueMap<String, Object> bodyParams = new LinkedMultiValueMap<>();
        //bodyParams.add("b", parent_id);
        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(bodyParams, headers);
        bodyParams.add("body", "{ \"parent_id\":" + parent_id + "}");
        restTemplate.setRequestFactory(requestFactory);
        System.out.println("nodeId=" + nodeId + "    " + "parent id=" + parent_id);
        restTemplate.put(main_path + secondaryPath + "/" + nodeId, httpEntity);
        //restTemplate.exchange(main_path + secondaryPath + "/" + Long.valueOf(nodeId), HttpMethod.PUT, httpEntity, Object.class);
    }

    //check if node with name exist under the parent ,will check only level one nodes
    public Node getNodeWithName(String otcsticket, Integer parentNodeId, String nodeName) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(OTCS_TICKET, otcsticket);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(headers);

        return restTemplate.exchange(main_path + secondaryPath + "/" + parentNodeId + "/nodes?where_name=" + nodeName, HttpMethod.GET, httpEntity, Node.class).getBody();
    }

    //check if node with name exist under the parent ,will check only level one nodes
    public Node getNodeWitID(String otcsticket, Integer nodeId) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(OTCS_TICKET, otcsticket);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(headers);
        return restTemplate.exchange(main_path + secondaryPath + "/" + nodeId, HttpMethod.GET, httpEntity, Node.class).getBody();
    }

    //http://ot2016/otcs/cs.exe/api/v2/nodes/813632/content
    //check if node with name exist under the parent ,will check only level one nodes
    public String getXMLNode(String otcsticket, Integer xmlNodeId) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(OTCS_TICKET, otcsticket);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(headers);
        return restTemplate.exchange(main_path + secondaryPath + "/" + xmlNodeId + "/content", HttpMethod.GET, httpEntity, String.class).getBody();
    }

    //check if node with name exist under the parent ,will check only level one nodes
    public SubNode getSubNode(String otcsticket, String parentNodeId) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(OTCS_TICKET, otcsticket);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(headers);
        return restTemplate.exchange(main_path + secondaryPath + "/" + parentNodeId + "/nodes", HttpMethod.GET, httpEntity, SubNode.class).getBody();
    }

    public void renameNode(String otcsticket, Integer nodeId, String newName) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(OTCS_TICKET, otcsticket);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> bodyParams = new LinkedMultiValueMap<>();
        bodyParams.add("body", " { \"name\":\"" + newName + "\" }");
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(bodyParams, headers);
        Objects.requireNonNull(restTemplate.exchange(main_path + "api/v1/nodes" + "/" + nodeId, HttpMethod.PUT, httpEntity, String.class).getBody());
    }

    public ByteArrayResource downloadNode(String otcsticket, Integer docID) throws HttpServerErrorException {
        ClientHttpRequestFactory requestFactory = new
                HttpComponentsClientHttpRequestFactory(HttpClients.createDefault());

        HttpHeaders header = new HttpHeaders();
        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
        header.add("Pragma", "no-cache");
        header.add("User-Agent", "Microsoft-IIS/10.0");
        header.add("Expires", "0");
        header.add(OTCS_TICKET, otcsticket);
        header.add("Content-Length", "0");
        header.add(" Accept-Encoding", " gzip, deflate, br");
        header.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        header.add("Accept", "*/*");
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(header);

        System.out.println(docID + "  doc id ");
        restTemplate.setRequestFactory(requestFactory);
        return restTemplate.exchange(main_path + secondaryPath + "/" + docID + "/content?suppress_response_codes", HttpMethod.GET, httpEntity, ByteArrayResource.class).getBody();
    }
}
