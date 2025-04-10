package com.bankaiassistant.controller;

import com.bankaiassistant.model.User;
import com.bankaiassistant.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/")
public class AuthController {

    @Autowired
    private Config config;

    @GetMapping("/login")
    public String login(Model model, HttpSession session) {
        if (session.getAttribute("user_email") != null) {
            return "redirect:/";
        }
        model.addAttribute("showRegisterForm", false);
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String password, HttpSession session, RedirectAttributes redirectAttributes) {
        List<Map<String, Object>> users = User.loadData(config.getDataFilePath());

        if (User.authenticate(email, password, users)) {
            session.setAttribute("user_email", email);
            return "redirect:/";
        }

        redirectAttributes.addFlashAttribute("error", "Invalid email or password. Try again.");
        return "redirect:/login";
    }

    @PostMapping("/register")
    public String register(Model model, @RequestParam String email, @RequestParam String password, @RequestParam String confirmPassword, HttpSession session, RedirectAttributes redirectAttributes) {
        List<Map<String, Object>> users = User.loadData(config.getDataFilePath());

        if (User.register(email, password, confirmPassword, users, config.getDataFilePath())) {
            session.setAttribute("user_email", email);
            return "redirect:/";
        }

        redirectAttributes.addFlashAttribute("error", "Invalid password or user already exists.");
        return "redirect:/login";
    }

    @GetMapping("/toggle_register")
    public String toggleRegister(Model model) {
        model.addAttribute("showRegisterForm", true);
        return "login";
    }

    @GetMapping("/toggle_login")
    public String toggleLogin(Model model) {
        model.addAttribute("showRegisterForm", false);
        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}