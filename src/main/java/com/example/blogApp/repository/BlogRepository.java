package com.example.blogApp.repository;

import com.example.blogApp.entity.Blog;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BlogRepository extends MongoRepository<Blog, ObjectId> {

}