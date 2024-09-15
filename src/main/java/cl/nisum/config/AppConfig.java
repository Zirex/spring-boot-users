package cl.nisum.config;

import java.util.Arrays;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableWebMvc
public class AppConfig implements WebMvcConfigurer {
    @Value("${app.cors.pathPattern:/**}")
    private String pathPattern;
    
    @Value("${app.cors.allowedOrigins:*}")
    private String[] allowedOrigins;
    
    @Value("${app.cors.allowedHeaders:*}")
    private String[] allowedHeaders;
    
    @Value("${app.cors.allowedMethods:*}")
    private String[] allowedMethods;
    
    @Value("${app.cors.maxAge:1800}")
    private long maxAge;
    
    @Override
    public void addCorsMappings(CorsRegistry corsRegistry) {

        log.info("pathPattern: {}",pathPattern);
        log.info("allowedOrigins: {}",Arrays.toString(allowedOrigins));
        log.info("allowedMethods: {}",Arrays.toString(allowedMethods));
        log.info("maxAge: {}",maxAge);
        
        corsRegistry.addMapping(pathPattern)
            .allowedHeaders(allowedHeaders)
            .allowedOrigins(allowedOrigins)
            .allowedMethods(allowedMethods)
            .maxAge(maxAge);
    }
    
    @Bean
    public ModelMapper getModelMapper() {
        return new ModelMapper();
    }

    
}
