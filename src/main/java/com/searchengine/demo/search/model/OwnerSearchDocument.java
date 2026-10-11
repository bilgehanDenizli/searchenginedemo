package com.searchengine.demo.search.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "owners")
public record OwnerSearchDocument(@Id String id,
                                  @Field(type = FieldType.Text) String fullName,
                                  @Field(type = FieldType.Boolean) boolean active) {

}

