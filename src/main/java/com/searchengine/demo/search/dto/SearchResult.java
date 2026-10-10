package com.searchengine.demo.search.dto;

public record SearchResult(String type, String id, String title, String summary, float score) {
}
