package com.wilkins.github.search.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wilkins.github.search.model.SearchType;
import com.wilkins.github.search.properties.SearchProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/search")
@Slf4j
public class SearchController {

    private static final String PARAMETERS = "parameters";
    private final RestTemplate restTemplate;
    private final SearchProperties searchProperties;
    private final ObjectMapper objectMapper;

    public SearchController(RestTemplateBuilder restTemplateBuilder, SearchProperties searchProperties, ObjectMapper objectMapper) {
        this.restTemplate = restTemplateBuilder.build();
        this.searchProperties = searchProperties;
        this.objectMapper = objectMapper;
    }

    @GetMapping
    public String getSearch(String searchText, SearchType searchType, Map<String, Object> model) throws IOException {
        String githubSearchUrl = String.format(searchProperties.getBaseUrl(), searchType, searchText);
        log.info("searchText: {}, searchType: {}, githubSearchIUrl: {}, accept: {}", searchText, searchType, githubSearchUrl, searchType.getAccept());

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCEPT, searchType.getAccept());
        HttpEntity<String> entity = new HttpEntity<>(PARAMETERS, headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(githubSearchUrl, HttpMethod.GET, entity, String.class);
        String body = responseEntity.getBody();
        Map<String, ?> results = objectMapper.readValue(body, new TypeReference<Map<String, ?>>() {});
        log.info("Found {} results", ((List)results.get("items")).size());
        model.put(searchType.name(), results);
        return "index";
    }
}
