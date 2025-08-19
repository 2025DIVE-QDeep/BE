package org.dive2025.qdeep.common.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.util.AntPathMatcher;

@EnableTransactionManagement
@Configuration
public class AppConfig {

    @Bean
    public AntPathMatcher antPathMatcher(){
        return new AntPathMatcher();
    }

    @Bean
    public ObjectMapper objectMapper(){
        return new ObjectMapper();
    }
}
