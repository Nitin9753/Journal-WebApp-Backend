package com.example.blogApp.api.response;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class WeatherResponse {
    private Main main;
    @Data
    public class Main{
        public double temp;
        @JsonProperty("feels_like")
        public double feelsLike;
    }
}

