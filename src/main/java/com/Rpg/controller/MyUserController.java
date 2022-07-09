package com.Rpg.controller;

import com.Rpg.dto.UserDTO;
import com.Rpg.service.MyUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;

@Controller
public class MyUserController {

    private MyUserService myUserService;

    @Autowired
    public MyUserController(MyUserService myUserService) {
        this.myUserService = myUserService;
    }

    @ModelAttribute("userDTO")
    public UserDTO getModel() {
        return new UserDTO();
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/registration")
    public String getRegistration() {
        return "registration";
    }

    @PostMapping("/registration")
    public String registration(@ModelAttribute("userDTO") UserDTO userDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) return "registration";
        myUserService.registration(userDTO);
        return "redirect:/login";
    }

    @GetMapping
    public String getUserName(Principal principal) {
        String name = principal.getName();
        return "redirect:" + name + "/hero/choose";
    }
}