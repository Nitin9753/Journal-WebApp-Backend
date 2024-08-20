package com.example.blogApp.entity;


import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("config_blog_app")
public class Config {
    private String key;
    private String value;
}
