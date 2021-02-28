package com.odelan.editor;

import com.odelan.editor.service.FileStorageService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class EditorApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(EditorApplication.class, args);
    }

    @Bean
    CommandLineRunner init(FileStorageService storageService) {
        return (args) -> {
            storageService.init();
        };
    }

}
