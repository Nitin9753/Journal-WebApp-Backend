package com.example.journalApp.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document("blogs")
@Data
@NoArgsConstructor
public class Journal {
    @Id
    private String id;
    @NonNull
    private String title;
    private String content;
    private String image;
    private LocalDateTime createdAt=LocalDateTime.now();
    private String createdBy;



}