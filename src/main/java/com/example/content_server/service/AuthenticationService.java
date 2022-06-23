
package com.example.content_server.service;

import com.example.content_server.models.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

import static com.example.content_server.constant.Constants.main_path;

public class AuthenticationService {

    @Autowired
    private RestTemplate restTemplate;

    // function responsible for get the content server token that will allow us to access content server and do whatever we want.
    public String getToken(String userName, String password) {
        //prepare the header to send with request
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        //body to send with request,username and password of user account to access in content server
        MultiValueMap<String, String> bodyParams = new LinkedMultiValueMap<>();
        bodyParams.add("userName", userName);
        bodyParams.add("password", password);
        //combining header and body
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(bodyParams, headers);
        return Objects.requireNonNull(restTemplate.postForEntity(main_path + "api/v1/auth", httpEntity, Token.class).getBody()).getTicket();

    }
}


