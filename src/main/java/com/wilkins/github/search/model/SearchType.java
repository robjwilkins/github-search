package com.wilkins.github.search.model;

import lombok.Getter;

public enum SearchType {
    repositories("application/json"), commits("application/vnd.github.cloak-preview");

    @Getter
    private final String accept;

    SearchType(final String accept) {
        this.accept = accept;
    }
}
