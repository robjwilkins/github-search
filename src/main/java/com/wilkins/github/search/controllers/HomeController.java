package com.wilkins.github.search.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
@RequestMapping("/")
public class HomeController {
    @GetMapping
    public String home( Map<String, Object> model) {
        model.put("searchText", "");
        model.put("searchType", "");
        return "index";
    }
}
