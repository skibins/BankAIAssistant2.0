package com.bankaiassistant.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/")
public class PageController {

    @GetMapping("/")
    public String index(HttpSession session, Model model) {
        String userEmail = (String) session.getAttribute("user_email");

        if (userEmail != null) {
            model.addAttribute("userEmail", userEmail.split("@")[0]);
        }

        return "main";
    }

    @GetMapping("/services")
    public String services(HttpSession session, Model model) {
        String userEmail = (String) session.getAttribute("user_email");

        if (userEmail != null) {
            model.addAttribute("userEmail", userEmail.split("@")[0]);
        }

        return "services";
    }

    @GetMapping("/about")
    public String about(HttpSession session, Model model) {
        String userEmail = (String) session.getAttribute("user_email");

        if (userEmail != null) {
            model.addAttribute("userEmail", userEmail.split("@")[0]);
        }

        return "about";
    }
}