package com.example.journalApp.service;

import com.example.journalApp.entity.User;
import com.example.journalApp.repository.UserRepository;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private static final PasswordEncoder passwordEncoder=new BCryptPasswordEncoder();
    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    @Autowired
    private UserRepository userRepository;
    public List<User> getUsers(String username){
        try {
            User user=loadByUsername(username);
            if(user==null) throw new RuntimeException("Username not found "+username);
            if(user.getRoles().contains("ADMIN")) return userRepository.findAll();
            else throw new RuntimeException("Not authorized");
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }
    public void createUser(User user){
        if(user.getRoles()==null ||user.getRoles().isEmpty()){
            user.setRoles(new ArrayList<>(Arrays.asList("USER")));
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }
    public void updateJournals(User user){
        userRepository.save(user);
    }
    public Optional<User> getUserById(ObjectId Id){
        return userRepository.findById(Id);
    }
    public void deleteUserById(ObjectId Id){
        userRepository.deleteById(Id);
    }
    public boolean updateUser(User newUser, ObjectId Id){
        try{
            Optional<User> old=getUserById(Id);
            if(old.isPresent()){
                User oldUser=old.get();
                oldUser.setUsername(newUser.getUsername());
                oldUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
                createUser(oldUser);
            }
            return true;
        }catch (Exception e){
            log.error("Exception found in the updateUser at userService file "+e);
            return false;
        }
    }
    public User loadByUsername(String username){
        return userRepository.findByUsername(username);
    }
}
