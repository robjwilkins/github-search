package com.wilkins.github.search.model;

import lombok.Getter;
import org.springframework.http.MediaType;

public enum SearchType {
    repositories(MediaType.APPLICATION_JSON_UTF8_VALUE), commits("application/vnd.github.cloak-preview");

    @Getter
    private final String accept;

    SearchType(final String accept) {
        this.accept = accept;
    }
}
