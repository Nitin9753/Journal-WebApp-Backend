package com.example.journalApp.controller;

import com.example.journalApp.entity.User;
import com.example.journalApp.repository.UserRepositoryImpl;
import com.example.journalApp.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepositoryImpl userRepository;
    @GetMapping("/all-users")
    public ResponseEntity<?> getUsers(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username=auth.getName();
        if(username==null) return new ResponseEntity<>("Username is not valid" , HttpStatus.BAD_REQUEST);
        try{
            List<User> users=userService.getUsers(username);
            System.out.print("The value of the user[0] is: "+ users.get(0));
//            users.stream().map((x)->x.getBlogs().stream().map(Blog::getId));
            return new ResponseEntity<>(users, HttpStatus.OK);
        }catch (Exception e){
            log.error("Error found at the getUsers at UserController "+e);
            return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/all")
    public ResponseEntity<?> getAllUsers(){
        Authentication auth =SecurityContextHolder.getContext().getAuthentication();
        String username=auth.getName();
        try{
            List<User> users=userRepository.findAll();
            return new ResponseEntity<>(users, HttpStatus.OK);
        }catch (Exception e){
            throw  new RuntimeException(e);
        }
    }
    @GetMapping("/my-blogs")
    public ResponseEntity<?> getMyBlogs(){
        Authentication auth=SecurityContextHolder.getContext().getAuthentication();
        String username =auth.getName();
        try{
            User user=userRepository.getBlogs(username);
            Map<String, Object> result=new HashMap<>();
            result.put("blogs", user.getJournals());
            return new ResponseEntity<>(result, HttpStatus.OK);
        }catch (Exception e){
//            throw new RuntimeException("error fouund in my-blogs user controller"+e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
