package com.example.journalApp.cache;

import com.example.journalApp.entity.Config;
import com.example.journalApp.repository.ConfigRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
