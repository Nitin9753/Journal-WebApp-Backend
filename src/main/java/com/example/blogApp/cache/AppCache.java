package com.example.blogApp.cache;

import com.example.blogApp.entity.Config;
import com.example.blogApp.repository.ConfigRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Component
public class AppCache {
    @Autowired
    private ConfigRepository configRepository;
    public Map<String, String> appCache=new HashMap<>();

    @PostConstruct
    public void init(){
        appCache=new HashMap<>();
        for(Config config : configRepository.findAll()){
            appCache.put(config.getKey(), config.getValue());
        }
    }
}
