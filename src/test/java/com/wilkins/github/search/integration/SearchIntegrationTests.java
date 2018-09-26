package com.wilkins.github.search.integration;


import com.wilkins.github.search.controllers.SearchController;
import com.wilkins.github.search.properties.SearchProperties;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.ExpectedCount.once;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringRunner.class)
@RestClientTest(SearchController.class)
@SpringBootTest
public class SearchIntegrationTests {

    private static final String INDEX = "index";
    @Autowired
    private MockRestServiceServer server;
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext webApplicationContext;
    @MockBean
    private SearchProperties searchProperties;

    @Before
    public void setup() {
        server.reset();
        when(searchProperties.getBaseUrl()).thenReturn("http://www.localhost:8001/search/%s?q=%s");
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void htmlIsReturnedWhenGetSearchEndpointCalledWithRepositorySearchType() throws Exception {
        server.expect(once(), requestTo(String.format("http://www.localhost:8001/search/%s?q=%s", "repositories", "foo")))
                .andExpect(method(HttpMethod.GET))
                .andExpect(header("accept", MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andRespond(withSuccess("{\"items\":[]}", MediaType.APPLICATION_JSON));

        mockMvc.perform(get("/search?searchText=foo&searchType=repositories"))
                .andExpect(status().isOk())
                .andExpect(view().name(INDEX));
    }

    @Test
    public void githubSearchCommitsIsCalledWhenGetSearchEndpointCalledWithCommitsSearchType() throws Exception {
        server.expect(once(), requestTo(String.format("http://www.localhost:8001/search/%s?q=%s", "commits", "foo")))
                .andExpect(method(HttpMethod.GET))
                .andExpect(header("accept", "application/vnd.github.cloak-preview"))
                .andRespond(withSuccess("{\"items\":[]}", MediaType.APPLICATION_JSON));

        mockMvc.perform(get("/search?searchText=foo&searchType=commits"))
                .andExpect(status().isOk())
                .andExpect(view().name(INDEX));
    }
}
