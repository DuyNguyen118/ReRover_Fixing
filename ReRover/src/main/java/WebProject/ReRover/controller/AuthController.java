package WebProject.ReRover.controller;

import WebProject.ReRover.model.User;
import WebProject.ReRover.repository.UserRepository;
import WebProject.ReRover.util.EmailSending;
import WebProject.ReRover.util.OTPManager;
import WebProject.ReRover.services.UserService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder, UserService userService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login() {
        // This endpoint is handled by Spring Security's formLogin
        return ResponseEntity.ok(Collections.singletonMap("success", true));
    }

    @GetMapping("/user")
    public ResponseEntity<?> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getPrincipal())) {
            String username = authentication.getName();
            User user = userRepository.findByStudentId(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.status(401).body(Collections.singletonMap("error", "Not authenticated"));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> userData) {
        try {
            // Validate required fields
            if (userData.get("student_id") == null || userData.get("student_id").trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Collections.singletonMap("error", "Student ID is required"));
            }
            if (userData.get("fullname") == null || userData.get("fullname").trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Collections.singletonMap("error", "Full name is required"));
            }
            if (userData.get("email") == null || userData.get("email").trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Collections.singletonMap("error", "Email is required"));
            }
            if (userData.get("password") == null || userData.get("password").trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Collections.singletonMap("error", "Password is required"));
            }

            // Check if username or email already exists
            if (userRepository.existsByStudentId(userData.get("student_id"))) {
                return ResponseEntity.badRequest().body(Collections.singletonMap("error", "Student ID already exists"));
            }
            if (userRepository.existsByEmail(userData.get("email"))) {
                return ResponseEntity.badRequest().body(Collections.singletonMap("error", "Email already in use"));
            }

            // Create new user
            User newUser = new User();
            newUser.setStudentId(userData.get("student_id"));
            newUser.setFullname(userData.get("fullname"));
            newUser.setEmail(userData.get("email"));
            newUser.setPassword(passwordEncoder.encode(userData.get("password")));
            newUser.setCreatedAt(LocalDateTime.now());
            
            // Set optional fields
            if (userData.containsKey("phoneNumber")) {
                newUser.setPhoneNumber(userData.get("phoneNumber"));
            }
            newUser.setMeritPoint(0);
            
            // Save user
            User savedUser = userRepository.save(newUser);
            
            // Clear password before returning
            savedUser.setPassword(null);
            return ResponseEntity.ok(Collections.singletonMap("success", savedUser));
            
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Collections.singletonMap("error", "Registration failed: " + e.getMessage()));
        }
    }

    @PostMapping("/forgot-password")
    @ResponseBody
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        if (email == null || email.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Email is required");
        }
        
        // Check if email exists in database
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            // For security reasons, return success even if email doesn't exist
            return ResponseEntity.ok("If your email is registered, you will receive a password reset code");
        }

        // Generate OTP
        String otp = OTPManager.generateOTP(email);
        
        // Send email with OTP
        String subject = "Password Reset - Verification Code";
        String message = "Your password reset code is: " + otp + "\n\n" +
                        "This code will expire in 5 minutes.\n\n" +
                        "If you didn't request this, please ignore this email.";

        try {
            EmailSending.sendSimpleEmail(email, subject, message);
            return ResponseEntity.ok("Verification code sent to your email");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to send verification code. Please try again.");
        }
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String code = request.get("code");
        
        if (OTPManager.verifyOTP(email, code)) {
            // OTP is valid, generate a token or return success
            return ResponseEntity.ok("OTP verified successfully");
        } else {
            return ResponseEntity.badRequest().body("Invalid or expired verification code");
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String newPassword = request.get("newPassword");
        
        // Update the password in the database
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            return ResponseEntity.badRequest().body("Invalid email address");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
userService.saveUser(user);
        
        return ResponseEntity.ok("Password has been reset successfully");
    }
}
