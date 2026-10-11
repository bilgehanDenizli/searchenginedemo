package com.searchengine.demo.search.model;

import com.searchengine.demo.model.Species;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDate;

@Document(indexName = "pets")
public record PetSearchDocument(@Id String id,
                                @Field(type = FieldType.Text) String name,
                                @Field(type = FieldType.Keyword) Species species,
                                @Field(type = FieldType.Text) String breed,
                                @Field(type = FieldType.Date) LocalDate birthDate,
                                @Field(type = FieldType.Text) String ownerName,
                                @Field(type = FieldType.Long) Long ownerId,
                                @Field(type = FieldType.Boolean) boolean active) {
}
