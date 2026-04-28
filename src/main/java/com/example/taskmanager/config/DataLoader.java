package com.example.taskmanager.config;

import com.example.taskmanager.model.User;
import com.example.taskmanager.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner initDatabase(UserRepository userRepository) {
        return args -> {
            if (userRepository.findByUsername("admin").isEmpty()) {
                User user = new User();
                user.setUsername("admin");
                user.setPassword("123");
                userRepository.save(user);
            }
        };
    }
}