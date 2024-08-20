package com.example.blogApp.service;

import com.example.blogApp.api.response.WeatherResponse;
import com.example.blogApp.cache.AppCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

import java.net.http.HttpResponse;

@Service
public class WeatherService {
    @Autowired
    private RedisService redisService;
    @Autowired
    private AppCache appCache;
    @Autowired
    private RestTemplate restTemplate;
    @Value("${weather.api.key}")
    private String apiKey;
    public String getWeather(String city){
        WeatherResponse weatherResponse=redisService.get("weather_of_"+city, WeatherResponse.class);
        if(weatherResponse!=null)
            return weatherResponse.getMain().getFeelsLike()+"";
        else{
//            String finalURL=appCache.appCache.get("weather_api").toString().replace("<CITY>", city).replace("<API_KEY>", apiKey);
            String finalApi = appCache.appCache.get("weather_api").replace("<CITY>", city).replace("<API_KEY>", apiKey);
            System.out.print("\n the value of the final api is: "+finalApi);
            ResponseEntity<WeatherResponse> response=restTemplate.exchange(finalApi, HttpMethod.GET, null, WeatherResponse.class);
            WeatherResponse body=response.getBody();
            System.out.print("the value of the body is: "+body);
            if(body!=null){
                redisService.set("weather_of_"+city, body,300l );
            }
            return body.getMain().getFeelsLike()+"";
        }
    }

}
