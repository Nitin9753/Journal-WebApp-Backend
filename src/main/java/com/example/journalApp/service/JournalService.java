package com.example.journalApp.service;

import com.example.journalApp.entity.Journal;
import com.example.journalApp.entity.User;
import com.example.journalApp.repository.JournalRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class JournalService {
    @Autowired
    private UserService userService;

    @Autowired
    private JournalRepository journalRepository;
    public List<Journal> getJournals(){
        try {
            return journalRepository.findAll();
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }
    @Transactional
    public boolean createJournal(String username, Journal newJournal){
        try {
            System.out.print(username+"\n");
            User user=userService.loadByUsername(username);
            if(user==null) throw new RuntimeException("user not found");
            newJournal.setCreatedBy(user.getId().toString());
            journalRepository.save(newJournal);
            user.getJournals().add(newJournal);
            userService.updateJournals(user);
            return true;
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }
    public Optional<Journal> getJournalById(String username, String Id){
        try {
            if(username==null) return null;
            User user=userService.loadByUsername(username);
            if(user==null) return null;
            List<Journal> journals =user.getJournals().stream().filter(x->x.getId().equals(Id)).toList();
            if(journals.isEmpty()) return null;
            return journalRepository.findById(new ObjectId(Id));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @Transactional
    public boolean deleteBlogoById(String username, String Id){
        try {
            User user=userService.loadByUsername(username);
            if(user==null)  throw new RuntimeException("user not found");
            Optional<Journal> blog= journalRepository.findById(new ObjectId(Id));
            if(blog.isPresent()){
                boolean removedFromUser=user.getJournals().removeIf(x->x.getId().equals(Id));
                if(removedFromUser==false) throw new RuntimeException("Blog not found");
                journalRepository.deleteById(new ObjectId(Id));
                userService.updateJournals(user);
                return true;
            }
            else throw new RuntimeException("Blog not Present");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public boolean editJournalById(Journal newJournal, String Id){
        Optional<Journal> old= journalRepository.findById(new ObjectId(Id));
        if(old.isPresent()){
            Journal oldEntry=old.get();
            oldEntry.setTitle(newJournal.getTitle());
            oldEntry.setContent(newJournal.getContent());
            oldEntry.setCreatedAt(LocalDateTime.now());
            journalRepository.save(oldEntry);
            return true;
        }
        return false;
    }
}