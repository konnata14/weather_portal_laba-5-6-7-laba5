package com.example.weather_portal.controller;

import com.example.weather_portal.model.User;
import com.example.weather_portal.repository.UserRepository;
import com.example.weather_portal.service.VisitService;
import com.example.weather_portal.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.servlet.http.HttpServletRequest;



import java.nio.file.Files;

@Controller
public class ProfileController {

    private final UserRepository userRepo;
    private final VisitService visitService;
    private final UserService userService;

    public ProfileController(UserRepository userRepo, VisitService vs, UserService us) {
        this.userRepo = userRepo;
        this.visitService = vs;
        this.userService = us;
    }
    @PostMapping("/profile/password")
    public String updatePassword(Authentication auth,
                                 @RequestParam String oldPassword,
                                 @RequestParam String newPassword,
                                 @RequestParam String confirmPassword,
                                 HttpServletRequest request) {

        User user = userRepo.findByUsername(auth.getName())
                .orElseThrow();

        try {
            userService.changePassword(user, oldPassword, newPassword, confirmPassword);
        }
        catch (RuntimeException e) {
            return "redirect:/profile?passwordError";
        }

        request.getSession().invalidate(); // выходим из системы
        return "redirect:/login?passwordChanged";
    }

    @PostMapping("/profile/username")
    public String updateUsername(Authentication auth,
                                 @RequestParam("username") String username, HttpServletRequest request) {

        User user = userRepo.findByUsername(auth.getName())
                .orElseThrow();

        userService.changeUsername(user, username);

        request.getSession().invalidate(); // обязательно

        return "redirect:/login?usernameChanged";
    }



    @GetMapping("/profile")
    public String profile(Authentication auth, Model model, HttpSession session) throws Exception {
        String username = auth.getName();
        User user = userRepo.findByUsername(username).orElseThrow();
        Boolean profileViewed = (Boolean) session.getAttribute("profileViewed");
        if (profileViewed == null || !profileViewed) {
            visitService.increment(user);
            session.setAttribute("profileViewed", true);
        }
        model.addAttribute("user", user);
        model.addAttribute("visitCount", visitService.getCounterFor(user).getCount());
        model.addAttribute("serverTime", java.time.LocalDateTime.now().withNano(0));
        return "profile";
    }

    @PostMapping("/profile/upload")
    public String uploadPhoto(Authentication auth, MultipartFile file) throws Exception {
        User user = userRepo.findByUsername(auth.getName()).orElseThrow();
        userService.savePhoto(user, file);
        return "redirect:/profile";
    }

}