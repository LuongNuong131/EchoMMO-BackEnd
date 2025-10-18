package com.poly.controller;

import com.poly.model.User;
import com.poly.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Controller
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // HIỂN THỊ TRANG LOGIN (Đã sửa để nhận thông báo lỗi/logout)
    @GetMapping("/login")
    public String loginPage(@RequestParam(value = "error", required = false) String error,
                            @RequestParam(value = "logout", required = false) String logout,
                            Model model) {
        
        if (error != null) {
            model.addAttribute("error", "Tên đăng nhập hoặc mật khẩu không đúng!");
        }
        if (logout != null) {
            model.addAttribute("success", "Đăng xuất thành công!");
        }
        
        return "pages/login"; // Trả về file login.html
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

        if (!password.equals(confirm)) {
            model.addAttribute("error", "Mật khẩu xác nhận không khớp!");
            return "pages/register";
        }

        if (userRepository.findByUsername(username).isPresent() || userRepository.findByEmail(email).isPresent()) {
            model.addAttribute("error", "Tên đăng nhập hoặc email đã tồn tại!");
            return "pages/register";
        }

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setFull_name(full_name);
        user.setPassword_hash(passwordEncoder.encode(password));
        user.setCreated_at(LocalDateTime.now());
        
        userRepository.save(user);

        model.addAttribute("success", "Đăng ký thành công! Hãy đăng nhập.");
        return "pages/login"; // Chuyển về trang login
    }
}