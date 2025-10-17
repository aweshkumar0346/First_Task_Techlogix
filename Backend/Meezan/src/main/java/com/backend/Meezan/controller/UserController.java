package com.backend.Meezan.controller;

import com.backend.Meezan.model.OtpStore;
import com.backend.Meezan.model.User;
import com.backend.Meezan.model.UserSummary;
import com.backend.Meezan.repo.AccountRepo;
import com.backend.Meezan.repo.OtpStoreRepo;
import com.backend.Meezan.repo.UserRepo;
import com.backend.Meezan.repo.UserSummaryRepo;
import com.backend.Meezan.service.EmailService;
import com.backend.Meezan.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService service;

    @Autowired
    private UserRepo repo;

    @Autowired
    private AccountRepo accountRepo;

    @Autowired
    private OtpStoreRepo otpRepo;

    @Autowired
    private UserSummaryRepo summaryrepo;

    @Autowired
    private EmailService emailService;

    /** STEP 1: Generate & Save OTP */
    @PostMapping("/request-otp")
    public ResponseEntity<Map<String, String>> requestOtp(@RequestBody User user) {
        String email = user.getRegisteredEmailAddress();
        String otp = String.format("%06d", (int) (Math.random() * 999999));

        try {
            // Check if existing record for email exists
            OtpStore existingOtp = otpRepo.findByEmail(email).orElse(null);

            if (existingOtp != null) {
                existingOtp.setOtp(otp);
                existingOtp.setCreatedAt(LocalDateTime.now());
                otpRepo.save(existingOtp);
            } else {
                otpRepo.save(new OtpStore(email, otp));
            }

            emailService.sendOtpEmail(email,otp);
            // ✅ Print the OTP in server console for debugging
            System.out.println("🔐 OTP generated for " + email + ": " + otp);

            Map<String, String> response = new HashMap<>();
            response.put("message", "OTP generated successfully for " + email);
            response.put("email", email);
            response.put("otp", otp); // ⚠️ remove in production (for security)
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500)
                    .body(Map.of("error", "Failed to generate OTP: " + e.getMessage()));
        }
    }

    /** STEP 2: Verify OTP */
    @PostMapping("/verify-otp")
    public ResponseEntity<Map<String, String>> verifyOtp(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String enteredOtp = body.get("otp");

        try {
            OtpStore otpRecord = otpRepo.findByEmail(email).orElse(null);

            if (otpRecord == null) {
                return ResponseEntity.status(400).body(Map.of("error", "No OTP found for this email."));
            }

            // Check if OTP matches and is not expired (e.g., valid for 5 minutes)
            boolean isValid = otpRecord.getOtp().equals(enteredOtp)
                    && otpRecord.getCreatedAt().isAfter(LocalDateTime.now().minusMinutes(5));

            if (isValid) {
                return ResponseEntity.ok(Map.of("message", "OTP verified successfully!"));
            } else {
                return ResponseEntity.status(400).body(Map.of("error", "Invalid or expired OTP."));
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500)
                    .body(Map.of("error", "Internal server error: " + e.getMessage()));
        }
    }

    /** STEP 3: Get all users */
    @GetMapping("/allusers")
    public List<User> getAllUsers() {
        return service.getAllUsers();
    }

    /** STEP 4: Get user by CNIC */
    @GetMapping("/bycnic/{cnic}")
    public ResponseEntity<User> getUserByCnic(@PathVariable Long cnic) {
        return service.getUserByCnic(cnic)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /** STEP 5: Create user with optional accounts */
    @PostMapping("/create")
    public ResponseEntity<?> createUser(@RequestBody User user) {
        try {
            User savedUser = service.createUser(user);
            return ResponseEntity.ok(savedUser);

        } catch (IllegalArgumentException e) {
            Map<String, Object> body = new HashMap<>();
            body.put("timestamp", LocalDateTime.now());
            body.put("status", 400);
            body.put("error", "Invalid CNIC");
            body.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(body);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500)
                    .body(Map.of("error", "Failed to create user: " + e.getMessage()));
        }
    }

    /** STEP 6: Partial update user */
    @PutMapping("/update/{cnic}")
    public ResponseEntity<User> updateUser(@PathVariable Long cnic, @RequestBody User updateUser) {
        try {
            repo.updateUserPartial(
                    cnic,
                    updateUser.getRegisteredContactNumber(),
                    updateUser.getRegisteredEmailAddress(),
                    updateUser.getRegisteredHomeAddress(),
                    updateUser.getCity(),
                    updateUser.getCountry()
            );

            return repo.findById(cnic)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    /** STEP 5: View UserSummar*/
    @GetMapping("/usersummary/{cnic}")
    public ResponseEntity<?> getSummaryByCnic(@PathVariable Long cnic) {
        List<UserSummary> summaries = summaryrepo.findByCnic(cnic);

        if (summaries == null || summaries.isEmpty()) {
            // Return a readable JSON error response
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("timestamp", LocalDateTime.now());
            errorResponse.put("status", 404);
            errorResponse.put("error", "User not found");
            errorResponse.put("message", "No user found with CNIC: " + cnic);

            return ResponseEntity.status(404).body(errorResponse);
        }

        // ✅ Success response
        return ResponseEntity.ok(summaries);
    }

}
