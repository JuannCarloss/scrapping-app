package com.scrap.knewin.configs;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfigs {

    @Bean
    public RestTemplate restTemplateBuilderConfig(){
        return new RestTemplateBuilder()
                .defaultHeader("User-Agent", "Mozilla/5.0")
                .build();
    }
}
