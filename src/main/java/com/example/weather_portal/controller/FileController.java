package com.example.weather_portal.controller;

import com.example.weather_portal.service.UserService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.net.MalformedURLException;
import java.nio.file.Path;

@Controller
public class FileController {

    private final UserService userService;

    public FileController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/uploads/{filename:.+}")
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) throws MalformedURLException {
        Path path = userService.getPhotoPath(filename);

        Resource file = new UrlResource(path.toUri());
        if (!file.exists()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok().body(file);
    }
}
