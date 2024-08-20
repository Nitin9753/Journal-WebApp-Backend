package com.example.blogApp.controller;


import com.example.blogApp.entity.Blog;
import com.example.blogApp.service.BlogService;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;
@RestController
@RequestMapping("/api/v1/blog")
public class BlogController {

    private static final Logger log = LoggerFactory.getLogger(BlogController.class);
    @Autowired
    private BlogService blogService;


    @GetMapping("/{Id}")
    public ResponseEntity<?> getJournalById(@PathVariable String Id){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        String username=authentication.getName();
        try {
            Optional<Blog> journal= blogService.getJournalById(username, Id);
            if(journal!=null && journal.isPresent()){
                return new ResponseEntity<>(journal, HttpStatus.OK);
            }
            return new ResponseEntity<>("Requested resource is not found", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.error("Error found in the getJournalById function at journalController"+e);
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/create-blog")
    public ResponseEntity<?> createJournal(@RequestBody Blog newBlog){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        String username=authentication.getName();
        if(username==null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        try{
            boolean result= blogService.createJournal(username, newBlog);
            if(result)  return new ResponseEntity<>(newBlog, HttpStatus.OK);
            throw new RuntimeException("Error creating blog");
        }catch (Exception e){
            log.error("Exception found in the JournalController at create Journal"+e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete-blog/{Id}")
    public ResponseEntity<?> deleteJournalById(@PathVariable String Id) {
        Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
        String username=authentication.getName();
        if(username==null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        try{
            boolean result = blogService.deleteBlogoById(username, Id);
            if (result) {
                return new ResponseEntity<>("Content deleted", HttpStatus.OK);
            }
            return new ResponseEntity<>("Content not found", HttpStatus.NOT_FOUND);
        }catch(Exception e){
            log.error("Exception found in the deleteJournalById controller at JournalController "+e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PutMapping("/update-blog/{id}")
    public ResponseEntity<?> updateBlog(@RequestBody Blog newBlog, @PathVariable ObjectId id){
        try {
            if(blogService.editJournalById(newBlog, id)) return new ResponseEntity<>("Blog updated", HttpStatus.OK);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}