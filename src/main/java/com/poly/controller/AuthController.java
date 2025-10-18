package com.poly.controller;

import com.poly.model.User;
import com.poly.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder; // Dùng giao diện chung
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;

import java.time.LocalDateTime;

@Controller
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    // Tiêm Bean PasswordEncoder đã định nghĩa ở SecurityConfig
    @Autowired
    private PasswordEncoder passwordEncoder; 

    // Trang login
    @GetMapping("/login")
    public String loginPage() {
        return "pages/login";
    }

    // POST login
    @PostMapping("/login")
    public String doLogin(@RequestParam String username,
                          @RequestParam String password,
                          Model model,
                          HttpSession session) {

        User user = userRepository.findByUsername(username).orElse(null);
        
        // SỬ DỤNG BCrypt: So khớp mật khẩu thô với hash đã lưu
        if(user != null && passwordEncoder.matches(password, user.getPassword_hash())) {
            user.setLast_login(LocalDateTime.now());
            userRepository.save(user);
            session.setAttribute("user", user);
            return "redirect:/"; // Home
        } else {
            model.addAttribute("error", "Tên đăng nhập hoặc mật khẩu không đúng!");
            return "pages/login";
        }
    }

    // Trang đăng ký
    @GetMapping("/register")
    public String registerPage() {
        return "pages/register";
    }

    // POST đăng ký
    @PostMapping("/register")
    public String doRegister(@RequestParam String username,
                             @RequestParam String email,
                             @RequestParam String full_name,
                             @RequestParam String password,
                             @RequestParam String confirm,
                             Model model) {

        if(!password.equals(confirm)) {
            model.addAttribute("error", "Mật khẩu xác nhận không khớp!");
            return "pages/register";
        }

        if(userRepository.findByUsername(username).isPresent() || userRepository.findByEmail(email).isPresent()) {
            model.addAttribute("error", "Tên đăng nhập hoặc email đã tồn tại!");
            return "pages/register";
        }

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setFull_name(full_name);
        
        // SỬ DỤNG BCrypt: Mã hóa mật khẩu trước khi lưu
        user.setPassword_hash(passwordEncoder.encode(password));
        
        user.setCreated_at(LocalDateTime.now());
        userRepository.save(user);

        model.addAttribute("success", "Đăng ký thành công! Hãy đăng nhập.");
        return "pages/login";
    }

    // Logout
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}