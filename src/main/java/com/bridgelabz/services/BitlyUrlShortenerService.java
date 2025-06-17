package com.bridgelabz.services;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class BitlyUrlShortenerService {



    @Value("${bitly.access.token}")
    private String bitlyAccessToken;

    private final RestTemplate restTemplate;

    public BitlyUrlShortenerService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String shortenUrl(String longUrl) {
        String apiUrl = "https://api-ssl.bitly.com/v4/shorten";


        Map<String, String> requestBody = Map.of("long_url", longUrl);


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(bitlyAccessToken);

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(apiUrl, entity, Map.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                Map responseBody = response.getBody();
                if (responseBody != null && responseBody.containsKey("link")) {
                    return responseBody.get("link").toString();
                }
            }
            return "Bitly shortening failed - no link found";

        } catch (HttpClientErrorException e) {
            return "Bitly error: " + e.getStatusCode() + " - " + e.getResponseBodyAsString();
        }
    }
}