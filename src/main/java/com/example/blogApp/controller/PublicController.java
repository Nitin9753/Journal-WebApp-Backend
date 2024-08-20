package com.example.blogApp.controller;

import com.example.blogApp.api.response.WeatherResponse;
import com.example.blogApp.entity.Blog;
import com.example.blogApp.entity.User;
import com.example.blogApp.service.BlogService;
import com.example.blogApp.service.UserDetailServiceImpl;
import com.example.blogApp.service.UserService;
import com.example.blogApp.service.WeatherService;
import com.example.blogApp.utils.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private BlogService blogService;
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
            result.put("jwt", jwt);
            return new ResponseEntity<>(result, HttpStatus.OK);
       }catch (Exception e){
           throw new RuntimeException("Error at login");
       }
    }
    @GetMapping("/all-blogs")
    public ResponseEntity<?> getAllBlogs(){
        try{
            List<Blog> blogs = blogService.getJournals();
            return new ResponseEntity<>(blogs, HttpStatus.OK);
        }catch (Exception e){
            log.error("Found error at getJournals controller at JournalController "+e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
