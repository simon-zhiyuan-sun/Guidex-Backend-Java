package com.guidex.system.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * @author kled2
 * @date 2025/5/13
 */
@Component
public class PythonClient {
    @Autowired
    private RestTemplate restTemplate;

    public String analyze(String videoUrl, String category, String standard, String type) {
        Map<String, String> payload = new HashMap<>();
        payload.put("video_url", videoUrl);
        payload.put("category", category);
        payload.put("standard", standard);
        payload.put("type", type);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, String>> request = new HttpEntity<>(payload, headers);
        String api = "http://localhost:5000/analyze";

        ResponseEntity<String> response = restTemplate.postForEntity(api, request, String.class);
        return response.getBody();
    }
}
