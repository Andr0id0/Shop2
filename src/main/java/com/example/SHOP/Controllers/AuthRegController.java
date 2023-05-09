package com.example.SHOP.Controllers;


import com.example.SHOP.models.Role;
import com.example.SHOP.models.User;
import com.example.SHOP.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Collections;


@Controller
public class AuthRegController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @GetMapping("/login")
    public String login(){
        return "login";
    }



    @GetMapping("/user")
    public String user(){
        return "user";
    }


    @GetMapping("/reg")
    public String reg(@RequestParam(name ="error", defaultValue = "", required = false) String error, Model model){
        if(error.equals("username")){
            model.addAttribute("error", "Такой логин уже используется");
        }
        return "reg";
    }



    @PostMapping("/reg")
    public String addUser(@RequestParam String username,
                          @RequestParam String email,
                          @RequestParam String password){

        if(userRepository.findByUsername(username) != null){
            return "redirect:/reg&error=username";
        }

        password = passwordEncoder.encode(password);
        User user = new User(username, password, email, true, Collections.singleton(Role.USER));
        userRepository.save(user);
        return "redirect:/login";

    }
    @GetMapping("user/update")
    public String update(Principal principal, Model model){
        User user = userRepository.findByUsername(principal.getName());
        model.addAttribute("user", user);
        return "user-update";
    }

    @PostMapping("/user/update")
    public String updateUser(Principal principal,
                             @RequestParam String email,
                             @RequestParam String password){
        User user = userRepository.findByUsername(principal.getName());
        user.setEmail(email);
        user.setPassword(password);
        userRepository.save(user);
        return "redirect:/user";
    }



}
