package com.example.blogApp.service;

import com.example.blogApp.entity.Blog;
import com.example.blogApp.entity.User;
import com.example.blogApp.repository.BlogRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class BlogService {
    @Autowired
    private UserService userService;

    @Autowired
    private BlogRepository blogRepository;
    public List<Blog> getJournals(){
        try {
            return blogRepository.findAll();
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }
    @Transactional
    public boolean createJournal(String username, Blog newBlog){
        try {
            blogRepository.save(newBlog);
            User user=userService.loadByUsername(username);
            if(user==null) throw new RuntimeException("user not found");
            user.getBlogs().add(newBlog);
            userService.updateJournals(user);
            return true;
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }
    public Optional<Blog> getJournalById(String username, String Id){
        try {
            if(username==null) return null;
            User user=userService.loadByUsername(username);
            if(user==null) return null;
            List<Blog> blogs=user.getBlogs().stream().filter(x->x.getId().equals(Id)).toList();
            if(blogs.isEmpty()) return null;
            return blogRepository.findById(new ObjectId(Id));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @Transactional
    public boolean deleteBlogoById(String username, String Id){
        try {
            User user=userService.loadByUsername(username);
            if(user==null)  throw new RuntimeException("user not found");
            Optional<Blog> blog= blogRepository.findById(new ObjectId(Id));
            if(blog.isPresent()){
                boolean removedFromUser=user.getBlogs().removeIf(x->x.getId().equals(Id));
                if(removedFromUser==false) throw new RuntimeException("Blog not found");
                blogRepository.deleteById(new ObjectId(Id));
                userService.updateJournals(user);
                return true;
            }
            else throw new RuntimeException("Blog not Present");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public boolean editJournalById(Blog newBlog, ObjectId Id){
        Optional<Blog> old=blogRepository.findById(Id);
        if(old.isPresent()){
            Blog oldEntry=old.get();
            oldEntry.setTitle(newBlog.getTitle());
            oldEntry.setContent(newBlog.getContent());
            oldEntry.setCreatedAt(LocalDateTime.now());
            blogRepository.save(oldEntry);
            return true;
        }
        return false;
    }
}