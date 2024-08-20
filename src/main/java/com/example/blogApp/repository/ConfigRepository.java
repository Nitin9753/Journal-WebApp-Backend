package com.example.blogApp.repository;

import com.example.blogApp.entity.Config;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ConfigRepository extends MongoRepository<Config, ObjectId> {

}
