package com.example.blogApp.controller;

import com.example.blogApp.entity.User;
import com.example.blogApp.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserService userService;
    @GetMapping("/all-users")
    public ResponseEntity<?> getUsers(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username=auth.getName();
        if(username==null) return new ResponseEntity<>("Username is not valid" , HttpStatus.BAD_REQUEST);
        try{
            List<User> users=userService.getUsers(username);
            return new ResponseEntity<>(users, HttpStatus.OK);
        }catch (Exception e){
            log.error("Error found at the getUsers at UserController "+e);
            return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST);
        }
    }

}
