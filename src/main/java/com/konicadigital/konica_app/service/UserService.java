package com.konicadigital.konica_app.service;

import com.konicadigital.konica_app.dto.UserRegisterDTO;
import com.konicadigital.konica_app.dto.VerifyOtpDTO;
import com.konicadigital.konica_app.model.User;
import com.konicadigital.konica_app.model.User;
import com.konicadigital.konica_app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class UserService {

    @Autowired private UserRepository userRepo;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private JavaMailSender mailSender; // Needs config in application.properties

    // --- 1. REGISTER USER ---
    public String registerUser(UserRegisterDTO dto) {
        // Validation: Email Exists?
        if (userRepo.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email already registered!");
        }

        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());
        user.setRole("USER"); // Default role
        user.setVerified(false); // Not active yet

        // Hash the password!
        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        // Generate OTP
        String otp = String.format("%06d", new Random().nextInt(999999));
        user.setOtp(otp);
        user.setOtpExpiry(LocalDateTime.now().plusMinutes(10));

        userRepo.save(user);

        // Send Email (Wrap in try-catch so it doesn't crash if SMTP is not set up)
        try {
            sendOtpEmail(user.getEmail(), otp);
        } catch (Exception e) {
            System.out.println("⚠️ EMAIL FAILED (Check Config). OTP is: " + otp);
        }

        return "User registered! Please verify OTP sent to email.";
    }

    // --- 2. VERIFY OTP ---
    public String verifyOtp(VerifyOtpDTO dto) {
        User user = userRepo.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.isVerified()) {
            return "User already verified!";
        }

        if (user.getOtp().equals(dto.getOtp()) && LocalDateTime.now().isBefore(user.getOtpExpiry())) {
            user.setVerified(true);
            user.setOtp(null); // Clear OTP
            userRepo.save(user);
            return "Email verified successfully! You can now login.";
        } else {
            throw new RuntimeException("Invalid or Expired OTP");
        }
    }

    // --- 3. LOGIN (Manual) ---
    public User login(String email, String password) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Check if verified
        if (!user.isVerified()) {
            throw new RuntimeException("Account not verified. Please verify OTP first.");
        }

        // Check Password Match (Raw vs Hash)
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid Credentials");
        }

        return user;
    }

    // --- 4. GOOGLE LOGIN / SIGNUP ---
    public String googleLogin(String email, String name, String googleToken) {
        // Note: Real apps verify the 'googleToken' with Google API here.
        // We will assume the token is valid for this example.

        Optional<User> existing = userRepo.findByEmail(email);

        if (existing.isPresent()) {
            User user = existing.get();
            // If they registered manually before, just log them in
            return "Login Successful";
        } else {
            // New Google User -> Register them automatically
            User newUser = new User();
            newUser.setEmail(email);
            newUser.setName(name);
            newUser.setGoogleLogin(true);
            newUser.setVerified(false); // Require OTP for first time as requested
            newUser.setRole("USER");
            newUser.setPassword(passwordEncoder.encode("GOOGLE_USER")); // Dummy password

            // Generate OTP
            String otp = String.format("%06d", new Random().nextInt(999999));
            newUser.setOtp(otp);
            newUser.setOtpExpiry(LocalDateTime.now().plusMinutes(10));

            userRepo.save(newUser);

            try { sendOtpEmail(email, otp); }
            catch (Exception e) { System.out.println("OTP: " + otp); }

            return "Google Account Created. Please Verify OTP sent to email.";
        }
    }

    // --- CRUD OPERATIONS ---
    public List<User> getAllUsers() { return userRepo.findAll(); }

    public void deleteUser(int id) {
        if (!userRepo.existsById(id)) throw new RuntimeException("User not found");
        userRepo.deleteById(id); // Triggers Soft Delete
    }

    // Helper: Send Email
    private void sendOtpEmail(String to, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Konica App - Your OTP Code");
        message.setText("Your verification code is: " + otp + "\nIt expires in 10 minutes.");
        mailSender.send(message);
    }
}