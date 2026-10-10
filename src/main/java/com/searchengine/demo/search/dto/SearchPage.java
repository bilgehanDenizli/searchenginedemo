package com.searchengine.demo.search.dto;

import java.util.List;

public record SearchPage(List<SearchResult> results, long totalHits, int page, int size) {
}
