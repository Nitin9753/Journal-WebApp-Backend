package com.example.journalApp.controller;


import com.example.journalApp.entity.Journal;
import com.example.journalApp.service.JournalService;
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
public class JournalController {

    private static final Logger log = LoggerFactory.getLogger(JournalController.class);
    @Autowired
    private JournalService journalService;


    @GetMapping("/{Id}")
    public ResponseEntity<?> getJournalById(@PathVariable String Id){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        String username=authentication.getName();
        try {
            Optional<Journal> journal= journalService.getJournalById(username, Id);
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
    public ResponseEntity<?> createJournal(@RequestBody Journal newJournal){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        String username=authentication.getName();
        if(username==null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        try{
            boolean result= journalService.createJournal(username, newJournal);
            if(result)  return new ResponseEntity<>(newJournal, HttpStatus.OK);
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
            boolean result = journalService.deleteBlogoById(username, Id);
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
    public ResponseEntity<?> updateBlog(@RequestBody Journal newJournal, @PathVariable String id){
        try {
            if(journalService.editJournalById(newJournal, id)) return new ResponseEntity<>("Blog updated", HttpStatus.OK);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}