package com.bridgelabz.services;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import java.util.Map;

@Service
public class UrlShortenerService {

    private final RestTemplate restTemplate = new RestTemplate();
    private static final String SHORTEN_API = "https://cleanuri.com/api/v1/shorten";

    public String shortenUrl(String longUrl) {
        Map<String, String> requestBody = Map.of("url", longUrl);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(SHORTEN_API, entity, Map.class);
        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            return (String) response.getBody().get("result_url");
        }
        return longUrl; // fallback to original
    }
}

