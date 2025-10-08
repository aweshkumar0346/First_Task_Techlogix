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

    // Temporary store for OTP and profile data before confirmation
    private Map<String, String> otpStore = new HashMap<>();
    private Map<String, User> pendingUser = new HashMap<>();

    // Step 1: When user submits edit form -generate OTP
    @PostMapping("/request-otp")
    public ResponseEntity<Map<String,String>> requestOtp(@RequestBody User user){

        // Generate random 6-digit OTP
        String otp = String.format("%06d", new Random().nextInt(999999));

        // Store OTP and pedning profile temporarily
        otpStore.put(user.getRegisteredEmailAddress(), otp);
        pendingUser.put(user.getRegisteredEmailAddress(), user);

        // Print OTP in console (simulate SMS/Email)
        System.out.println("Generated OTP for" + user.getRegisteredEmailAddress() + ": " +otp);

        //Response
        Map<String, String> response = new HashMap<>();
        response.put("message","OTP sent successfully to" + user.getRegisteredEmailAddress());
        response.put("email", user.getRegisteredEmailAddress());
        return ResponseEntity.ok(response);
    }

    // Step 2: Verify OTP and save profile permanently
    @PostMapping("verify-otp")
    public ResponseEntity<String> verifyOTP(@RequestBody Map<String, String> body){
        String email = body.get("email");
        String enteredOTP=body.get("otp");

        String storedotp = otpStore.get(email);

        if(storedotp != null && storedotp.equals(enteredOTP)){
            User usertosave = pendingUser.get(email);

            if(usertosave != null){
                repo.save(usertosave);
            }

            // Clear temporary data after saving
            otpStore.remove(email);
            pendingUser.remove(email);

            return ResponseEntity.ok("OTP verifed successfully. Profile saved to database!");
        }else{
            return ResponseEntity.status(400).body("Invalid or Expired OTP");
        }
    }

    @GetMapping("/allusers")
    public List<User> getAllUsers(){
        return service.getAllusers();
    }

    @GetMapping("/byid/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id){
        return service.getuserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());

    }


    @PostMapping("/create")
    public User createUser(@RequestBody User user){
        return service.createuser(user);
    }



    @PutMapping("/update/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User updateuser){
        User savedUser = service.updateUser(id, updateuser);
        return (savedUser != null) ? ResponseEntity.ok(savedUser): ResponseEntity.notFound().build();
    }

}
