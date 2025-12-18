package com.example.weather_portal.service;

import com.example.weather_portal.model.User;
import com.example.weather_portal.model.Role;
import com.example.weather_portal.repository.UserRepository;
import com.example.weather_portal.validation.PasswordValidator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder pwEncoder;
    private final String uploadDir;

    public UserService(UserRepository userRepository, PasswordEncoder pwEncoder,
                       org.springframework.core.env.Environment env) {
        this.userRepository = userRepository;
        this.pwEncoder = pwEncoder;
        this.uploadDir = env.getProperty("app.upload.dir", "uploads");
        try {
            Files.createDirectories(Paths.get(uploadDir));
        } catch (IOException e) {
            throw new RuntimeException("Can't create upload dir", e);
        }
    }
    public void changePassword(User user,
                               String oldPassword,
                               String newPassword,
                               String confirmPassword) {
        if (!pwEncoder.matches(oldPassword, user.getPassword())) {
            throw new RuntimeException("Неверный старый пароль");
        }

        if (!newPassword.equals(confirmPassword)) {
            throw new RuntimeException("Пароли не совпадают");
        }

        if (!PasswordValidator.isValid(newPassword)) {
            throw new RuntimeException("Слабый пароль");
        }

        user.setPassword(pwEncoder.encode(newPassword));
        userRepository.save(user);
    }

    public void changeUsername(User user, String newUsername) {

        if (newUsername == null || newUsername.length() < 3) {
            throw new RuntimeException("Логин слишком короткий");
        }

        if (userRepository.findByUsername(newUsername).isPresent()) {
            throw new RuntimeException("Логин уже занят");
        }

        user.setUsername(newUsername);
        userRepository.save(user);
    }

    public User register(String username, String rawPassword, String role) {

        if (userRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException("Пользователь уже существует");
        }

        if (!PasswordValidator.isValid(rawPassword)) {
            throw new RuntimeException(
                    "Пароль должен содержать минимум 8 символов, " +
                            "1 букву, 1 цифру и 1 спецсимвол."
            );
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(pwEncoder.encode(rawPassword));
        user.setRole(Role.valueOf(role));

        return userRepository.save(user);
    }


    public void savePhoto(User user, MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) return;
        String ext = "";
        String original = file.getOriginalFilename();
        if (original != null && original.contains(".")) {
            ext = original.substring(original.lastIndexOf('.'));
        }
        String filename = "user-" + user.getId() + "-" + System.currentTimeMillis() + ext;
        Path target = Paths.get(uploadDir).resolve(filename);
        Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
        user.setPhotoFilename(filename);
        userRepository.save(user);
    }

    public Path getPhotoPath(String filename) {
        return Paths.get(uploadDir).resolve(filename);
    }
}