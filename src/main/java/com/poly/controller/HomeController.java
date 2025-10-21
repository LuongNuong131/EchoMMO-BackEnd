package com.poly.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model; // <<<--- Giữ lại import này
// import org.springframework.beans.factory.annotation.Autowired; // <<<--- Bỏ Autowired UserRepository nếu không dùng nữa
// import org.springframework.security.core.Authentication; // <<<--- Bỏ import này
// import org.springframework.security.core.context.SecurityContextHolder; // <<<--- Bỏ import này
// import org.springframework.security.core.userdetails.UsernameNotFoundException; // <<<--- Bỏ import này
// import com.poly.model.User; // <<<--- Bỏ import này
// import com.poly.repository.UserRepository; // <<<--- Bỏ import này

@Controller
public class HomeController {

    // Bỏ UserRepository nếu không cần fetch User đầy đủ ở đây nữa
    // @Autowired
    // private UserRepository userRepository;

    @GetMapping("/")
    public String home(Model model) { // Vẫn giữ Model để truyền activePage
        // Không cần lấy thông tin User ở đây nữa, Thymeleaf sẽ lấy từ Security Context
        /*
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
        model.addAttribute("currentUser", currentUser);
        */

        model.addAttribute("activePage", "home"); // Vẫn truyền activePage cho sidebar

        return "home"; // Trả về templates/home.html
    }
}