package com.example.easyoderba.Config;

import com.cloudinary.Cloudinary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CloudinaryConfig {
    @Bean
    public Cloudinary cloudinary() {
        Map<String, String> map = new HashMap<>();
        map.put("cloud_name", "dohgdvnuy");
        map.put("api_key", "629845858442262");
        map.put("api_secret", "gB6Ld8Agabga0TuyOH1RD9_2BG0");
        return new Cloudinary(map);
    }
}
