package com.searchengine.demo.search.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "visits")
public record VisitSearchDocument(@Id String id,
                                  @Field(type = FieldType.Text) String ownerName,
                                  @Field(type = FieldType.Text) String petName,
                                  @Field(type = FieldType.Text) String reason,
                                  @Field(type = FieldType.Text) String notes,
                                  @Field(type = FieldType.Keyword) String scheduledAt,
                                  @Field(type = FieldType.Keyword) String status,
                                  @Field(type = FieldType.Boolean) boolean active) {
}
