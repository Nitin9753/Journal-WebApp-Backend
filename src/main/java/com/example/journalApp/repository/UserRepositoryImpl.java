package com.example.journalApp.repository;

import com.example.journalApp.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

public class UserRepositoryImpl {
    @Autowired
    private MongoTemplate mt;
    public List<User> findAll(){
        Query query=new Query();
        query.fields()
                .include("id")
                .include("name")
                .include("username")
                .include("roles")
                .include("blogs._id");

        return mt.find(query, User.class);
    }

    public User getBlogs(String username) {
        Query query=new Query();
        query.addCriteria(Criteria.where("username").is(username));
        query.fields().include("blogs");
        return mt.findOne(query, User.class);
    }
}
