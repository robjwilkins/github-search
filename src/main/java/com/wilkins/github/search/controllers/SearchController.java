package com.wilkins.github.search.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wilkins.github.search.model.SearchType;
import com.wilkins.github.search.properties.SearchProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Map;

@RequiredArgsConstructor
@Controller
@RequestMapping("/search")
@Slf4j
public class SearchController {

    private static final String ACCEPT = "accept";
    private static final String PARAMETERS = "parameters";
    private final SearchProperties searchProperties;
    private static final RestTemplate REST_TEMPLATE = new RestTemplate();
    private final ObjectMapper objectMapper;

    @GetMapping
    public String getSearch(String searchText, SearchType searchType, Map<String, Object> model) throws IOException {
        model.put("searchText", searchText);
        model.put("searchType", searchType);
        String githubSearchIUrl = String.format(searchProperties.getBaseUrl(), searchType, searchText);
        log.info("searchText:  {}, searchType: {}, githubSearchIUrl: {}, accept: {}", searchText, searchType, githubSearchIUrl, searchType.getAccept());

        HttpHeaders headers = new HttpHeaders();
        headers.add(ACCEPT, searchType.getAccept());
        HttpEntity<String> entity = new HttpEntity<>(PARAMETERS, headers);
        ResponseEntity<String> responseEntity = REST_TEMPLATE.exchange(githubSearchIUrl, HttpMethod.GET, entity, String.class);
        String body = responseEntity.getBody();
        Map<String, ?> results = objectMapper.readValue(body, new TypeReference<Map<String, ?>>() {});
        log.info("Found {} results", results.size());
        model.put(searchType.name(), results);
        return "index";
    }
}
