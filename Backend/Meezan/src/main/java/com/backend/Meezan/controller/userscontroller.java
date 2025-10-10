package com.backend.Meezan.controller;

import com.backend.Meezan.model.User;
import com.backend.Meezan.repo.usersrepo;
import com.backend.Meezan.service.userservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@RestController
@CrossOrigin(origins = "*")
public class userscontroller {

    @Autowired
    private userservice service;

    @Autowired
    private usersrepo repo;

    // Temporary store for OTP
    private Map<String, String> otpStore = new HashMap<>();

    /** STEP 1️⃣: Generate OTP when edit form submitted */
    @PostMapping("/request-otp")
    public ResponseEntity<Map<String, String>> requestOtp(@RequestBody User user) {

        String otp = String.format("%06d", new Random().nextInt(999999));
        otpStore.put(user.getRegisteredEmailAddress(), otp);

        // Simulate SMS/Email send
        System.out.println("Generated OTP for " + user.getRegisteredEmailAddress() + ": " + otp);

        Map<String, String> response = new HashMap<>();
        response.put("message", "OTP sent successfully to " + user.getRegisteredEmailAddress());
        response.put("email", user.getRegisteredEmailAddress());
        return ResponseEntity.ok(response);
    }

    /** STEP 2️⃣: Verify OTP only (no DB save here) */
    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyOTP(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String enteredOTP = body.get("otp");

        String storedOTP = otpStore.get(email);

        System.out.println("=== 🔍 OTP Verification Debug ===");
        System.out.println("Email received: " + email);
        System.out.println("Entered OTP: " + enteredOTP);
        System.out.println("Stored OTP: " + storedOTP);
        System.out.println("===============================");

        if (storedOTP != null && storedOTP.equals(enteredOTP)) {
            otpStore.remove(email);
            System.out.println("✅ OTP verified successfully for " + email);
            return ResponseEntity.ok("OTP verified successfully!");
        } else {
            System.out.println("❌ Invalid or expired OTP for " + email);
            return ResponseEntity.status(400).body("Invalid or expired OTP");
        }
    }


    /** STEP 3️⃣: Normal CRUD APIs **/
    @GetMapping("/allusers")
    public List<User> getAllUsers() {
        return service.getAllusers();
    }

    @GetMapping("/byid/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return service.getuserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/create")
    public User createUser(@RequestBody User user) {
        return service.createuser(user);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User updateUser) {
        User existing = repo.findById(id).orElse(null);
        if (existing == null) return ResponseEntity.notFound().build();

        existing.setRegisteredContactNumber(updateUser.getRegisteredContactNumber());
        existing.setRegisteredEmailAddress(updateUser.getRegisteredEmailAddress());
        existing.setRegisteredHomeAddress(updateUser.getRegisteredHomeAddress());
        existing.setCity(updateUser.getCity());
        existing.setCountry(updateUser.getCountry());

        repo.save(existing);
        return ResponseEntity.ok(existing);
    }
}
