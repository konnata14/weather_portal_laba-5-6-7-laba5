package com.example.weather_portal.initializer;

import com.example.weather_portal.model.Role;
import com.example.weather_portal.model.User;
import com.example.weather_portal.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner init(UserRepository userRepository, PasswordEncoder encoder) {
        return args -> {

            // Создаём админа
            if (userRepository.findByUsername("admin").isEmpty()) {
                User admin = new User();
                admin.setUsername("admin");
                admin.setPassword(encoder.encode("adminpass"));
                admin.setRole(Role.ROLE_ADMIN);
                userRepository.save(admin);
            }

            // Создаём модератора
            if (userRepository.findByUsername("mod").isEmpty()) {
                User mod = new User();
                mod.setUsername("mod");
                mod.setPassword(encoder.encode("modpass"));
                mod.setRole(Role.ROLE_MODERATOR);
                userRepository.save(mod);
            }

            // Создаём обычного пользователя
            if (userRepository.findByUsername("user").isEmpty()) {
                User user = new User();
                user.setUsername("user");
                user.setPassword(encoder.encode("userpass"));
                user.setRole(Role.ROLE_USER);
                userRepository.save(user);
            }
        };
    }
}
