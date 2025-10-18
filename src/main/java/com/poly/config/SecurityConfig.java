package com.poly.config;

import com.poly.model.User;
import com.poly.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    UserRepository userRepository;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        // Định nghĩa cách Spring Security tìm user trong DB
        return username -> {
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy user: " + username));
            
            // Trả về một đối tượng UserDetails (Spring Security)
            return org.springframework.security.core.userdetails.User
                    .withUsername(user.getUsername())
                    .password(user.getPassword_hash()) // Mật khẩu đã mã hóa
                    .roles("USER") // Bạn có thể thêm vai trò (role) nếu có
                    .build();
        };
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Tắt CSRF (tạm thời để test)
            .authorizeHttpRequests(auth -> auth
                // Cho phép truy cập tự do các trang này
                .requestMatchers("/login", "/register", "/css/**", "/js/**", "/images/**")
                .permitAll()
                // Tất cả các request còn lại phải được xác thực
                .anyRequest().authenticated() 
            )
            .formLogin(form -> form
                .loginPage("/login")         // Trang login của bạn
                .loginProcessingUrl("/login") // URL mà form POST tới (Spring Security tự xử lý)
                .defaultSuccessUrl("/", true) // Đăng nhập thành công -> về trang chủ
                .failureUrl("/login?error=true") // Đăng nhập thất bại
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout") // URL để logout
                .logoutSuccessUrl("/login?logout=true") // Logout thành công
                .permitAll()
            );

        return http.build();
    }
}