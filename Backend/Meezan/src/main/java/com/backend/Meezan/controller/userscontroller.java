package com.backend.Meezan.controller;

import com.backend.Meezan.model.OtpStore;
import com.backend.Meezan.model.User;
import com.backend.Meezan.repo.otpstorerepo;
import com.backend.Meezan.repo.usersrepo;
import com.backend.Meezan.service.userservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api")
public class userscontroller {

    @Autowired
    private userservice service;

    @Autowired
    private usersrepo repo;

    @Autowired
    private otpstorerepo otprepo;


    /** STEP 1️⃣: Generate OTP and SAVE in Database (keep record, don’t delete) */
    @PostMapping("/request-otp")
    public ResponseEntity<Map<String, String>> requestOtp(@RequestBody User user) {

        String otp = String.format("%06d", new Random().nextInt(999999));

        // Check if OTP already exists for this email - update or create
        Optional<OtpStore> existingOtp = otprepo.findByEmail(user.getRegisteredEmailAddress());
        OtpStore otpEntity;

        if(existingOtp.isPresent()){
            otpEntity = existingOtp.get();
            otpEntity.setOtp(otp);
            otpEntity.setCreatedAt(LocalDateTime.now());
        }else{
            otpEntity = new OtpStore(user.getRegisteredEmailAddress(),otp);
        }

        otprepo.save(otpEntity);


        // Simulate SMS/Email send
        System.out.println("Generated OTP for " + user.getRegisteredEmailAddress() + ": " + otp);

        Map<String, String> response = new HashMap<>();
        response.put("message", "OTP sent successfully to " + user.getRegisteredEmailAddress());
        response.put("email", user.getRegisteredEmailAddress());
        return ResponseEntity.ok(response);
    }

    /** STEP 2️⃣: Verify OTP (without deleting from DB) */
    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyOTP(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String enteredOTP = body.get("otp");

        Optional<OtpStore> otpRecord = otprepo.findByEmail(email);

        System.out.println("=== 🔍 OTP Verification Debug ===");
        System.out.println("Email received: " + email);
        System.out.println("Entered OTP: " + enteredOTP);
        System.out.println("Stored OTP: " + (otpRecord.isPresent() ? otpRecord.get().getOtp() : "null"));
        System.out.println("===============================");

        if(otpRecord.isPresent() && otpRecord.get().getOtp().equals(enteredOTP)){
            System.out.println("✅ OTP verified successfully for " + email);
            return ResponseEntity.ok("OTP verified successfully!");
        }else {
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
    public ResponseEntity<?> createUser(@RequestBody User user) {
        try {
            User savedUser = service.createuser(user);
            return ResponseEntity.ok(savedUser);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
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
