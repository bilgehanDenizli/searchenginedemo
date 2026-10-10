package com.searchengine.demo.search.model;

import jakarta.persistence.Id;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

public record OwnerSearchDocument(@Id String id,
                                  @Field(type = FieldType.Text) String fullName) {

}

