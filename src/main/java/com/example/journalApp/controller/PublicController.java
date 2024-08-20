package com.example.journalApp.controller;

import com.example.journalApp.entity.Journal;
import com.example.journalApp.entity.User;
import com.example.journalApp.service.JournalService;
import com.example.journalApp.service.UserDetailServiceImpl;
import com.example.journalApp.service.UserService;
import com.example.journalApp.service.WeatherService;
import com.example.journalApp.utils.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/public")
public class PublicController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserDetailServiceImpl userDetailService;
    @Autowired
    private JwtUtil jwtUtil;
    private static final Logger log = LoggerFactory.getLogger(PublicController.class);
    @Autowired
    private JournalService journalService;
    @Autowired
    private UserService userService;
    @Autowired
    private WeatherService weatherService;
    @GetMapping("/greetings")
    public ResponseEntity<?> greetings(){
        String response=weatherService.getWeather("delhi");
        return new ResponseEntity<>("Hello welcome the temp feels like "+response.trim() +"'C", HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<?> createUser(@RequestBody User user){
        try{
            if(user.getUsername().isEmpty() || user.getPassword().isEmpty()){
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            userService.createUser(user);
            return new ResponseEntity<>("User created", HttpStatus.OK);
        }catch (Exception e){
            log.error("Exception found in the createUser function at User controller "+e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user){
       try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
            UserDetails userDetails=userDetailService.loadUserByUsername(user.getUsername());
            String jwt=jwtUtil.generateToken(userDetails.getUsername());
            Map<String , String> result=new HashMap<>();
            result.put("token", jwt);
            result.put("user_id", userService.loadByUsername(user.getUsername()).getId());
            return new ResponseEntity<>(result, HttpStatus.OK);
       }catch (Exception e){
           throw new RuntimeException("Error at login");
       }
    }
    @GetMapping("/all-blogs")
    public ResponseEntity<?> getAllBlogs(){
        try{
            List<Journal> journals = journalService.getJournals();
            Map<String, Object> result=new HashMap<>();
            result.put("blogs", journals);
            return new ResponseEntity<>(result, HttpStatus.OK);
        }catch (Exception e){
            log.error("Found error at getJournals controller at JournalController "+e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
