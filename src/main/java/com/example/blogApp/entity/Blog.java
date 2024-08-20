package com.example.blogApp.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document("blogs")
@Data
@NoArgsConstructor
public class Blog {
    @Id
    private String id;
    @NonNull
    private String title;
    private String content;
    private LocalDateTime createdAt=LocalDateTime.now();




}