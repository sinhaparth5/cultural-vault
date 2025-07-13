/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.culturalvault.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author parth
 */
@Configuration
public class AppConfig {
    /**
     * RestTemplate for external API calls
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
