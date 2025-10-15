package com.backend.Meezan.controller;

import com.backend.Meezan.model.OtpStore;
import com.backend.Meezan.model.User;
import com.backend.Meezan.model.UserSummary;
import com.backend.Meezan.repo.otpstorerepo;
import com.backend.Meezan.repo.usersrepo;
import com.backend.Meezan.repo.usersummaryrepo;
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

    @Autowired
    private usersummaryrepo summaryrepo;

    /** STEP 1️⃣: Generate OTP and SAVE in Database (keep record, don’t delete) */
    @PostMapping("/request-otp")
    public ResponseEntity<Map<String, String>> requestOtp(@RequestBody User user) {

        String otp = String.format("%06d", new Random().nextInt(999999));

        try{
            // 🔹 Call PostgreSQL stored procedure instead of JPA save()
            otprepo.saveOrUpdateOtp(user.getRegisteredEmailAddress(), otp);

            // Simulate SMS/Email send
            System.out.println("Generated OTP for " + user.getRegisteredEmailAddress() + ": " + otp);

            Map<String, String> response = new HashMap<>();
            response.put("message", "OTP sent successfully to " + user.getRegisteredEmailAddress());
            response.put("email", user.getRegisteredEmailAddress());
            return ResponseEntity.ok(response);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(500)
                    .body(Map.of("error", "Failed to save OTP: " + e.getMessage()));
        }
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

        try {
            Boolean isValid = otprepo.verifyOtp(email, enteredOTP);

            if (Boolean.TRUE.equals(isValid)) {
                System.out.println("✅ OTP verified successfully for " + email);
                return ResponseEntity.ok("OTP verified successfully!");
            } else {
                System.out.println("❌ Invalid or expired OTP for " + email);
                return ResponseEntity.status(400).body("Invalid or expired OTP");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Internal server error: " + e.getMessage());
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
            repo.createUser(
                    user.getId(),
                    user.getName(),
                    user.getAccountTitle(),
                    user.getFileStatus(),
                    user.getZakatdeductionstatus(),
                    user.getCnicexpirationdate(),
                    user.getLastLoginDetails(),
                    user.getDateOfBirth(),
                    user.getRegisteredHomeAddress(),
                    user.getRegisteredContactNumber(),
                    user.getRegisteredEmailAddress(),
                    user.getCity(),
                    user.getCountry()
            );

            return ResponseEntity.ok(user);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500)
                    .body(Map.of("error", "Failed to create user: " + e.getMessage()));
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User updateUser) {
        try {
            repo.updateUserPartial(
                    id,
                    updateUser.getRegisteredContactNumber(),
                    updateUser.getRegisteredEmailAddress(),
                    updateUser.getRegisteredHomeAddress(),
                    updateUser.getCity(),
                    updateUser.getCountry()
            );

            // Return updated object (optional: fetch from DB for most accurate data)
            User existing = repo.findById(id).orElse(null);
            if (existing == null) return ResponseEntity.notFound().build();
            return ResponseEntity.ok(existing);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/usersummary/{id}")
    public ResponseEntity<?> getUserSummary(@PathVariable Long id){
        UserSummary summary = summaryrepo.findByIdValue(id);
        if(summary == null){
            return ResponseEntity.status(404).body("No summary found for ID: " + id);
        }
        return ResponseEntity.ok(summary);
    }

}
