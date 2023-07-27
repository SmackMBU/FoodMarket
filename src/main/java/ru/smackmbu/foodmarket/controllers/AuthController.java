package ru.smackmbu.foodmarket.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.smackmbu.foodmarket.models.Role;
import ru.smackmbu.foodmarket.models.User;
import ru.smackmbu.foodmarket.repositories.UserRepository;

import java.util.HashMap;
import java.util.LinkedList;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/login")
    public String loginPage(){
        return "login";
    }

    @GetMapping("/registration")
    public String registrationPage(){
        return "registration";
    }

    @PostMapping("/reg")
    public String registration(@Qualifier("username") String username,
                               @Qualifier("password") String password){
        createUser(username, password);
        return "redirect:/auth/login";
    }

    public User createUser(String username, String password){
        User user = new User(username, passwordEncoder.encode(password), new HashMap<>(), Role.USER);
        userRepository.save(user);
        return user;
    }
}
