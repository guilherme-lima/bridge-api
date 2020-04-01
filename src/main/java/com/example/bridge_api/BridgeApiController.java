package com.example.bridge_api;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;

/**
 * Created by Guilherme Lima on 30/01/20.
 * https://github.com/guilherme-lima
 */
@RestController
@RequestMapping("/")
public class BridgeApiController {

    @GetMapping("redirect")
    public ResponseEntity<String> redirectRequest(@RequestParam(value = "url", required = true) final String url) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();

        String userAndPass = url.substring(url.indexOf("://") + 3, url.indexOf("@"));
        String encryptedCredential = Base64.getEncoder().encodeToString(userAndPass.getBytes());
        String uri = "http://" + url.substring(url.indexOf("@") + 1);
        headers.setBasicAuth(encryptedCredential);

        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        try {
            return restTemplate.exchange(uri, HttpMethod.GET, httpEntity, String.class);
        } catch (HttpClientErrorException ex) {
            return ResponseEntity.status(ex.getStatusCode()).body(ex.getResponseBodyAsString());
        }
    }
}